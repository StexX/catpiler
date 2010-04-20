package catpiler.frontend.scanner.keywords;

public class CAN extends Keyword {

	public final static java.lang.String tokenId = "CAN";
	
	private java.lang.String attribute;
	
	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public java.lang.String getTokenID() {
		return CAN.tokenId;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}

}
