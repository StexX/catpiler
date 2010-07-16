package catpiler.frontend.scanner.keywords;

public class NUMBRZ extends Keyword {

	public final static java.lang.String tokenId = "NUMBRZ";
	private java.lang.String attribute;

	public NUMBRZ() {
		numericId = 0x2; // 0000 1100
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
