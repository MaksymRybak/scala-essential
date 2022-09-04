import scala.annotation.tailrec
import scala.collection.immutable.Queue
import scala.collection.immutable.ArraySeq
import java.util.Date
import java.util.Calendar
import scala.util.Try
import scala.util.matching.Regex

object HelloScala extends App {
    println("Hello Scala")

    // classes and objects
    val p1 = new Person("Mario", "Rossi", 30)
    val p2 = new Person(last = "Polo", first = "Marco")

    p1.sayHello
    p2.sayHello

    println(greet(p1))
    println(greetCustom(last = "Rossi"))

    p1.copy(lastName = "Pluto").sayHello

    val res = new Counter(10).inc(5).dec().inc().inc().count
    println(res)
    assert(res == 16)

    println(Counter(10).adjust(new Adder(5).apply).count)

    def greet(p: Person) = s"Greetings, ${p.first} ${p.last}"
    def greetCustom(first: String = "Some", last: String = "Some") = s"Greeting, $first $last"

    // function application
    val adder = new Adder(10)
    println(adder(15))

    println(Timestamp(1,1).seconds)
    println(Timestamp(1,1,1).seconds)

    // companion objects
    Person("Mario Superstar").sayHello

    print("Older, ")
    Person.older(p1, p2).sayHello

    // case classes
    val f1 = FilmV1("Titanic", 2000, DirectorV1("Clint", "Eastwood"))
    val f2 = FilmV1("Titanic", 2000, DirectorV1("John", "McTiernan"))
    println(f1)
    //assert(f1 == f2)
    //assert(f1 equals f2)
    println(f1.copy(name="Pulp Fiction"))

    val f3 = FilmV1("Men in black")
    println(f3)

    // pattern matching
    println(Stormtrooper.inspect(Person("Mario", "Rossi")))
    println(Stormtrooper.inspect(Person("Marco", "Rossi")))

    println(Dad.rate(f1))
    println(Dad.rate(f2))
    println(Dad.rate(f3))

    // extractors
    val r = new Regex("""(\d+)\.(\d+)\.(\d+)\.(\d+)""")
    val extracted = "192.168.0.1" match {
        case r(a, b, c, d) => List(a, b, c, d)      // pattern that binds each of the captured groups
    }
    println(s"extracted: $extracted")

    val l = Nil
    val l1 = List(1,2,3)
    println( 
        l1 match {
            case List(a) => "length 1"
            //case ::(head, tail) => s"head $head tail $tail"    //  binary extractor pattern
            case head :: tail => s"head $head tail $tail"        //  binary extractor pattern can also be written infix
            case Nil => "length 0"
        }
    )
    // Combined use of ::, Nil, and _ allow us to match the first elements of any length of list
    List(1, 2, 3) match {
        case Nil => "length 0"
        case a :: Nil => s"length 1 starting $a"
        case a :: b :: Nil => s"length 2 starting $a $b"
        case a :: b :: c :: _ => s"length 3+ starting $a $b $c"
    }

    println("rybak.maksym@gmail.com" match { case Email(user, domain) => s"extracted from email: $user at $domain"})
    println("hello world" match { case Uppercase(s) => s })
    Person("Dave", "Gurnell", 30) match {
        case Person(f, Uppercase(l), _) => s"$f $l"
        case _ => "unknown person"
    }

    assert(
        "No" == 
            (0 match {
                    case Positive(_) => "Yes"
                    case _ => "No"
            })
    )

    assert(
        "Yes" ==
            (42 match {
                case Positive(_) => "Yes"
                case _ => "No"
            })
    )

    assert(
        "Sir Lord Doctor David Gurnell" ==
            ("sir lord doctor david gurnell" match {
                case Titlecase(str) => str
            })
    )

    "the quick brown fox" match {
        case Words(a, b, c) => s"3 words: $a $b $c"
        case Words(a, b, c, d) => s"4 words: $a $b $c $d"       // this case will match 
    }

    List(1, 2, 3, 4, 5) match {
        case List(a, b, _*) => a + b    // The wildcard sequence pattern, written _*, matches zero or more arguments from a variable-length pattern and discards their values
    }

    "the quick brown fox" match {
        case Words(a, b, _*) => a + b   // we don't have access to remaining elements
    }

    "the quick brown fox" match {
        case Words(a, b, rest @ _*) => println(rest)     // we can access remaining elements using rest 
    }
    //println("--------------")

    // traits
    def older(v1: Visitor, v2: Visitor) = v1.createdAt.before(v2.createdAt)

    val v1: Visitor = Anonymous("1")
    val v2: Visitor = User("2", "mario@gmail.com")
    println(s"v1 is older v2: ${older(v1,v2)}")
    println(s"v1 age: ${v1.age}")
    println(s"v2 age: ${v2.age}")

