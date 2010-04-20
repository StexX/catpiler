package catpiler.frontend.scanner.keywords;

public class VAR extends Keyword {

	public final static java.lang.String tokenId = "VAR";
	
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
		return VAR.tokenId;
	}
}
