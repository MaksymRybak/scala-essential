import scala.annotation.tailrec

sealed trait Tree[A] {
    def fold[B](node: (B, B) => B, leaf: A => B): B
 
    // @tailrec
    // def sum: Int = this match {
    //     case Leaf(el) => el 
    //     case Node(left, right) => left.sum + right.sum
    // }
    // // @tailrec
    // def double: Tree = this match {
    //     case Leaf(el) => Leaf(el * 2)
    //     case Node(left, right) => Node(left.double, right.double)
    // }
}

final case class Leaf[A](value: A) extends Tree[A] {
    def fold[B](node: (B, B) => B, leaf: A => B): B = leaf(value)
}

final case class Node[A](left: Tree[A], right: Tree[A]) extends Tree[A] {
    def fold[B](node: (B, B) => B, leaf: A => B): B = node(left.fold(node, leaf), right.fold(node, leaf))
}