package catpiler.frontend.scanner.keywords;

public class CLOSE extends Keyword {

	public final static java.lang.String tokenId = "CLOSE";
	
	private java.lang.String attribute;
	
	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public java.lang.String getTokenID() {
		return CLOSE.tokenId;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}

}
