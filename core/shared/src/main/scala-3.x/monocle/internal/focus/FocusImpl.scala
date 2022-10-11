package monocle.internal.focus

import monocle.{Focus, Lens}
import scala.quoted.{quotes, Expr, Quotes, Type}
import monocle.internal.focus.features.{AllFeatureGenerators, AllFeatureParsers, GeneratorLoop, ParserLoop}

private[focus] class FocusImpl(val macroContext: Quotes)
    extends FocusBase
    with ErrorHandling
    with LambdaConfigParser
    with ParserLoop
    with AllFeatureParsers
    with GeneratorLoop
    with AllFeatureGenerators {

  import macroContext.reflect._

  def run[From: Type, To: Type](lambda: Expr[Focus.KeywordContext ?=> From => To]): Expr[Any] = {
    val generatedCode =
      for {
        config       <- parseLambdaConfig[From](lambda.asTerm)
        focusActions <- parseFocusActions(config)
        code         <- generateCode[From](focusActions)
      } yield code

    generatedCode match {
      case Right(code) => code.asExpr
      case Left(error) => report.error(errorMessage(error)); '{ ??? }
    }
  }
}

private[monocle] object FocusImpl {
  def apply[From: Type, To: Type](lambda: Expr[Focus.KeywordContext ?=> From => To])(using q: Quotes): Expr[Any] =
    new FocusImpl(q).run(lambda)
}
