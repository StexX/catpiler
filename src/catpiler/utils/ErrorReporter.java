package catpiler.utils;
import catpiler.frontend.scanner.Scanner;


public class ErrorReporter {

	private static boolean error;
	
	private static Scanner s;
	
	public ErrorReporter() {
		error = false;
	}
	
	public static void markError(java.lang.String str) {
		ErrorReporter.markError(str, true);
	}
	
	public static void markError(java.lang.String str, boolean printLine) {
		error = true;
		if(printLine)
			System.out.println("Found an error at line " + s.getLineCount() + ": " + str);
		else
			System.out.println("Found an error : " + str);
	}
	
	public static void markWarning(java.lang.String str) {
		ErrorReporter.markWarning(str, true);
	}
	
	public static void markWarning(java.lang.String str, boolean printLine) {
		if(printLine)
			System.out.println("Warning at line " + s.getLineCount() + ": " + str);
		else
			System.out.println("Warning : " + str);
	}

	public static boolean isError() {
		return ErrorReporter.error;
	}

	public static void setError(boolean error) {
		ErrorReporter.error = error;
	}

	public static Scanner getS() {
		return s;
	}

	public static void setS(Scanner s) {
		ErrorReporter.s = s;
	}
	
	
}
