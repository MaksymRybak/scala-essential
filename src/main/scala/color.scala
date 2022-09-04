sealed trait Color {
  def red: Int
  def green: Int
  def blue: Int

  def isLight = red < 125 && green < 125 && blue < 125
  def isDark = !isLight
}

final case object Red extends Color {
    val red = 255
    val green = 0
    val blue = 0
}

final case object Yellow extends Color {
    val red = 255
    val green = 255
    val blue = 0
}

final case object Pink extends Color {
    val red = 255
    val green = 192
    val blue = 203
}

final case class CustomColor(red: Int, green: Int, blue: Int) extends Color

object NewColorFor {
    def apply(r: Int, g: Int, b: Int) = new CustomColor(r, g, b)
}