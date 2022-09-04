final case class Distribution[A](events: List[(A, Double)]) {
    def map[B](f: A => B): Distribution[B]  = 
        Distribution(events map { case (a, p) => f(a) -> p } )  // NB: how we can use case to distructure the tuple

    def flatMap[B](f: A => Distribution[B]): Distribution[B]  = {
        val res = for {
            event <- events
            (elm1, prob1) = event
            newEvent <- f(elm1).events
            (elm2, prob2) = newEvent
        } yield elm2 -> prob1 * prob2

        Distribution(res).compact.normalize
    }

    def normalize: Distribution[A] = {
        val totalWeight = (events map { case (a, p) => p }).sum

        Distribution(events map { case (a,p) => a -> (p / totalWeight) })   // for each event we divided the relative probability by the total probabilities
    }

    def compact: Distribution[A] = {
        val distinct = (events map { case (a, p) => a }).distinct

        def prob(a: A): Double =
            (events filter { case (x, p) => x == a } map { case (a, p) => p }).sum

        Distribution(distinct map { a => a -> prob(a) })    // distinct events, for every event we summed the probability
    }
    
}

object Distribution {

    def uniform[A](data: List[A]): Distribution[A] = {
        val probability = 1.0 / data.length
        val dist = data.map(d => (d, probability))
        Distribution(dist)
    }

    def discrete[A](events: List[(A,Double)]): Distribution[A] =
        Distribution(events).compact.normalize

}

sealed trait Coin
final case object Heads extends Coin
final case object Tails extends Coin
