package catpiler.frontend.scanner.keywords;

public class U extends Keyword {

	public final static java.lang.String tokenId = "U";
	
	private java.lang.String attribute;
	
	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public java.lang.String getTokenID() {
		return U.tokenId;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}

}
