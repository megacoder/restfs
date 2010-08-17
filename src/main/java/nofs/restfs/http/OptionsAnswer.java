package nofs.restfs.http;

public class OptionsAnswer extends HttpAnswer {

	public OptionsAnswer(int code, Iterable<HttpMethods> methods) {
		super(code);
		_methods = methods;
	}
	
	private final Iterable<HttpMethods> _methods;
	public Iterable<HttpMethods> getMethods() {
		return _methods;
	}

}
