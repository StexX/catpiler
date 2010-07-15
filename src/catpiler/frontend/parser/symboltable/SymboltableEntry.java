package catpiler.frontend.parser.symboltable;


public class SymboltableEntry {
	
	private String name;
	
	private String category;
	
	private String attribute;
	
	private String heap;
	
	private int heapsize;
	
	private String type;

	private String address;
	
	private String reg;
	
	private int level;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public String getReg() {
		return reg;
	}

	public void setReg(String reg) {
		this.reg = reg;
	}

	public String getHeap() {
		return heap;
	}

	public void setHeap(String heap) {
		this.heap = heap;
	}

	public int getHeapsize() {
		return heapsize;
	}

	public void setHeapsize(int heapsize) {
		this.heapsize = heapsize;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	
}
