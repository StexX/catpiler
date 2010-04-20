package catpiler.frontend.scanner.keywords;

public class IS extends Keyword {

	public final static java.lang.String tokenId = "IS";
	
	private java.lang.String attribute;
	
	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public java.lang.String getTokenID() {
		return IS.tokenId;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}

}
