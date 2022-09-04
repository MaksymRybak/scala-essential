// recursive data structure
// invariant sum type pattern
sealed trait LinkedList[A] {

    // fold is a function that permits the transformation of algebraic data type
    // A - algebraic data type
    // B - generic type, we convert to B from A
    // NB: see page 92 for detailed explanation
    // fold is used to abstract stractural recursion patterns
    def fold[B](end: B)(pair: (A, B) => B): B = this match {
        case End() => end
        case Pair(hd, tl) => pair(hd, tl.fold(end)(pair))
    }

    // NB: not tail recursive (stack based)
    // @tailrec
    def length: Int = fold(0){ (_, acc) => acc + 1 }   // parameter list permits to have function call that looks like a code block (more readable), AND helps Scala to infer the type (eg. in our case it's Int refered from first parameter list, see p.93 for details)

    def contains(element: A): Boolean = this match {
        case End() => false
        case Pair(head, tail) => if (head == element) true else tail.contains(element)
    }

    def apply(nth: Int): Result[A] = this match {
        case End() => Failure("Index out of bounds")
        case Pair(head, tail) => if (nth == 0) Success(head) else tail(nth - 1)
    }

    def map[B](fn: A => B): LinkedList[B] = this match {
        case End() => End()
        case Pair(head, tail) => Pair(fn(head), tail.map(fn))
    }
    
}
final case class End[A]() extends LinkedList[A]                               // base case, we return identity in match case (identity is an element that doesn't change the result)
final case class Pair[A](head: A, tail: LinkedList[A]) extends LinkedList[A]     // recursive case