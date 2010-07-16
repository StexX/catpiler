package catpiler.frontend.scanner.keywords;

public class CHARZ extends Keyword {

	public final static java.lang.String tokenId = "CHARZ";
	private java.lang.String attribute;

	public CHARZ() {
		numericId = 0x6; // 0110
	}

	
	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}
}
