package catpiler.frontend.scanner.keywords;

public class OF extends Keyword {

	public final static java.lang.String tokenId = "OF";
	
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
		return OF.tokenId;
	}
}
