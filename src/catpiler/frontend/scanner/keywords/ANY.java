package catpiler.frontend.scanner.keywords;

public class ANY extends Keyword {

	public final static java.lang.String tokenId = "ANY";
	
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
		return ANY.tokenId;
	}
}
