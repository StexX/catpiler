package catpiler.frontend.scanner.keywords;

public class ArrayAccessOp extends Keyword {

	// unused
	private java.lang.String attribute;
	
	public final static java.lang.String tokenId = ".";
	
	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public java.lang.String getTokenID() {
		return ArrayAccessOp.tokenId;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}
}
