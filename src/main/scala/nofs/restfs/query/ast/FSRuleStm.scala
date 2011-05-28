package nofs.restfs.query.ast

class FSRuleStm(act : FSAction, ops : List[FSOperation]) extends ProgramStm {
  private val _action = act
  private val _operations = ops

  def action() = _action
  def operations() = _operations
}