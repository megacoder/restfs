package nofs.restfs.query.ast

class FSRuleStm(act : FSAction, ops : java.util.List[FSOperation]) extends ProgramStm {
  private val _action = act
  private val _operations = ops

  def getAction() = _action
  def getOperations() = _operations
}