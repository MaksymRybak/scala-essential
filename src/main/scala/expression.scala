import scala.annotation.tailrec

sealed trait Expression {
    // @tailrec
    def eval: ExpResult = this match {
        case Number(value) => SuccessRes(value)
        case Addition(left, right) => left.eval match {
            case FailureRes(msg) => FailureRes(msg)
            case SuccessRes(l) => right.eval match {
                case FailureRes(msg) => FailureRes(msg)
                case SuccessRes(r) => SuccessRes(l + r) 
            } 
        }
        case Subtraction(left, right) => left.eval match {
            case FailureRes(msg) => FailureRes(msg)
            case SuccessRes(l) => right.eval match {
                case FailureRes(msg) => FailureRes(msg)
                case SuccessRes(r) => SuccessRes(l - r) 
            } 
        }
        case Division(left, right) => right.eval match {
            case FailureRes(msg) => FailureRes(msg)
            case SuccessRes(r) => r match {
                case 0 => FailureRes("Division by zero")
                case _ => left.eval match {
                    case SuccessRes(l) => SuccessRes(l / r)
                    case FailureRes(msg) => FailureRes(msg)
                }
            }
        } 
        case SquareRoot(exp) => exp.eval match {
            case FailureRes(msg) => FailureRes(msg)
            case SuccessRes(res) => if (res < 0) FailureRes("Square root of negative number") else SuccessRes(math.sqrt(res))
        }
    }

    def evalV2: Sum[String, Double] = this match {
        case Number(v) => RightType(v)
        case Addition(l, r) => lift2(l, r, (left, right) => RightType(left + right))
        case Subtraction(l, r) => lift2(l, r, (left, right) => RightType(left - right))
        case Division(l, r) => lift2(l, r, (left, right) => 
            if (right == 0) 
                LeftType("Division by zero")                    // Failure
            else
                RightType(left / right)                         // Success
        )
        case SquareRoot(v) => v.evalV2 flatMap { value => 
            if (value < 0) 
                LeftType("Square root of negative number")      // Failure
            else 
                RightType(math.sqrt(value))                     // Success
        }
    }

    private def lift2(l: Expression, r: Expression, f: (Double, Double) => Sum[String, Double]) = 
        l.evalV2 flatMap { left =>
            r.evalV2 flatMap { right => 
                f(left, right)
            }
        }
}

final case class Addition(left: Expression, right: Expression) extends Expression
final case class Subtraction(left: Expression, right: Expression) extends Expression
final case class Division(left: Expression, right: Expression) extends Expression
final case class SquareRoot(exp: Expression) extends Expression
final case class Number(value: Double) extends Expression


sealed trait ExpResult
final case class SuccessRes(res: Double) extends ExpResult
final case class FailureRes(msg: String) extends ExpResult
