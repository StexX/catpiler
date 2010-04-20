package catpiler.frontend.scanner.keywords;

public abstract class Keyword {
	
	public final static int KEYWORDCOUNT = 56;
	
	public abstract void setAttribute(java.lang.String attribute);

	public abstract java.lang.String getAttribute();
	
	public abstract java.lang.String getTokenID();
}
