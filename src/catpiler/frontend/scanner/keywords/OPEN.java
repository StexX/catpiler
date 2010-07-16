package catpiler.frontend.scanner.keywords;

public class OPEN extends Keyword {

public final static java.lang.String tokenId = "OPEN";
	
	private java.lang.String attribute;
	
	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public java.lang.String getTokenID() {
		return OPEN.tokenId;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}
	
}
