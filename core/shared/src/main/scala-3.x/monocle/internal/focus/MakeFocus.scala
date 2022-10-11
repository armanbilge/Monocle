package monocle.focus.i1306

import monocle.Iso
import monocle.Focus._
import monocle.internal.focus._

// transparent inline def mkFocus[From, To](inline lambda: (KeywordContext ?=> From => To)): Any =
//       ${ FocusImpl('lambda) }

class MakeFocus[From] {
def apply(): Iso[From, From] = Iso.id

transparent inline def apply[To](inline lambda: (KeywordContext ?=> From => To)): Any =
    ${ FocusImpl('lambda) }
}
