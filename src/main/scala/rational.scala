final case class Rational(numerator: Int, denominator: Int)

object Rational {
    implicit val ordering = Ordering.fromLessThan[Rational]((a, b) => (a.numerator.toDouble/a.denominator.toDouble) < (b.numerator.toDouble/b.denominator.toDouble))    // implicit with lower priority than local scope
                                                                                                                                                                        // aka default behaviour
                                                                                                                                                                        // can be overwritten in local scope 
}

object RationalLessThanOrdering {
    implicit val ordering = Ordering.fromLessThan[Rational]((x, y) =>
        (x.numerator.toDouble / x.denominator.toDouble) <
        (y.numerator.toDouble / y.denominator.toDouble)
    )
}

object RationalGreaterThanOrdering {
    implicit val ordering = Ordering.fromLessThan[Rational]((x, y) =>
        (x.numerator.toDouble / x.denominator.toDouble) >
        (y.numerator.toDouble / y.denominator.toDouble)
    )
}
