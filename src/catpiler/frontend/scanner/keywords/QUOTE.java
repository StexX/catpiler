package catpiler.frontend.scanner.keywords;

/**
 * @deprecated Quote is not really a token, but part of a string
 * @author Stephanie Stroka
 * 			(stephanie.stroka@sbg.ac.at)
 *
 */
@Deprecated
public class QUOTE extends Keyword {

	public final static java.lang.String tokenId = ":\"";
	
	private java.lang.String attribute;

	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}
	
	@Override
	public java.lang.String getTokenID() {
		return QUOTE.tokenId;
	}
}
