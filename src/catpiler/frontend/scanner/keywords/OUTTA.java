package catpiler.frontend.scanner.keywords;

public class OUTTA extends Keyword {

	public final static java.lang.String tokenId = "OUTTA";
	
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
		return OUTTA.tokenId;
	}
}
