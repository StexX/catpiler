package catpiler.frontend.scanner.keywords;

public class Identifier extends Keyword {

	public final static java.lang.String tokenId = "identifier";
	
	private java.lang.String name;
	
	private java.lang.String attribute;
	
	private java.lang.String type;

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	@Override
	public java.lang.String getAttribute() {
		return attribute;
	}

	@Override
	public void setAttribute(java.lang.String attribute) {
		this.attribute = attribute;
	}
	
	@Override
	public java.lang.String getTokenID() {
		return Identifier.tokenId;
	}

	public void setType(java.lang.String type) {
		this.type = type;
	}

	public java.lang.String getType() {
		return type;
	}
}
