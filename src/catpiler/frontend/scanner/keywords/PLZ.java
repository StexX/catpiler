package catpiler.frontend.scanner.keywords;

public class PLZ extends Keyword {

public final static java.lang.String tokenId = "PLZ";
	
	private java.lang.String attribute;
	
	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public java.lang.String getTokenID() {
		return PLZ.tokenId;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}

}
