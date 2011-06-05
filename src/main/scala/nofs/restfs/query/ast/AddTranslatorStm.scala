package nofs.restfs.query.ast

class AddTranslatorStm(fromFormat: String, toFormat: String, withFileName: String) extends ProgramStm {
  private var _from = fromFormat
  private var _to = toFormat
  private var _with = withFileName

  def getFrom() = _from

  def getTo() = _to

  def getWithFile() = _with;
}