package catpiler.frontend.scanner.keywords;

public class CALL extends Keyword {

	public final static java.lang.String tokenId = "CALL";
	
	private java.lang.String attribute;
	
	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public java.lang.String getTokenID() {
		return CALL.tokenId;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}

}