    val s1 = Circle(2, Red)
    val s2 = Rectangle(10, 20, Yellow)
    val s3 = Square(50, Pink)
    val s4 = Square(50, NewColorFor(250,150,50))

    println(Draw(s1))
    println(Draw(s2))
    println(Draw(s3))
    println(Draw(s4))

    println(divide(4,2))
    println(divide(4,3))
    println(divide(4,0))

    println(
        divide(1,0) match {
            case Finite(res) => s"Finite result $res"
            case Infinite => s"Infinite result"
        }
    )

    // structural recursion (pattern matching / polymorphic)
    // structural recursion - is breaking data in smaller pieces
    // polymorphic example (oop like) p. 70 - see feline.scala
    // pattern matching example (fp like) p. 71 - see feline.scala

    println(Calculator.+(Success(1), 1))
    println(Calculator.-(Success(1), 1))
    println(Calculator./(Success(1), 0))
    println(Calculator.+(Failure("Badness"), 1))

    // recursive data
    val example = Pair(1, Pair(2, Pair(3, End())))
    assert(sum(example) == 6)
    assert(sum(example.tail) == 5)
    assert(sum(End()) == 0)

    assert(example.length == 3)
    assert(example.tail.length == 2)
    assert(End().length == 0)

    assert(example.contains(3) == true)
    assert(example.contains(4) == false)
    assert(End().contains(0) == false)

    assert(example(0) == Success(1))
    assert(example(1) == Success(2))
    assert(example(2) == Success(3))
    assert(example(3) == Failure("Index out of bounds"))

    val intList = ILPair(1, ILPair(2, ILPair(3, ILEnd)))
    assert(intList.length == 3)
    assert(intList.tail.length == 2)
    assert(ILEnd.length == 0)

    assert(intList.sum == 6)
    assert(intList.tail.sum == 5)
    assert(ILEnd.sum == 0)

    assert(intList.product == 6)
    assert(intList.tail.product == 6)
    assert(ILEnd.product == 1)

    assert(intList.double == ILPair(2, ILPair(4, ILPair(6, ILEnd))))
    assert(intList.tail.double == ILPair(4, ILPair(6, ILEnd)))
    assert(ILEnd.double == ILEnd)

    val tree: Tree[Int] = Node(Node(Leaf(1), Leaf(3)), Leaf(2))
    // assert(tree.sum == 6)
    // assert(tree.double == Node(Node(Leaf(2), Leaf(6)), Leaf(4)))

    val expression: Expression = Addition(Number(1), Number(2));
    assert(expression.eval == SuccessRes(3))

    assert(Addition(SquareRoot(Number(-1.0)), Number(2.0)).eval == FailureRes("Square root of negative number"))
    assert(Addition(SquareRoot(Number(4.0)), Number(2.0)).eval == SuccessRes(4.0))
    assert(Division(Number(4), Number(0)).eval == FailureRes("Division by zero"))

    {
        val jsonRes1 = SeqCell(JsString("a string"), SeqCell(JsNumber(1.0), SeqCell(JsBoolean(true), SeqEnd))).print
        println(jsonRes1)
    
        val jsonRes2 = ObjectCell(
            "a", SeqCell(JsNumber(1.0), SeqCell(JsNumber(2.0), SeqCell(JsNumber(3.0), SeqEnd))), ObjectCell(
            "b", SeqCell(JsString("a"), SeqCell(JsString("b"), SeqCell(JsString("c"), SeqEnd))), ObjectCell(
            "c", ObjectCell(
                "doh", JsBoolean(true), ObjectCell(
                "ray", JsBoolean(false), ObjectCell(
                "me", JsNumber(1.0), ObjectEnd))), ObjectEnd))
            ).print
        println(jsonRes2)
    }

    @tailrec
    def sum(list: LinkedList[Int], total: Int = 0): Int = list match {
        case End() => total                               // here we return the identity of operation
        case Pair(head, tail) => sum(tail, total + head)   // here we make recursive call
    }

    // sequencing computation   (using generics, functions we can build abstraction like functors, and monads)
    println(Box(1))
    println(Box("Hi"))

    def generic[A](value: A) = value
    println(generic[Int](1))
    println(generic("Hi"))  // scala infering type automatically

    // functions (allow us to abstract over methods) - function types
    // function literas
    val sum = (a: Int, b: Int) => a + b
    println(sum(1,3))

    val add1 = ((_: Int) + 1)
    val add1V2 = (a: Int) => a + 1
    println(add1(1))
    println(add1V2(1))

    println(Counter(2).adjust(MathStuff.add1))

    def multParamList(x: Int)(y: Int) = x + y

    println(multParamList(1)(2))

    val treeAlgType: Tree[String] =
        Node(Node(Leaf("To"), Leaf("iterate")),
            Node(Node(Leaf("is"), Leaf("human,")),
                Node(Leaf("to"), Node(Leaf("recurse"), Leaf("divine")))))

    println(treeAlgType.fold[String]((a, b) => a + " " + b, str => str))

