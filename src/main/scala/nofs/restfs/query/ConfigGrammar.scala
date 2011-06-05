package nofs.restfs.query

import ast._
import util.parsing.combinator.syntactical._
import java.lang.Exception
import io.Source

class ConfigGrammar extends StandardTokenParsers() {

  private val ADD = "ADD"
  private val XFORM = "XFORM"
  private val FROM = "FROM"
  private val TO = "TO";
  private val WITH = "WITH"
  private val NL = "\n"
  private val CLOSED = "CLOSED"
  private val OPENING = "OPENING"
  private val UPDATED = "UPDATED"
  private val MKNOD = "MKNOD"
  private val LPAREN = "("
  private val RPAREN = ")"
  private val GET = "GET"
  private val POST = "POST"
  private val PUT = "PUT"
  private val DELETE = "DELETE"
  private val USING = "USING"
  private val OAUTH = "OAUTH"
  private val TOKEN = "TOKEN"

  lexical.reserved += (
    ADD, XFORM, FROM, TO, WITH, NL, CLOSED, UPDATED, MKNOD, RPAREN,
    OPENING, LPAREN, GET, PUT, POST, DELETE, USING, OAUTH, TOKEN)
  lexical.delimiters += (LPAREN, RPAREN)

  def Parse_JavaList(text:String) : java.util.List[ProgramStm] = {
    val stmList = new java.util.ArrayList[ProgramStm];
    val statements = Parse(text)
    statements.foreach(stm => stmList.add(stm));
    return stmList
  }

  def Parse(text:String) : List[ProgramStm] = {
    val updatedText = Source.fromString(text).getLines.reduceLeft[String](_ + NL + _)
    val tokens = new lexical.Scanner(updatedText)
    val statements = phrase(program)(tokens).get
    if(statements.size == 0 && !lexical.lastNoSuccess.successful) {
      throw new Exception(lexical.lastNoSuccess.toString)
    }
    return statements
  }

  def program : Parser[List[ProgramStm]] = (
    (statement+) ^^ { case x=>x }
  )

  def statement : Parser[ProgramStm] = (
    (AddTranslator|fsRule) ^^ { case stm => stm }
  )

  //add xform from 'json' to 'xml' with /foo/bar
  def AddTranslator : Parser[AddTranslatorStm] = (
    ADD ~ XFORM ~ FROM ~ stringLit ~ TO ~ stringLit ~ WITH ~ stringLit ^^ {
    case _ ~ _ ~ _ ~ fromFormat ~ _ ~ toFormat ~ _ ~ withFile => new AddTranslatorStm(fromFormat, toFormat, withFile)
  })

  def fsRule : Parser[FSRuleStm] = (
    (fsAction ~ operations) ^^ {
      case act ~ ops => new FSRuleStm(act, ops)
    }
  )

  def operations : Parser[java.util.List[FSOperation]] = (
    ( operation+ ) ^^ { case ops => toOperationsList(ops) }
  )

  def toOperationsList(operations : List[FSOperation]) : java.util.List[FSOperation] = {
    val operationList = new java.util.ArrayList[FSOperation];
    operations.foreach(stm => operationList.add(stm));
    return operationList;
  }

  def operation : Parser[FSOperation] = (
    (XFORM ~ FROM ~ stringLit ~ TO ~ stringLit) ^^ {
    case _ ~ _ ~ fromFmt ~ _ ~ toFmt => new ApplyTransformationOperation(fromFmt, toFmt)
    } |
    (HttpMethod ~ stringLit ~ USING ~ OAUTH ~ TOKEN ~ stringLit) ^^ {
      case method ~ url ~ _ ~ _ ~ _ ~ tokenPath => new ApplyWebMethodOperation(method, url, tokenPath)
    } |
    (HttpMethod ~ stringLit) ^^ {
      case method ~ url => new ApplyWebMethodOperation(method, url, "")
    }
  )

  def HttpMethod : Parser[String] = (
    GetMethod|DeleteMethod|PostMethod|PutMethod ^^ { case x => x }
  )

  def GetMethod : Parser[String] = (
    (GET ~ FROM) ^^ { case _ ~ _ => "get" }
  )

  def DeleteMethod : Parser[String] = (
    (DELETE ~ FROM) ^^ { case _ ~ _ => "delete" }
  )

  def PostMethod : Parser[String] = (
    (POST ~ TO) ^^ { case _ ~ _ => "post" }
  )

  def PutMethod : Parser[String] = (
    (PUT ~ TO) ^^ { case _ ~ _ => "put" }
  )

  def fsAction : Parser[FSAction] = (
    (OPENING|CLOSED|MKNOD|UPDATED) ~ LPAREN ~ stringLit ~ RPAREN ^^ {
      case action ~ _ ~ fileName ~ _ => new FSAction(action, fileName)
    }
  )
}
