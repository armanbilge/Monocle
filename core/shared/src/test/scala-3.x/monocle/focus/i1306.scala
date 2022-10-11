package monocle.focus.i1306

trait Qux

object Foo {

  opaque type Bar = Qux

  case class Baz(id: String)
  object Baz {
    implicit val bazId: monocle.Lens[Foo.Baz, String] = new monocle.Focus.MkFocus[Foo.Baz].apply(_.id)
  }

}