    // generic product types (eg. Tuple)
    val boxIntAndString = PairBox(1, "Hi")
    println(boxIntAndString.one)
    println(boxIntAndString.two)

    // tuples
    println(Tuple3(1, true, "Hello World"))
    println((1, true, "Hello World"))
    println((1, "Hello World"))

    def printTuple[A,B](t: (A, B)) = {
        println(t)
    }

    printTuple((true, "story"))

    // println(
    //     (1, "a") match {
    //         case (a, b) => a + b
    //     }
    // )

    // generic sum types (eg. Either)
    println(Left[Int, String](1).value)
    println(Right[Int, String]("Hello").value)

    val sumT: Sum[Int, String] = RightType("foo")
    val sumVal = sumT match {
        case LeftType(num) => num.toString
        case RightType(str) => str
    }
    println(s"sumVal: $sumVal")

    //val either: Either[Int, Boolen] = Left(1)

    // generic optional value (eg. Option)
    val maybe1: Maybe[Int] = Full(1)
    val maybe2: Maybe[Int] = Empty
    println(maybe1)
    println(maybe2)

    println(maybe1.fold("no-value"){ value => value.toString })
    println(maybe2.fold("no-value"){ value => value.toString })

    // map
    val list = Pair(1, Pair(2, Pair(3, End())))
    println(list.map(el => el * 2))

    // NB: A type like F[A] with a map method is called a functor. If a functor also has a flatMap method it is called a monad

    def mightFail1: Maybe[Int] = Full(1)
    def mightFail2: Maybe[Int] = Full(2)
    def mightFail3: Maybe[Int] = Full(3)
    //def mightFail3: Maybe[Int] = Empty() // This one failed

    // the general idea is a monad represents a value in some context (eg. context is Option / List / Tuple)
    println(
        mightFail1 flatMap { value1 => 
            mightFail2 flatMap { value2 => 
                mightFail3 flatMap { value3 => Full(value1 + value2 + value3) }     // creating new Full = creating new context with transformed value
            }
        }
    )

    // NB: We use map when we want to transform the value within the context to a new value, while keeping the context the same. 
    //     We use flatMap when we want to transform the value and provide a new context. 
    // p. 101

    println(list.map(_ * 2))
    println(list.map(_ + 1))
    println(list.map(_ / 3))

    val list1 = List(1, 2, 3)
    println(list1.flatMap(el => List(el, -el)))

    val list2 = List(Full(3), Full(2), Full(1))
    val list2new = list2.map(el => el match {
        case Full(value) => if (value % 2 == 0) el else Empty
        case _ => Empty
    })
    val list2newV2 = list2.map(el => el.flatMap(x => if (x % 2 == 0) Full(x) else Empty))
    println(list2new)
    println(list2newV2)

    // variance (if A is a subtype of B, Foo[A] is a subtype of Foo[B]? 
    //           -> depends on variance of type Foo) see p. 104!
    // This pattern is the most commonly used one with generic sum types. We should only use covariant types where the container type is immutable. If the container allows mutation we should only use invariant types.

    assert(Addition(Number(1), Number(2)).evalV2 == RightType(3))
    assert(SquareRoot(Number(-1)).evalV2 == LeftType("Square root of negative number"))
    assert(Division(Number(4), Number(0)).evalV2 == LeftType("Division by zero"))
    assert(Division(Addition(Subtraction(Number(8), Number(6)), Number(2)), Number(2)).evalV2 == RightType(2.0))

    // collections
    // sequence
    val seq = Seq(1,2,3)
    println(seq)
    println(seq(1))

    println(Seq().headOption)
    println(Seq(1,3,5).find(_ == 3))
    println(Seq(1,3,5).find(_ > 10))
    println(Seq(10,3,5,1).sortWith(_ < _))

    println(seq :+ 10)  // append (infix operator)
    println(0 +: seq)   // prepend 

    println(Seq(1,2,3) ++ Seq(4,5,6))   // concatenate sequences
    
    // lists
    println(Nil)
    val listV2 = 1 :: 2 :: 3 :: Nil     // prepending element to the list
    println(listV2)

    println(List(1,2,3) ::: List(4))

    // :: and ::: are specific to lists whereas +:, :+ and ++ work on any type of sequence

    println(Vector(1,2,3))
    println(Queue(1,2,3))
    println(ArraySeq(1,2,3))

    println(listV2.mkString(","))

    val animals = Seq("cat", "dog", "penguin")
    println ( "mouse" +: animals :+ "tyrannosaurus" )

    println ( 2 +: animals )

