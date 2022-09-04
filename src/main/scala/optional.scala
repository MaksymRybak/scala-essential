sealed trait Maybe[+A] {

    def fold[B](acc: B)(f: A => B) = this match {
        case Empty => acc
        case Full(value) => f(value)
    }

    def map[B](fn: A => B): Maybe[B] = this match {
        case Empty => Empty
        case Full(value) => Full(fn(value))
    }
    
    def mapViaFlatMap[B](fn: A => B): Maybe[B] = flatMap(value => Full(fn(value)))

    def flatMap[B](fn: A => Maybe[B]): Maybe[B] = this match {
        case Empty => Empty
        case Full(value) => fn(value)
    }
}

final case class Full[A](value: A) extends Maybe[A]

final case object Empty extends Maybe[Nothing]


