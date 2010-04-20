package catpiler.frontend.scanner.keywords;

public class AN extends Keyword {

	public final static java.lang.String tokenId = "AN";
	
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
		return AN.tokenId;
	}
}