    // 
    val memento = new Film("Memento", 2000, 8.5)
    val darkKnight = new Film("Dark Knight", 2008, 9.0)
    val inception = new Film("Inception", 2010, 8.8)
    val highPlainsDrifter = new Film("High Plains Drifter", 1973, 7.7)
    val outlawJoseyWales = new Film("The Outlaw Josey Wales", 1976, 7.9)
    val unforgiven = new Film("Unforgiven", 1992, 8.3)
    val granTorino = new Film("Gran Torino", 2008, 8.2)
    val invictus = new Film("Invictus", 2009, 7.4)
    val predator = new Film("Predator", 1987, 7.9)
    val dieHard = new Film("Die Hard", 1988, 8.3)
    val huntForRedOctober = new Film("The Hunt for Red October", 1990, 7.6)
    val thomasCrownAffair = new Film("The Thomas Crown Affair", 1999, 6.8)

    val eastwood = new Director("Clint", "Eastwood", 1930, Seq(highPlainsDrifter, outlawJoseyWales, unforgiven, granTorino, invictus))
    val mcTiernan = new Director("John", "McTiernan", 1951, Seq(predator, dieHard, huntForRedOctober, thomasCrownAffair))
    val nolan = new Director("Christopher", "Nolan", 1970, Seq(memento, darkKnight, inception))
    val someGuy = new Director("Just", "Some Guy", 1990, Seq())

    val directors = Seq(eastwood, mcTiernan, nolan, someGuy)

    def directorsWithMoreThan(numberOfFilms: Int) = directors.filter(_.films.length > numberOfFilms)
    def directorBornBefore(year: Int): Option[Director] = directors.find(_.yearOfBirth < year)
    def directorsSearch(year: Int, numberOfFilms: Int) = directors.filter(d => d.yearOfBirth < year && d.films.length > numberOfFilms)
    def directorsSortedByAge(ascending: Boolean) = ascending match {
        case true => directors.sortWith(_.yearOfBirth < _.yearOfBirth)
        case false => directors.sortWith(_.yearOfBirth > _.yearOfBirth)
    }
    
    println(directorsWithMoreThan(3))
    println(directorsWithMoreThan(4))

    println(directorBornBefore(2000))
    println(directorBornBefore(1930))
    
    println(directorsSearch(1900, 2))
    println(directorsSearch(1960, 2))
    println(directorsSearch(2000, 3))

    println(directorsSortedByAge(true))
    println(directorsSortedByAge(false))

    // working with sequences
    println("cat".permutations.toList)

    println("Directed by Christopher Nolan: " + nolan.films.map(_.name))
    println("All films by all directors: " + directors.flatMap(_.films.map(_.name)))
    println("The date of the earliest McTiernan film: " + mcTiernan.films.sortWith(_.yearOfRelease < _.yearOfRelease).head.yearOfRelease)
    println("All films sorted by descending IMDB rating: " + directors.flatMap(_.films).sortWith(_.yearOfRelease > _.yearOfRelease))
    val allFilms = directors.flatMap(_.films);  // aka cache all films as are iterated twice
    println("Average score across all films: " + allFilms.foldLeft(0.0)(_ + _.imdbRating) / allFilms.length)

    directors foreach { d => 
        d.films foreach { f => 
            println(s"Tonight only! ${f.name} by ${d.firstName} ${d.lastName}!")
        }
    }

    println("The earliest film by any director: " + directors
                    .flatMap(director => director.films)
                    .sortWith((a, b) => a.yearOfRelease < b.yearOfRelease)
                    .headOption)

    def smallest(seq: Seq[Int]) = seq.foldLeft(Int.MaxValue)(math.min)

    println(smallest(Seq(-10, 10, -20, 25)))
    println(smallest(Seq()))

    def unique(seq: Seq[Int]) = seq.foldLeft(Seq[Int]()) { 
        (acc, num) => if (acc.contains(num)) acc else num +: acc 
    }

    println(unique(Seq(1, 1, 2, 4, 3, 4)))

    def reverse[A](seq: Seq[A]) = seq.foldLeft(Seq.empty[A]) {
        (acc, num) => num +: acc 
    }

    println(reverse(Seq(1, 1, 2, 4, 3, 4)))

    def map[A, B](seq: Seq[A], f: A => B): Seq[B] = seq.foldRight(Seq.empty[B]) {
        (num, acc) => f(num) +: acc
    }

    println(map[Int, Int](Seq(1, 1, 2, 4, 3, 4), { n => n * 2 }))

    def foldLeftMutable[A, B](seq: Seq[A], identity: B, f: (B, A) => B): B = {
        var res = identity
        seq.foreach { el => res = f(res, el) }
        res
    }

    println(foldLeftMutable[Int, Int](Seq(1,2,3), 0, (a,b) => a + b))

    // for comprehensions
    val newSeq = for {
        x <- Seq(1, 2, 3)       // pattern on the left <- generator expression on the right
    } yield x * 2
    // a for comprehension iterates over the elements in the generator, binding each element to the pattern and calling the yield expression -> It combines the yielded results into a sequence of the same type as the original generator.

    println(newSeq)

