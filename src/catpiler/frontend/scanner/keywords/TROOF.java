package catpiler.frontend.scanner.keywords;

public class TROOF extends Keyword {

	public final static java.lang.String tokenId = "TROOF";
	private java.lang.String attribute;

	public TROOF() {
		numericId = 0x5; // 0101
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
