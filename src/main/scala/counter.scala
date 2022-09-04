case class Counter(val count: Int) {
    def inc(by: Int = 1) = new Counter(count + by)
    def dec(by: Int = 1) = new Counter(count - by)
    def adjust(f: Int => Int) = new Counter(f(count))
}

class Adder(amount: Int) {
    def apply(in: Int) = in + amount
}