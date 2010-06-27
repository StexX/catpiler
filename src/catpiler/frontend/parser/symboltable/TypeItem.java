package catpiler.frontend.parser.symboltable;

public class TypeItem {

	private String name;
	
	private Symboltable symbolTable;
	
	private int size;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Symboltable getSymbolTable() {
		return symbolTable;
	}

	public void setSymbolTable(Symboltable symbolTable) {
		this.symbolTable = symbolTable;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	
}
