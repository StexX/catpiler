package catpiler.frontend.parser.lib;

import java.io.File;
import java.io.FileNotFoundException;

/*
 * The header of each .cat file contains meta-
 * information about the file.
 * The first line represents the module name and
 * the timestamp that provides information about
 * the build-time.
 * The second line contains the name of the modules
 * that import and use the functions of the current
 * module. This information is necessary for the final
 * function-access-check. E.g. if one deleted a function of
 * a module, the caller function must be checked, whether it
 * called the deleted function. 
 * This is also the reason, why we store the function labels 
 * of all implemented functions in the third line and all 
 * called functions in the fourth line of the header. 
 * Function-Format: function_label(arg1_type, arg2_type...).
 *  
 */
public class ExtractFileHeader {

	/**
	 * Returns an array of Strings with the header of the file
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public String[] getHeaderLines(File mainFileCat) throws FileNotFoundException {
		java.util.Scanner filescannerCat;

		filescannerCat = new java.util.Scanner(mainFileCat);
		java.lang.String curLine;

		String[] headerLines = new String[4];
		for(int i=0; i<4; i++) {
			if(!filescannerCat.hasNextLine()) {
				// TODO: Error Handling or simply recompile?!
				// Chose to recompile for now...
				throw new FileNotFoundException();
			}
			headerLines[i] = filescannerCat.nextLine();
		}
		return headerLines;
	}
	
	/**
	 * Extracts important information from header-line.
	 * First string array element is
	 * @param line
	 * @return
	 */
	public String[] extractInformation(String line) {
		String[] name_content = line.split(":");
		String[] contents = name_content[1].split(",");
		String[] returnStr = new String[contents.length+1];
		returnStr[0] = name_content[0];
		for(int i=0; i<contents.length; i++) {
			returnStr[i+1] = contents[i];
		}
		return returnStr;
	}
}
