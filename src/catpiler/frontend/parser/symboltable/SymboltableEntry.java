package catpiler.frontend.parser.symboltable;

import java.util.ArrayList;

public class SymboltableEntry {
	
	private String name;
	
	private String category;
	
	private String attribute;
	
	private String type;

	private String address;
	
	private String reg;
	
	private ArrayList<String> attributeArrayList;
	
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
	
	public ArrayList<String> getAttributeArrayList() {
		return attributeArrayList;
	}

	public void setAttributeArrayList(ArrayList<String> attributeArrayList) {
		this.attributeArrayList = attributeArrayList;
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
}
