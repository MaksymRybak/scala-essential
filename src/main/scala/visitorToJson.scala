import JsonConversion._
import java.util.Date

object VisitorToJsonConversions {
    import JsExtensions._

    implicit object StringWriter extends JsWriter[String] {
        def write(value: String) = JsString(value)
    }
    
    implicit object DateWriter extends JsWriter[Date] {
        def write(value: Date) = JsString(value.toString)
    }
    
    implicit object AnonymousWriter extends JsWriter[Anonymous] {
        def write(in: Anonymous): JsValue = JsObject(Map(
            "id" -> in.id.toJson,
            "createdAt" -> in.createdAt.toJson
        ))
    }
    
    implicit object UserWriter extends JsWriter[User] {
        def write(in: User): JsValue = JsObject(Map(
            "id" -> in.id.toJson,
            "email" -> in.email.toJson,
            "createdAt" -> in.createdAt.toJson
        ))
    }
    
    implicit object VisitorWriter extends JsWriter[Visitor] {
        def write(in: Visitor): JsValue = in match {
            case an: Anonymous => an.toJson
            case user: User => user.toJson
        }
    }
}