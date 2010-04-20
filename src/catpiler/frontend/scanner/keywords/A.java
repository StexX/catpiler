package catpiler.frontend.scanner.keywords;

public class A extends Keyword {

	public static final java.lang.String tokenId = "A";
	
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
		return A.tokenId;
	}

}
