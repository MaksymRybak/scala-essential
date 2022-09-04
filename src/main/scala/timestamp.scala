class Timestamp(val seconds: Int) {

}

object Timestamp {
    def apply(hours: Int, minutes: Int) = new Timestamp(hours*60*60 + minutes*60)
    def apply(hours: Int, minutes: Int, seconds: Int) = new Timestamp(hours*60*60 + minutes*60 + seconds)
}