    val data = Seq(Seq(1), Seq(2, 3), Seq(4, 5, 6))
    println(data.flatMap(_.map(_ * 2))) 

    val dataNew = for {
        innerSeq <- data
        num <- innerSeq
    } yield num * 2
    println(dataNew)

    // The intuitive understanding of the code is to iterate through all of the sequences in the generators, mapping the yield expression over every element therein, 
    // and accumulating a result of the same type as sequence fed into the first generator.
    println(
        for {
            a <- Seq(1, 2, 3)
            b <- Seq(2)
            c <- Seq(3)
        } yield a + b + c   // if we omit yield, the result type is Unit, used to produce side effect
    )

    // same written in different style
    for(x <- Seq(1, 2, 3)) yield {
        x * 2
    }

    // films directed by Christopher Nolan 
    println(
        for {
            f <- nolan.films
        } yield f.name
    )

    // the names of all films by all directors.
    println(
        for {
            d <- directors
            f <- d.films
        } yield f.name
    )

    // all films sorted by descending IMDB rating
    println(
        (for {
            d <- directors
            f <- d.films
        } yield f) sortWith { 
            _.yearOfRelease > _.yearOfRelease 
        }
    )

    // side effect
    for {
        d <- directors
        f <- d.films
    } println(s"Tonight only! ${f.name} by ${d.firstName} ${d.lastName}!")

    println(
        for {
            n <- Seq(1,2,3)
            s <- Seq("a","b","c")
        } yield Some(s"$n $s")
    )

    // Option
    println(Seq(Some(1), None, Some(3)).flatMap(x => x))

    println(
        for {
            n1 <- Option(1)
            n2 <- Option(2)
        } yield n1 + n2
    )
    // We can think of each clause in the for comprehension as an expression that says: if this clause results in a Some, extract the value and continue… if it results in a None, exit the for comprehension and return None

    def addOptions(p1: Option[Int], p2: Option[Int]) = for {
        n1 <- p1
        n2 <- p2
    } yield n1 + n2

    println(addOptions(Some(2), Some(3)))
    println(addOptions(Some(2), None))

    def addOptionsV2(p1: Option[Int], p2: Option[Int]) = 
        p1 flatMap { n1 => 
            p2 map { n2 => 
                n1 + n2
            }    
        }

    println(addOptionsV2(Some(2), Some(3)))
    println(addOptionsV2(Some(2), None))

    def addOptions(p1: Option[Int], p2: Option[Int], p3: Option[Int]) = for {
        n1 <- p1
        n2 <- p2
        n3 <- p3
    } yield n1 + n2 + n3

    def addOptionsV2(p1: Option[Int], p2: Option[Int], p3: Option[Int]) = 
        p1 flatMap { n1 => 
            p2 flatMap { n2 => 
                p3 map { n3 =>
                    n1 + n2 + n3
                }
            }    
        }

    def divideNums(n1: Int, n2: Int): Option[Int] = if (n2 == 0) None else Some(n1 / n2)
    
    def divideOptions(p1: Option[Int], p2: Option[Int]) = for {
        n1 <- p1
        n2 <- p2
        res <- divideNums(n1, n2)   // NB: to extract the result of division
    } yield res                     //     and wrap to Option again

    println(divideOptions(Some(4), Some(2)))
    println(divideOptions(Some(4), Some(0)))

    def readInt(str: String): Option[Int] = if(str matches "\\d+") Some(str.toInt) else None

    def calculator(operand1: String, operator: String, operand2: String): Unit = {
        def calcInternal(a: Int, b: Int) =
            operator match {
                case "+" => Some(a + b)
                case "-" => Some(a - b)
                case "*" => Some(a * b)
                case "/" => divideNums(a, b)
                case _ => None
            }

        val calcRes = for {
            o1 <- readInt(operand1)
            o2 <- readInt(operand2)
            res <- calcInternal(o1, o2)
        } yield res

        calcRes match {
            case Some(res) => println(s"$operand1 $operator $operand2 = $res")
            case None => println("Calculation error")
        }
    }

    calculator("1", "+", "5")
    calculator("1", "-", "5")
    calculator("2", "*", "5")
    calculator("10", "/", "5")
    calculator("10", "/", "0")
    calculator("a", "/", "2")
    
    // Monads
    val opt1 = Some(1)
    val opt2 = Some(2)
    val opt3 = Some(3)

    println(
        for {
            o1 <- opt1
            o2 <- opt2
            o3 <- opt3
        } yield o1 + o2 + o3
    )

    val seq1 = Seq(1)
    val seq2 = Seq(2)
    val seq3 = Seq(3)

    println(
        for {
            o1 <- seq1
            o2 <- seq2
            o3 <- seq3
        } yield o1 + o2 + o3
    )

    val try1 = Try(1)
    val try2 = Try(2)
    val try3 = Try(3)

    println(
        for {
            o1 <- try1
            o2 <- try2
            o3 <- try3
        } yield o1 + o2 + o3
    )

