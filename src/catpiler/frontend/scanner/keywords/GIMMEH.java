package catpiler.frontend.scanner.keywords;

public class GIMMEH extends Keyword {

	public final static java.lang.String tokenId = "GIMMEH";
	
	private java.lang.String attribute;
	
	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public java.lang.String getTokenID() {
		return GIMMEH.tokenId;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}

}
