case class Person(first: String, last: String, age: Int = 18) {
    def sayHello = println(s"Hello $first $last")
    def copy(firstName: String = first, lastName: String = last) = new Person(firstName, lastName)
}

object Person {
    def apply(completeName: String) = {
        val splitted = completeName.split(" ")
        new Person(splitted(0), splitted(1))
    }

    def older(p1: Person, p2: Person): Person = if (p1.age > p2.age) p1 else p2
}
