package catpiler.frontend.scanner.keywords;

public class Keyword {
	
	public final static int KEYWORDCOUNT = 56;
	protected int numericId = Integer.MAX_VALUE;
	
	protected Keyword() {
	}
	
	public void setAttribute(java.lang.String attribute) {
		System.err.println("Must not be called");
	};

	public java.lang.String getAttribute() {
		System.err.println("Must not be called");
		return null;
	}
	
	public java.lang.String getTokenID() {
		return this.getClass().getSimpleName();
	}
	
	public int getNumericID() {
		return numericId;
	}
	
}
