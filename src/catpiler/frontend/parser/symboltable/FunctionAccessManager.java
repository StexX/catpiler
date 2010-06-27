package catpiler.frontend.parser.symboltable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FunctionAccessManager {

	
	private Map<String, FunctionItem> functionCaller;
	
	private Map<String, FunctionItem> functionDefinition;
	
	private Set<String> undefinedFunctions;
	
	public FunctionAccessManager() {
		functionCaller = new HashMap<String, FunctionItem>();
		functionDefinition = new HashMap<String, FunctionItem>();
		undefinedFunctions = new HashSet<String>();
	}
	
	public boolean addFunctionCaller(FunctionItem funcItem) {
		if(functionCaller.containsKey(funcItem.getFunctionName()))
			return false;
		else {
			functionCaller.put(funcItem.getFunctionName(), funcItem);
			return true;
		}
	}
	
	public boolean addFunctionDefinition(FunctionItem funcItem) {
		if(functionDefinition.containsKey(funcItem.getFunctionName()))
			return false;
		else {
			functionDefinition.put(funcItem.getFunctionName(), funcItem);
			return true;
		}
	}

	public Set<String> getUndefinedFunctions() {
		return undefinedFunctions;
	}

	/*
	 * checks whether every function call is defined
	 */
	public boolean check() {
		for(String s : functionCaller.keySet()) {
			if(!functionDefinition.containsKey(s)) {
				undefinedFunctions.add(s);
				return false;
			} else {
				if(!functionCaller.get(s).equals(functionDefinition.get(s))) {
					undefinedFunctions.add(s);
					return false;
				}
			}
		}
		return true;
	}
}
