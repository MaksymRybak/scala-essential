// sum type (OR)
sealed trait WaterSource 

final case object Well extends WaterSource 
final case object Spring extends WaterSource 
final case object Tap extends WaterSource 

// product type (AND)
case class BottleOfWater(size: Int, source: WaterSource, carbonated: Boolean) {}