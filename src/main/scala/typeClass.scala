// EXAMPLE 1
// type class, has one type variable - A
trait Equal[A] {
    def equal(t1: A, t2: A): Boolean
}

// interface with implicit parameter
// For most complicated interfaces, with more than a single method, the companion object pattern would be preferred
// in our case, the Eq object is more easier to use
object Equal {
    def apply[A](implicit eq: Equal[A]): Equal[A] = eq

    implicit class EqualOps[A](in: A) {
        def ===(other: A)(implicit eq: Equal[A]) = eq.equal(in, other)
    }
}

// type class instance
object NameImplicit {
    implicit object PersonCompareByName extends Equal[Person] {
        def equal(p1: Person, p2: Person): Boolean = p1.first == p2.first && p1.last == p2.last
    }
}

object AgeImplicit {
    implicit object PersonCompareByAge extends Equal[Person] {
        def equal(p1: Person, p2: Person): Boolean = p1.age == p2.age
    }
}

object StringImplicit {
    implicit object CaseInsensitiveEqual extends Equal[String] {
        def equal(t1: String, t2: String): Boolean = t1.equalsIgnoreCase(t2)
    }
}

object Eq {
    def apply[A](p1: A, p2: A)(implicit eq: Equal[A]): Boolean = eq.equal(p1, p2)
}


// EXAMPLE 2
trait HtmlWriter[A] {
    def write(in: A): String
}

// interface using implicit parameter (aka Type Class Interface Pattern)
object HtmlWriter {
    def apply[A](implicit writer: HtmlWriter[A]): HtmlWriter[A] = writer
}

object PersonWriter extends HtmlWriter[Person] {
    def write(person: Person): String = s"<span>${person.first} &lt;${person.last}&gt;</span>"
}

object HtmlUtl {
    // The implicit keyword applies to the whole parameter list - This makes the parameter list optional
    def htmlify[A](in: A)(implicit writer: HtmlWriter[A]): String = writer.write(in)

    implicit class HtmlOps[A](data: A) {
        def toHtml(implicit writer: HtmlWriter[A]) = writer.write(data)
    }
}