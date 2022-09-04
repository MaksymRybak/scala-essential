// sum type (OR)
sealed trait Result[A]

final case class Success[A](res: A) extends Result[A]
final case class Failure[A](msg: String) extends Result[A]

object Calculator {

    def +(c: Result[Int], n: Int) = c match {
        case Success(res) => Success(res + n)
        case Failure(msg) => Failure(msg)
    }

    def -(c: Result[Int], n: Int) = c match {
        case Success(res) => Success(res - n)
        case Failure(msg) => Failure(msg)
    }
    
    def /(c: Result[Int], n: Int) = c match {
        case Success(res) => n match {
            case 0 => Failure("Divide by zero")
            case _ => Success(res / n)
        }
        case Failure(msg) => Failure(msg)
    }

}