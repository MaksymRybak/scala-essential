package JsonConversion {

    sealed trait JsValue {
        def stringify: String
    }
    
    final case class JsObject(values: Map[String, JsValue]) extends JsValue {
        def stringify = values
            .map { case (name, value) => "\"" + name + "\":" + value.stringify }
            .mkString("{", ",", "}")
    
    }

    final case class JsString(value: String) extends JsValue {
        def stringify = "\"" + value.replaceAll("\\|\"", "\\\\$1") + "\""
    }

    // type class for converting Scala data to JSON
    trait JsWriter[A] {
        def write(in: A): JsValue
    }

    object JsUtil {
        def toJson[A](in: A)(implicit jsWriter: JsWriter[A]) = jsWriter.write(in)
        //def toJson[A : JsWriter](in: A) = implicitly[JsWriter[A]].write(in)           // same using Type Bounds and implicitle method, but using explicitely the implicit param, the first version is more correct
    }

    object JsExtensions {
        implicit class JsUtilExtension[A](value: A) {
            def toJson(implicit writer: JsWriter[A]) = writer write value 
        }
    }
}