    // filtering in for comprehension
    println(
        for(x <- Seq(-2, -1, 0, 1, 2) if x > 0) yield x
    )

    // parallel iteration (zip is used to create new sequence with pairs for each element on the same index)
    println(
        for (x <- Seq(1,2,3).zip(Seq(2,3,4))) yield { val (a,b) = x; a + b }
    )

    println(
        for(x <- Seq(1, 2, 3).zipWithIndex) yield x
    )

    // pattern matching
    println(
        for ((a, b) <- Seq(1,2,3).zip(Seq(2,3,4))) yield a + b      // remember, on the left of <- we can place a pattern, on the right of <- we have a generator (with possible filter)
    )

    // intermediate results
    println(
        for {
            x <- Seq(1, 2, 3)
            square = x * x          // intermediate result
            y <- Seq(4, 5, 6)
        } yield square * y
    )

    // maps and sets
    val exampleMap = Map("a" -> 1, "b" -> 2, "c" -> 3)      // The constructor function for Map accepts an arbitrary number of Tuple2 arguments
                                                            // -> is actually a function that generates a Tuple2
    println(exampleMap)

    println(exampleMap("a"))
    println(exampleMap.get("c"))
    // println(exampleMap("z"))     // throws java.util.NoSuchElementException is the key is missed
    println(exampleMap.get("z"))    // returns Option type, so Some is there's the key, otherwise None

    println(exampleMap.getOrElse("z", -1))
    println(exampleMap.contains("a"))

    println(
        exampleMap + ("d" -> 4, "e" -> 5)   // add new pairs
    )
    println(
        exampleMap - ("a","b")              // remove by key
    )

    // mutable maps
    val example2 = scala.collection.mutable.Map("x" -> 10, "y" -> 11, "z" -> 12)
    example2 += ("x" -> 20)
    example2 -= ("y", "z")
    example2("x") = 30
    println(example2)

    // sorted maps
    val example3 = Map("a" -> 1) + ("b" -> 2) + ("c" -> 3) + ("d" -> 4) + ("e" -> 5)
    println(example3)

    val example3Ordered = scala.collection.immutable.ListMap("a" -> 1) + ("b" -> 2) + ("c" -> 3) + ("d" -> 4) + ("e" -> 5)  // preserves the order in which keys are added
    println(example3Ordered)

    // map and flatMap
    println(
        Map("a" -> 1, "b" -> 2, "c" -> 3).map(pair => pair._1 -> pair._2 * 2)   // we iterate on data type Tuple2
    )
    println(
        Map("a" -> 1, "b" -> 2, "c" -> 3).map(pair => pair._1 + "=" + pair._2)  // if return type doesn't match Tuple2, the result is still there but the final type is more general one, eg. List
    )
    println(
        exampleMap.flatMap {
            case (str, num) => (1 to 3).map(x => (str + x) -> (num * x))
        }
    )
    // the same using for syntax
    for {
        (str, num) <- exampleMap
        x <- 1 to 3
    } yield (str + x) -> (num * x)

    // sets (unordered collections that contain no duplicate elements)

    // exercises
    val people = Set(
        "Alice",
        "Bob",
        "Charlie",
        "Derek",
        "Edith",
        "Fred"
    )

    val ages = Map(
        "Alice" -> 20,
        "Bob" -> 30,
        "Charlie" -> 50,
        "Derek" -> 40,
        "Edith" -> 10,
        "Fred" -> 60
    )

    val favoriteColors = Map(
        "Bob" -> "green",
        "Derek" -> "magenta",
        "Fred" -> "yellow"
    )

    val favoriteLolcats = Map(
        "Alice" -> "Long Cat",
        "Charlie" -> "Ceiling Cat",
        "Edith" -> "Cloud Cat"
    )

    def favoriteColor(person: String) = favoriteColors.get(person).getOrElse("beige")

    def printColors = for {
        p <- people
        c = favoriteColor(p)
    } println(s"$p favorite color: $c")

    printColors

    def lookup[A, B](name: A, map: Map[A, B]): Option[B] = map.get(name)

    //  the color of the oldest person
    val oldest: Option[String] =
        people.foldLeft(Option.empty[String]) { (older, person) =>
            if(ages.getOrElse(person, 0) > older.flatMap(ages.get).getOrElse(0)) {
                Some(person)
            } else {
                older
            }
        }

    val favorite: Option[String] = for {
        oldest <- oldest
        color <- favoriteColors.get(oldest)
    } yield color

    println(s"favorite: $favorite")

    def unionOfSets[A](set1: Set[A], set2: Set[A]): Set[A] = set2.foldLeft(set1){ (result, elt) => result + elt }

    println(unionOfSets(Set(1,2,3), Set(4,5,6)))

