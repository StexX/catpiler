/*
 * SyntaxException.java Copyright (C) 2010 Stephanie Stroka 
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
package catpiler.frontend.exception;

/**
 * The SyntaxException is thrown if the CATpiler Scanner 
 * detects an incorrect Token, e.g. a literal that starts 
 * with a number, etc.
 * 
 * @author Stephanie Stroka
 * 			(stephanie.stroka@sbg.ac.at)
 *
 */
public class SyntaxException extends Exception {

	/**
	 * SerialVersionId
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public SyntaxException() {
		super();
	}

	/**
	 * Constructor with param 'message'
	 * @param message
	 */
	public SyntaxException(String message) {
		super(message);
	}

}
