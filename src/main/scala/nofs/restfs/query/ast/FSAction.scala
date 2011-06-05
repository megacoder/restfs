package nofs.restfs.query.ast

class FSAction(act: String, file: String) {
  private val _action = act
  private val _fileName = file

  def getAction() = _action

  def getFileName() = _fileName
}