    //def unMaps[A](map1: Map[A, Int], map2: Map[A, Int]) = map1.map { elm => elm._1 -> (elm._2 + map2.getOrElse(elm._1, 0)) }
    def unionOfMaps[A, B](map1: Map[A, B], map2: Map[A, B])(add: (B, B) => B) = map1.foldLeft(map2) { (map, elm) => {
            val (k, v) = elm
            val newVal = map.get(k).map(v2 => add(v, v2)).getOrElse(v)
            map + (k -> newVal)
        } 
    }

    println(
        unionOfMaps(Map('a' -> 1, 'b' -> 2), Map('a' -> 2, 'b' -> 4)) { (a, b) => a + b } 
    )
    println(
        unionOfMaps(Map('a' -> 1, 'b' -> 2, 'x' -> 20), Map('a' -> 2, 'b' -> 4, 'c' -> 10)) { (a, b) => a + b }
    )

    // Ranges
    println(
        1 until 10
    ) 
    println(
        10 until 0 by -1
    )
    println(
        (1 until 10) ++ (20 until 30)
    )

    // random words
    val subjects = List("Noel", "The cat", "The dog")
    val verbs = List("wrote", "chased", "slept on")
    val objects = List("the book", "the ball", "the bed")

    def verbsForSubject(subject: String) = 
        subject match {
            case "Noel" => List("wrote", "chased", "slept on")
            case "The cat" => List("meowed at", "chased", "slept on")
            case "The dog" => List("barked at", "chased", "slept on")
        }

    def objectsForVerb(verb: String) = 
        verb match {
            case "wrote" => List("the book", "the letter”", "the code")
            case "chased" => List("the ball", "the dog", "the cat")
            case "slept on" => List("the bad", "the mat", "the train")
            case "meowed at" => List("Noel", "the door", "the food cupboard")
            case "barked at" => List("the postman", "the car", "the cat")
        }

    // aka full random generation mode
    val possibleSentences = for {
        s <- subjects
        v <- verbsForSubject(s)
        o <- objectsForVerb(v)
    } yield (s, v, o)

    println(possibleSentences)

    // probabilities (see distribution.scala)
    println("a" -> 1)
    println(("a", 1))

    val fairCoin: Distribution[Coin] = Distribution.uniform(List(Heads, Tails))
    println(fairCoin)
    val threeFlips =
        for {
            c1 <- fairCoin
            c2 <- fairCoin
            c3 <- fairCoin
        } yield (c1, c2, c3)
    println(threeFlips)

    // Type Classes (implicits)
    // allow us to extend existing libraries with new functionality
    // A type class is like a trait, defining an interface

    // ordering
    import scala.math.Ordering

    val minOrdering = Ordering.fromLessThan[Int](_ < _)     // implementation of a type class are called type class instance
    val maxOrdering = Ordering.fromLessThan[Int](_ > _)

    println(
        List(3, 4, 2).sorted(minOrdering)
    )
    println(
        List(3, 4, 2).sorted(maxOrdering)
    )

    // The type class pattern separates the implementation of functionality (the type class instance, an Ordering[A] in our example) from the type the functionality is provided for (the A in an Ordering[A]).
    
    // implicit values 
    {
        implicit val ordering = Ordering.fromLessThan[Int](_ < _)
        println(
            List(3, 4, 2).sorted        // NB: we didn’t supply an ordering to sorted. Instead, the compiler provides it for us.
        )
    }
    // we can get the compiler to supply implicit values to parameters that are themselves marked implicit
    // an implicit value must be declared within a surrounding object, class, or trait

    val absOrdering = Ordering.fromLessThan[Int](math.abs(_) < math.abs(_))
    assert(List(-4, -1, 0, 2, 3).sorted(absOrdering) == List(0, -1, 2, 3, -4))
    assert(List(-4, -3, -2, -1).sorted(absOrdering) == List(-1, -2, -3, -4))

    implicit val absOrderingImpl = Ordering.fromLessThan[Int](math.abs(_) < math.abs(_))
    assert(List(-4, -1, 0, 2, 3).sorted == List(0, -1, 2, 3, -4))
    assert(List(-4, -3, -2, -1).sorted == List(-1, -2, -3, -4))

    // implicit val ratOrdering = Ordering.fromLessThan[Rational]((a, b) => (a.numerator.toDouble/a.denominator.toDouble) < (b.numerator.toDouble/b.denominator.toDouble))     // implicit with higher priority
    import RationalLessThanOrdering._
    // import RationalGreaterThanOrdering._
    assert(List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted == List(Rational(1, 3), Rational(1, 2), Rational(3, 4)))

    // organizing type class instances (implicit scope)
    // When defining a type class instance, if there is a single instance for the type; AND you can edit the code for the type that you are defining the instance for -> then define the type class instance in the companion object of the type

    // implicit priority
    // NB: An ambiguity error is only raised if there are multiple type class instances with the same priority
    // local scope takes precedence over instances found in companion objects

