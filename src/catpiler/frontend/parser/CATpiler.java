package catpiler.frontend.parser;

public class CATpiler {

	/**
	 * @param args
	 */
	public static void main(java.lang.String[] args) {
		if(args[0] != null) {
			Parser.loadSourcecode(args[0]);
		}
	}

}
