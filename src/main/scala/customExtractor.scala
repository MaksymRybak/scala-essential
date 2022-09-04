// p. 171

// EXAMPLE: custom fixed-length extractors
// extractor below matches email addresses and splits them into their user and domain parts
object Email {
    def unapply(email: String): Option[(String, String)] = {
        val parts = email.split("@")
        if (parts.length == 2) Some((parts(0), parts(1))) else None
    }
}

// This simpler pattern matches any string and uppercases it:
object Uppercase {
    def unapply(str: String): Option[String] = Some(str.toUpperCase)
}

object Positive {
    def unapply(num: Int): Option[Int] = if (num > 0) Some(num) else None
}

object Titlecase {
    def unapply(str: String): Option[String] = {
        val parts = str.split(" ")
        val res = parts.map(_.capitalize)
        Some(res.mkString(" "))
    }
}

// EXAMLPE: custom variable-length extractors
// we have to define the method: def unapplySeq(value: A): Option[Seq[B]]
// Variable-length extractors match a value only if the pattern in the case clause is the same length as the Seq returned by unapplySeq. Regex and List are examples of variable-length extractors

// splits a string into its component words
object Words {
    def unapplySeq(str: String) = Some(str.split(" ").toSeq)
}
