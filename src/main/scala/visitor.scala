import java.util.Date

// When we mark a trait as sealed we must define all of its subtypes in the same file (eg. when all subtypes are known)
sealed trait Visitor {
    // It is good practice to never define vals in a trait, but rather to use def. 
    // Scala sees def as a more general version of val
    def id: String
    def createdAt: Date 

    def age: Long = new Date().getTime() - createdAt.getTime()
}

// final - class cannot be extended elsewhere

final case class Anonymous(id: String, createdAt: Date = new Date()) extends Visitor {}

final case class User(id: String, email: String, createdAt: Date = new Date()) extends Visitor {}

object EmailService {
    // all details about email service (eg. SMPT settings etc) would be incapsulated here

    def sendEmail(v: Visitor) = v match {
        case Anonymous(_, _) => s"Anonymous visitor without email"
        case User(_, email, _) => s"Sending email to $email"
    }

}