/* 
    The goal is to be able to rapresent this json:

    ["a string", 1.0, true]

    {
        "a": [1,2,3],
        "b": ["a","b","c"]
        "c": { "doh":true, "ray":false, "me":1 }
    }

*/

/* 
    We can model our data structure using BNF notation:
   
    Json ::= JsNumber value:Double
        |    JsBoolean value:Boolean
        |    JsString value:String
        |    JsNull
        |    JsSequence
        |    JsObject
    
    JsSequence ::= SeqCell head:Json tail:JsSequence
        |          SelEnd

    JsObject ::= ObjectCell key:String value:Json tail:JsObject
        |        ObjectEnd 

*/

sealed trait Json {
    def quote(s: String): String = '"'.toString ++ s ++ '"'.toString

    def seqToJson(seq: SeqCell): String = seq match {
        case SeqCell(h, t @ SeqCell(_, _)) => s"${h.print}, ${seqToJson(t)}"
        case SeqCell(h, SeqEnd) => h.print
    }

    def objectToJson(obj: ObjectCell): String = obj match {
        case ObjectCell(k, v, t @ ObjectCell(_, _, _)) => s"${quote(k)}: ${v.print}, ${objectToJson(t)}"
        case ObjectCell(k, v, ObjectEnd) => s"${quote(k)}: ${v.print}"
    }

    def print: String = this match {
        case JsNumber(v) => v.toString
        case JsString(v) => quote(v)
        case JsBoolean(v) => v.toString
        case JsNull => "null"
        case s @ SeqCell(_, _) => "[" ++ seqToJson(s) ++ "]"
        case SeqEnd => "[]"
        case o @ ObjectCell(_, _, _) => "{" ++ objectToJson(o) ++ "}"
        case ObjectEnd => "{}"
    }
}
final case class JsString(value: String) extends Json 
final case class JsNumber(value: Double) extends Json
final case class JsBoolean(value: Boolean) extends Json
final case object JsNull extends Json

sealed trait JsSequence extends Json
final case object SeqEnd extends JsSequence
final case class SeqCell(head: Json, tail: JsSequence) extends JsSequence

sealed trait JsObject extends Json
final case object ObjectEnd extends JsObject
final case class ObjectCell(key: String, value: Json, tail: JsObject) extends JsObject

