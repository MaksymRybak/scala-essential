class B {
    def bar = "This is the best method ever!"
}

class A {}

// with an implicit method we can convert from any type to any other type as long as an implicit is available in scope
object ImplicitConversions {
    // We can tag any single-argument method with the implicit keyword to allow the compiler to implicitly use the method to perform automated conversions from one type to another:
    implicit def aTob(in: A): B = new B()
}

// NB (p.161):
// - wherever possible, stick to the type enrichment and type class programming patterns
// - wherever possible, use implicit classes, values, and parameter lists over implicit conversions
// - package implicits clearly, and bring them into scope only where you need them
// - avoid creating implicit conversions that convert from one general type to another general type — the more specific your types are, the less likely the implicit is to be applied incorrectly


