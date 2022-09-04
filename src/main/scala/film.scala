case class FilmV1(name: String, year: Int, director: DirectorV1) {}

case class DirectorV1(first: String, last: String) {}

object FilmV1 {
    def apply(name: String) = new FilmV1(name, 2022, DirectorV1("Mario","Rossi"))
}

object Dad {
    def rate(f: FilmV1) = f match {
        case FilmV1(_, _, DirectorV1("Clint", "Eastwood")) => 10.0
        case FilmV1(_, _, DirectorV1("John", "McTiernan")) => 7.0
        case _ => 3.0
    }
}
