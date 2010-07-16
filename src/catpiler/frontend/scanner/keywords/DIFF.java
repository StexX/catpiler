package catpiler.frontend.scanner.keywords;

public class DIFF extends Keyword {

	public final static java.lang.String tokenId = "DIFF";
	private java.lang.String attribute;
	
	public DIFF() {
		numericId = 0xC; // 0000 1100
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
