package catpiler.frontend.scanner.keywords;

public class WRITE extends Keyword {

public final static java.lang.String tokenId = "WRITE";
	
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
