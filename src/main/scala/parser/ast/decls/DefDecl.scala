package parser.ast.decls

import lexer._
import parser.InvalidDef
import parser.ast._
import parser.ast.expressions._

class DefDecl(id: String) extends LangNode

object DefDecl {

  def parse(inner: Boolean, tokens: TokenStream): DefDecl = {
    tokens.consume(DEF)
    val defId = tokens.consume(classOf[ID]).value
    val (args, dispatchers) = DefArg.parseDefArgs(tokens)
    tokens.nextToken() match {
      case ASSIGN =>
        tokens.consume(ASSIGN)
        buildResult(inner, defId, args, dispatchers, parsePipedOrBodyExpression(tokens), WhereBlock.parse(tokens))

      case NL =>
        tokens.consume(NL)
        val body = DefBodyGuardExpr.parse(tokens)
        body match {
          case BodyGuardsExpresionAndWhere(guards, whereBlock) =>
            buildResult(inner, defId, args, dispatchers, BodyGuardsExpresion(guards), Some(whereBlock))
          case _ =>
            buildResult(inner, defId, args, dispatchers, body, WhereBlock.parse(tokens))
        }

      case _ => throw InvalidDef()
    }
  }

  private[this]
  def buildResult(inn: Boolean, id: String, args: List[DefArg], disps: List[DefArg],
             body: Expression, where: Option[WhereBlock]): DefDecl = {
    disps match {
      case Nil => SimpleDefDecl(inn, id, args, body, where)
      case dispatches => MultiMethod(inn, id, dispatches, args, body, where)
    }
  }


}
