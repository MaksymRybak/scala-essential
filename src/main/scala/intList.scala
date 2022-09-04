import scala.annotation.tailrec
sealed trait IntList {
  // @tailrec
  // param f is functional type (fold method is generic one, so the user have to specify the function that is ok to work with generic type)
  def fold[A](acc: A, f: (Int, A) => A): A =
    this match {
      case ILEnd          => acc
      case ILPair(hd, tl) => f(hd, tl.fold(acc, f))
    }

  def length: Int = fold[Int](0, (a, acc) => acc + 1)

  def double: IntList = fold[IntList](ILEnd, (a, acc) => ILPair(a*2, acc))

  def product: Int = fold[Int](1, (a, acc) => acc * a)

  def sum: Int = fold[Int](0, (a, acc) => acc + a)
}

final case object ILEnd extends IntList
final case class ILPair(head: Int, tail: IntList) extends IntList
