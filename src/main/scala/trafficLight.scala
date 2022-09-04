// sum type (OR)
sealed trait TrafficLight {
    def next: TrafficLight

    def nextV2: TrafficLight = this match {
        case RedLight => GreenLight
        case GreenLight => YellowLight
        case YellowLight => RedLight
    }
}

final case object RedLight extends TrafficLight {

    def next: TrafficLight = GreenLight

}

final case object GreenLight extends TrafficLight {

    def next: TrafficLight = YellowLight

}

final case object YellowLight extends TrafficLight {

    def next: TrafficLight = RedLight

}

object TrafficControl {

    def nextColor(t: TrafficLight) = t match {
        case RedLight => GreenLight
        case GreenLight => YellowLight
        case YellowLight => RedLight
    }

}

