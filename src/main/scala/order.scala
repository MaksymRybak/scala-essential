final case class Order(units: Int, unitPrice: Double) {
    val totalPrice: Double = units * unitPrice
}

object Order {
    implicit val ordering = Ordering.fromLessThan[Order](_.totalPrice < _.totalPrice)
}

object OrderByUnits {
    implicit val ordering = Ordering.fromLessThan[Order](_.units < _.units)
}

object OrderByUnitPrice {
    implicit val ordering = Ordering.fromLessThan[Order](_.unitPrice < _.unitPrice)
}