/*
 * Token.java Copyright (C) 2010 Stephanie Stroka 
 * 
 * This library is free software; you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. 
 * 
 * This library is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for 
 * more details. 
 * 
 * You should have received a copy of the GNU Lesser General Public License along 
 * with this library; if not, write to the Free Software Foundation, Inc., 59 
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 */
package catpiler.frontend.scanner;

/**
 * Token class - a Token consists of a tokenID and an attribute,
 * which is needed, for example, to transport the name of an id, 
 * the content of a string or the value of a number. The attribute 
 * value is null if the Token is a simple keyword.
 * 
 * @author Stephanie Stroka
 * 			(stephanie.stroka@sbg.ac.at)
 *
 */
public class Token {

	/*
	 * keyword or identifier
	 */
	private char[] tokenID;
	
	/*
	 * value of the identifier
	 */
	private char[] attribute;

	/**
	 * default constructor, sets the tokenID
	 * @param tokenID
	 */
	public Token(String tokenID) {
		this.tokenID = tokenID.toCharArray();
	}

	/**
	 * setter for tokenID
	 * @param tokenID
	 */
	public void setTokenID(String tokenID) {
		this.tokenID = tokenID.toCharArray();
	}

	/**
	 * getter for tokenID
	 * @return
	 */
	public char[] getTokenID() {
		return tokenID;
	}

	/**
	 * setter for attribute
	 * @param attribute
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute.toCharArray();
	}

	/**
	 * getter for attribute
	 * @return
	 */
	public char[] getAttribute() {
		return attribute;
	}
}