    // If there is no good default instance for a type class instance, or if there are several good defaults, we should 
    // not place an type class instances in the companion object but instead require the user to explicitly import an
    // instance into the local scope -> one simple way to package instances is to place each in its own object that the user can import into the local scope

    val orders = List(Order(2, 10), Order(4, 2), Order(1, 100))

    {
        //import OrderByUnitPrice._
        //import OrderByUnits._
        println(orders.sorted)
    }

    // creating type classes
    // Type Class is the trait with at least one type variable
    // The type variables specify the concrete types the type class instances are defined for. 
    {
        import AgeImplicit._
        println(
            PersonCompareByAge.equal(Person("Mario","Rossi", 20), Person("Marco","Polo",20))
        )
    }

    // implicit parameter list
    implicit val personWriter = PersonWriter
    println(
        HtmlUtl.htmlify(Person("Mario","Rossi",20))
    )

    // interfaces using implicit parameters
    HtmlWriter[Person].write(Person("Mario","Rossi",20))    // The idea is to simply select a type class instance by type (done by the no-argument apply method) and then directly call the methods defined on that instance.
                                                            // type class instance in this case is personWriter
    
    import NameImplicit._
    println(
        Eq(Person("Mario","Rossi",20), Person("Mario","Rossi",20))
    )
    // using interface with type parameter
    println(
        Equal[Person].equal(Person("Mario","Rossi",20), Person("Mario","Rossi",20))
    )
    
    // enriched interfaces (implicit classes, type enrichment, aka extension methods in C#)
    println(new StringExtensions.ExtraStringMethods("the quick brown fox").numberOfVowels)   
    // Implicit classes follow the same scoping rules as implicit values
    import StringExtensions._
    // NB: only a single implicit class will be used to resolve a type error. The compiler will not look to construct a chain of implicit classes to access the desired method.
    println("the quick brown fox".numberOfVowels)

    import HtmlUtl._    // needed to use toHtml (PersonWriter is 'injected' into the scope before)
    println(Person("Mario","Rossi",20).toHtml)

    // The Scala compiler uses implicit classes to fix type errors in our code.

    {
        import IntImplicits._
        // new IntOps(2).yeah
        2.yeah
        2.times(i => println(s"Look - it's the number $i!"))
    }

    import StringImplicit._     // inject type class instance (implicit variable) ->
    
    // -> OR implement in place the Equal[String] and assign it to implicit val
    /*implicit val compare = new Equal[String] {
        def equal(t1: String, t2: String): Boolean = t1.equalsIgnoreCase(t2)
    }*/
    
    import Equal._              // inject implicit class (type enrichment)
    println("abcd" === "ABCD")

    // using type classes
    // context bounds - is an annotation on a generic type variable like so [A : Context] and it expands into a generic type parameter [A] along with an implicit parameter for a Context[A].

    def pageTemplate[A](body: A)(implicit writer: HtmlWriter[A]): String = {
        val renderedBody = body.toHtml
        s"<html><head>...</head><body>${renderedBody}</body></html>"
    }

    def pageTemplateUsingContextBounds[A : HtmlWriter](body: A): String = {
        val renderedBody = body.toHtml
        s"<html><head>...</head><body>${renderedBody}</body></html>"
    }

    // Context bounds give us a short-hand syntax for declaring implicit parameters -  but since we don’t have an explicit name for the parameter we cannot use it in our methods (used only to inject the type into the scope!)
    
    // Implicitly
    implicit val implicitVal = Example("World")
    println(implicitly[Example])    // the way to access implicit value/parameter without using the name - can be used in method that uses Context Bounds to reference explicitely the implicit parameter
    // implicitly returns the implicit matching the given type, assuming there is no ambiguity.

    // implicit conversions (NOT TO USE if it's possible)
    // Implicit conversions are a more general form of implicit classes
    import ImplicitConversions._
    println(
        new A().bar
    )

    {
        import IntImplicitConversions._
        2.yeah
        2.times(i => println(s"Look - it's the number $i!"))
    }

    // JSON serialization
    {
        import JsonConversion._
        val json = JsObject(Map("foo" -> JsString("a"), "bar" -> JsString("b"), "baz" -> JsString("c")))
        println(json.stringify)

        import VisitorToJsonConversions._
        import JsExtensions._
        val visitors: Seq[Visitor] = Seq(Anonymous("001", new Date), User("003", "dave@xample.com", new Date))
        for {
            v <- visitors 
        } println(v.toJson.stringify)
    }
}

case class Example(name: String)

object Example {
    def example = List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted
}

final case class Box[A](value: A) 

object MathStuff {
    def add1(num: Int) = num + 1
}

object Stormtrooper {
    def inspect(p: Person) = p match {
        case Person("Mario", "Rossi", _) => "Stop, rebel scum"
        case Person("Marco", "Polo", _) => "Stop, rebel scum"
        case Person(first, last, _) => s"Go, $first"
    }
}
