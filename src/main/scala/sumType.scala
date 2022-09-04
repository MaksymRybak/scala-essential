sealed trait Sum[+A, +B] {
    def fold[C](left: A => C, right: B => C): C = this match {
        case LeftType(value) => left(value)
        case RightType(value) => right(value)
    }

    def map[C](fn: B => C): Sum[A, C] = this match {
        case LeftType(value) => LeftType(value)
        case RightType(value) => RightType(fn(value))
    }

    def flatMap[AA >: A, C](fn: B => Sum[AA, C]): Sum[AA, C] = this match {
        case LeftType(value) => LeftType(value)
        case RightType(value) => fn(value)
    }
}

final case class LeftType[A](value: A) extends Sum[A,Nothing] 

final case class RightType[B](value: B) extends Sum[Nothing,B] 