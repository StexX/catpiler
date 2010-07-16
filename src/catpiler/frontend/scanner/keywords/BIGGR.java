package catpiler.frontend.scanner.keywords;

public class BIGGR extends Keyword {

	public final static java.lang.String tokenId = "BIGGR";

	public BIGGR() {
		numericId = 0x1C; // 0001 1100
	}
	
	private java.lang.String attribute;

	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}
	
}
