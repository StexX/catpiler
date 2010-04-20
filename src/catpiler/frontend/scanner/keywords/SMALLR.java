package catpiler.frontend.scanner.keywords;

public class SMALLR extends Keyword {

	public final static java.lang.String tokenId = "SMALLR";
	
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
		return SMALLR.tokenId;
	}
}
