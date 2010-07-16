package catpiler.frontend.scanner.keywords;

public class SUM extends Keyword {

	public final static java.lang.String tokenId = "SUM";
	private java.lang.String attribute;

	public SUM() {
		numericId = 0x4; // 0000 0100
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
