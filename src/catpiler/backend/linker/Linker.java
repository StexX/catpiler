package catpiler.backend.linker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

import catpiler.frontend.parser.symboltable.FunctionAccessManager;
import catpiler.frontend.parser.symboltable.FunctionItem;
import catpiler.utils.ErrorReporter;

public class Linker {

	public static boolean needLink = false;
	
	public static String destination;

	public static void linkSources(Set<String> files) {
		
		FunctionAccessManager functionAccessManager = 
			new FunctionAccessManager();
		
		try {
			for(String s : files) {
				File f = new File(s);
				Scanner scanner = new Scanner(f);
				String[] function_calls = null;
				String[] function_defs = null;
				String line = null;
				if(scanner.hasNextLine()) {
					line = scanner.nextLine();
				} else {
					ErrorReporter.markError("Incomplete metadata");
					break;
				}
				if(scanner.hasNextLine()) {
					String function_call_line = scanner.nextLine();
					function_calls = function_call_line.substring(1).trim().split(",");
				} else {
					ErrorReporter.markError("Incomplete metadata");
					break;
				}
				if(scanner.hasNextLine()) {
					String function_def_line = scanner.nextLine();
					function_defs = function_def_line.substring(1).trim().split(",");
				} else {
					ErrorReporter.markError("Incomplete metadata");
					break;
				}
				for(String call : function_calls) {
					if(call.length() > 0) {
						FunctionItem functionItem = new FunctionItem();
						String[] call_split = call.trim().split(" ");
						functionItem.setFunctionName(call_split[0]);
						ArrayList<String> args = new ArrayList<String>();
						for(int i=1; i<call_split.length; i++) {
							args.add(call_split[i]);
						}
						functionItem.setArgumentTypes(args);
						functionAccessManager.addFunctionCaller(functionItem);
					}
				}
				for(String def : function_defs) {
					if(def.length() > 0) {
						FunctionItem functionItem = new FunctionItem();
						String[] def_split = def.trim().split(" ");
						functionItem.setFunctionName(def_split[0]);
						ArrayList<String> args = new ArrayList<String>();
						for(int i=1; i<def_split.length; i++) {
							args.add(def_split[i]);
						}
						functionItem.setArgumentTypes(args);
						functionAccessManager.addFunctionDefinition(functionItem);
					}
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		// check whether every called function is really implemented
		if(!ErrorReporter.isError() && !functionAccessManager.check()) {
			ErrorReporter.markError("Not all functions have been correctly implemented: " + 
					functionAccessManager.getUndefinedFunctions(), false);
		}
		
		
		ArrayList<String> dataSegmentList = new ArrayList<String>();
		ArrayList<String> textSegmentList = new ArrayList<String>();
		boolean dataSection = false;
		boolean textSection = false;
		boolean mmSection = false;
		try {
			File program = new File(destination);
			// write every character from the single files 
			// into the final program file
			BufferedWriter writer = new BufferedWriter(new FileWriter(program));
			for(String f : files) {
				File file = new File(f);
				try {
					Scanner scanner = new Scanner(file);
					while(scanner.hasNextLine()) {
						String line = scanner.nextLine();
						if(line.startsWith(".data")) {
							dataSection = true;
							textSection = false;
						} else if(line.startsWith(".text")) {
							dataSection = false;
							textSection = true;
						} else {
							if(dataSection) {
								if(!dataSegmentList.contains(line))
									dataSegmentList.add(line);
							} else if(textSection) {
								
								if(line.equals("#begin_mm")) {
									// hacking my way through the world ;)
									if(!mmSection) {
										
										while(scanner.hasNextLine() && 
												!(line = scanner.nextLine()).equals("#end_mm")) {
											textSegmentList.add(line);
										}
										mmSection = true;
									} else {
										// skip mm section
										while(scanner.hasNextLine() && 
												!(line = scanner.nextLine()).equals("#end_mm"));
									}
								} else {
									textSegmentList.add(line);
								}
							}
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			writer.write(".data\n");
			for(String d : dataSegmentList) {
				writer.write(d + "\n");
			}
			writer.write(".text\n");
			for(String t : textSegmentList) {
				writer.write(t + "\n");
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
