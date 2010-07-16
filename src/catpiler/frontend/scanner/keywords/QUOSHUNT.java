package catpiler.frontend.scanner.keywords;

public class QUOSHUNT extends Keyword {

	public final static java.lang.String tokenId = "QUOSHUNT";
	private java.lang.String attribute;

	public QUOSHUNT() {
		numericId = 0x24; // 0010 0100
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
