sealed trait DivisionResult {}

final case class Finite(res: Int) extends DivisionResult {}
final case object Infinite extends DivisionResult {}

object divide {
    def apply(a: Int, b: Int): DivisionResult = if (b == 0) Infinite else new Finite(a/b)
}