package catpiler.frontend.scanner.keywords;

public class String extends Keyword {

	public final static java.lang.String tokenId = "string";
	
	private java.lang.String attribute;

	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}
	
	@Override
	public java.lang.String getTokenID() {
		return String.tokenId;
	}
}
