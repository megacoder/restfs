package nofs.restfs.query.ast

class ApplyTransformationOperation(fromTyp: String, toTyp: String) extends FSOperation {
  private var _fromType = fromTyp;
  private var _toType = toTyp;

  def fromType() = _fromType;
  def toType() = _toType;
}