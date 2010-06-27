package catpiler.frontend.parser.symboltable;

import java.util.ArrayList;

import catpiler.frontend.scanner.keywords.NOOB;

public class FunctionItem {

	private String functionName;
	
	private ArrayList<String> argumentTypes;

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public ArrayList<String> getArgumentTypes() {
		return argumentTypes;
	}

	public void setArgumentTypes(ArrayList<String> argumentTypes) {
		this.argumentTypes = argumentTypes;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FunctionItem)) {
			return false;
		} 
		FunctionItem o = (FunctionItem) obj;
		if(!this.functionName.equals(o.functionName)) {
			return false;
		}
		if(this.argumentTypes.size() != o.argumentTypes.size()) {
			return false;
		}
		for(int i=0; i<this.argumentTypes.size(); i++) {
			if(!this.argumentTypes.get(i).equals(o.argumentTypes.get(i))) {
				if(!this.argumentTypes.get(i).equals(NOOB.tokenId) &&
						!o.argumentTypes.get(i).equals(NOOB.tokenId))
					return false;
			}
		}
		return true;
	}
	
}
