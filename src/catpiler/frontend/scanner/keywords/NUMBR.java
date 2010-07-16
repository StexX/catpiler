package catpiler.frontend.scanner.keywords;

public class NUMBR extends Keyword {

	public final static java.lang.String tokenId = "NUMBR";
	private java.lang.String attribute;

	public NUMBR() {
		numericId = 0x1; // 0001
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
