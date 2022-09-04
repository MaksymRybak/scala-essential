object StringExtensions {

    //  if we tag our class with the implicit keyword, we give Scala the ability to insert the constructor call automatically into our code (scala compiler does that)
    implicit class ExtraStringMethods(str: String) {
        val vowels = Seq('a', 'e', 'i', 'o', 'u')
    
        def numberOfVowels = str.toList.filter(vowels contains _).length
    }

}

object IntImplicits {

    implicit class IntOps(num: Int) {
        // def yeah = (1 to num).foreach(_ => println("Oh yeah!"))

        def yeah = times(_ => println("Oh yeah!"))

        def times(f: Int => Unit) = for {
            _ <- 1 to num
        } f(num)
    }

}

object IntImplicitConversions {

    implicit def toIntOps(num: Int): IntImplicits.IntOps = new IntImplicits.IntOps(num)

}
