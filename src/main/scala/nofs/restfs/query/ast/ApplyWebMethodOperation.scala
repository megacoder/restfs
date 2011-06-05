package nofs.restfs.query.ast

class ApplyWebMethodOperation(meth : String, urlVal : String, tokenPath : String) extends FSOperation {
  private val _method = meth;
  private val _url = urlVal;
  private val _token = tokenPath;

  def getMethod() = _method
  def getUrl() = _url
  def getToken() = _token
}