package codegen.clojure.expressions

import codegen.{CodeGenerator, Translator}
import codegen.clojure.decls.DeclGen._
import codegen.clojure.expressions.ExpressionsGen._
import parser.ast.decls.BodyGuardsExpresion
import parser.ast.expressions.vars._

object DeclsExprGen {

  implicit object LetIdTranslator extends Translator[LetId] {

    override def mkString(node: LetId): String = {
      node match {
        case LetSimpleId(id) => id
        case LetTupledId(ids) => s"[${ids.map(mkString).mkString(" ")}]"
      }
    }

  }

  implicit object LetDeclExpressionTranslator extends Translator[LetDeclExpression] {

    override def mkString(node: LetDeclExpression): String = {
      node match {
        case LetDeclExpression(decls, Some(expression)) =>
          "(let[" +
            decls.map { d =>
              s"${CodeGenerator.buildString(d.id)} ${CodeGenerator.buildString(d.value)}"
            }.mkString(" ") + "]\n\t" + CodeGenerator.buildString(expression) + ")"
        case LetDeclExpression(decls, None) =>
          decls.map {
            decl => s"(def ${CodeGenerator.buildString(decl.id)} ${CodeGenerator.buildString(decl.value)})"
          }.mkString("\n")
      }
    }
  }

  implicit object VarDeclExpressionTranslator extends Translator[VarDeclExpression] {

    override def mkString(node: VarDeclExpression): String = {
      val strBuf = new StringBuilder()
      node match {
        case VarDeclExpression(decls, None) =>
          for (d <- decls) {
            strBuf ++= toClojureOguVariable(d)
          }

        case VarDeclExpression(decls, Some(expression)) =>
          strBuf ++= "(with-local-vars ["
          strBuf ++= decls.asInstanceOf[List[LetVariable]].map(d => s"${CodeGenerator.buildString(d.id)} ${CodeGenerator.buildString(d.value)}").mkString(" ")
          strBuf ++= "]\n"
          addVariables(decls)
          strBuf ++= s"${CodeGenerator.buildString(expression)})\n"
          removeVariables(decls)
      }
      strBuf.mkString
    }


    def toClojureOguVariable(variable: LetVariable) : String = {
        s"(-def-ogu-var- ${variable.id} ${CodeGenerator.buildString(variable.value)})\n"
    }

    var varDecls : Set[String] = Set.empty[String]

    def isVariable(id: String): Boolean = {
      varDecls.contains(id)
    }

    def addVariables(decls: List[LetVariable]): Unit = {
      for (v <- decls) {
        v match {
          case LetVariable(LetSimpleId(id), _) => varDecls = varDecls + id
        }
      }
    }

    def removeVariables(decls: List[LetVariable]): Unit = {
      for (v <- decls) {
        v match {
          case LetVariable(LetSimpleId(id), _) => varDecls = varDecls - id
        }
      }
    }

  }

  implicit object BodyGuardsExpresionTranslator extends Translator[BodyGuardsExpresion] {

    override def mkString(node: BodyGuardsExpresion): String = {
          s"(cond\n ${node.guards.map(g => CodeGenerator.buildString(g)).mkString("\n")})"
    }

  }
}

