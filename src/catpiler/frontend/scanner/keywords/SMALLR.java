package catpiler.frontend.scanner.keywords;

public class SMALLR extends Keyword {

	public final static java.lang.String tokenId = "SMALLR";
	
	public SMALLR() {
		numericId = 0x2C; // 0010 1100
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
