
sealed trait Shape {
    def sides: Int
    def perimeter: Double
    def area: Double
    def color: Color
}

trait Rectangular extends Shape {
    def width: Int
    def height: Int  

    def sides = 4
    def perimeter = 2 * width + 2 * height
    def area = width * height
}

case class Circle(radius: Int, color: Color) extends Shape {
    def sides = 1
    def perimeter = 2 * math.Pi * radius
    def area = math.Pi * radius * radius
}

case class Rectangle(width: Int, height: Int, color: Color) extends Rectangular {}

case class Square(s: Int, color: Color) extends Rectangular {
    val width = s
    val height = s
}

object Draw {
    def apply(s: Shape): String = s match {
        case Circle(radius, color) => s"A ${Draw(color)} circle of radius ${radius}cm"
        case Rectangle(width, height, color) => s"A ${Draw(color)} rectangle of width ${width}cm and height ${height}cm"
        case Square(s, color) => s"A ${Draw(color)} square of a side ${s}cm"
        case _ => "Unknown shape"
    }

    def apply(c: Color): String = c match {
        case Red => "red"
        case Yellow => "yellow"
        case Pink => "pink"
        case color => if (color.isDark) "dark" else "light"
    }
}