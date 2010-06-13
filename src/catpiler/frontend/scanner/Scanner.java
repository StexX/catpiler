/*
 * Scanner.java Copyright (C) 2010 Stephanie Stroka 
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
import java.io.EOFException;

import org.junit.Assert;

import catpiler.frontend.exception.NoTokenFoundException;
import catpiler.frontend.exception.SyntaxException;
import catpiler.frontend.scanner.keywords.BTW;
import catpiler.frontend.scanner.keywords.Identifier;
import catpiler.frontend.scanner.keywords.Int;
import catpiler.frontend.scanner.keywords.Keyword;
import catpiler.frontend.scanner.keywords.OBTW;
import catpiler.frontend.scanner.keywords.R;
import catpiler.frontend.scanner.keywords.TLDR;

/**
 * The CATpiler Scanner searches for Tokens in a given source
 * code to prepare if for the Parser and to detect SyntaxExceptions,
 * e.g. wrongly written literals or numbers. 
 * 
 * @author Stephanie Stroka
 * 			(stephanie.stroka@sbg.ac.at)
 *
 */
public class Scanner {

	/*
	 * source code to be searched for tokens
	 */
	private char[] source;
	
	/*
	 * source code pointer, points to the 
	 * current position in the source code
	 */
	private int src_pointer;
	
	/*
	 * current token to be identified
	 */
	private char[] current_token;
	
	/*
	 * token pointer, points to the current 
	 * position in the token
	 */
	private int token_pointer;
	
	private int pointerBeforeToken;
	
	private int lineCount;
	
	/**
	 * Initializes the Scanner variables
	 * @param input
	 */
	public Scanner(String input) {
		source = input.toCharArray();
		src_pointer = 0;
		pointerBeforeToken = 0;
		token_pointer = 0;
		lineCount = 1;
	}	
	
	/**
	 * Calls the readNextChar() to fill out the
	 * current_token char-array and tries to detect 
	 * a token by calling TokenService.lookupToken().
	 * 
	 * The found token is returned.
	 * 
	 * @return found token
	 * @throws SyntaxException Thrown if the syntax of the current 
	 * token is incorrect.
	 */
	public Keyword lookupToken() {
		
		pointerBeforeToken = src_pointer;
		current_token = new char[100];
		TokenService tService = new TokenService();
		Keyword t = null;
		token_pointer = -1;
		// erase all the white spaces first
		while(readWhiteSpace());
		
		// read character that is not a whitespace
		readNextChar();
		// look up the token that matches the current_token char array
		// if more than one tokens have been found, null is returned,
		// which forces the loop to read another character and try to
		// detect the token again
		try {
			
			t = tService.returnFirstMatchinToken(current_token, token_pointer);
			if(readWhiteSpace() || readEOS()) {
				token_pointer++;
				current_token[token_pointer] = '\0';
			} else {
				readNextChar();
			}
			
			while(t != null &&
					!(current_token[token_pointer] == '\0' && token_pointer == t.getTokenID().length()) &&
					!((t instanceof catpiler.frontend.scanner.keywords.String) 
						|| (t instanceof Int))) {
				// if the next char is a whitespace, finalize current_token
				// and loop up token that exactly matches the current_token 
				// char array
				// TODO: error handling if no token matches exactly.
				
				if(token_pointer >= t.getTokenID().length() || 
						t.getTokenID().charAt(token_pointer) != current_token[token_pointer]) {
					retract();
					t = tService.returnFirstMatchinToken(current_token, token_pointer);
				}
				if(t == null) {
					break;
				}
				if(readWhiteSpace() || readEOS()) {
					token_pointer++;
					current_token[token_pointer] = '\0';
				} else {
					readNextChar();
				}
			}
		} catch (EOFException e) {
			// reached end of file -> no token found
			return t;
		} catch (NoTokenFoundException e) {
			// nothing found?! Could be an id...
			t = new Identifier();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		
		if(t == null) {
			t = new Identifier();
		}

		boolean fail = false;
		if(t instanceof Identifier) {
			// if the token was recognized to be an id, but contains
			// characters other than letters, digits and underscores,
			// if is identified as a syntax error
			if(!(current_token[token_pointer] >= 'A' && current_token[token_pointer] <= 'Z' || 
					current_token[token_pointer] >= 'a' && current_token[token_pointer] <= 'z' ||
					current_token[token_pointer] == '_' ||
					current_token[token_pointer] >= '0' && current_token[token_pointer] <= '9')) {
				//fail
				fail = true;
			}
			while(!(readWhiteSpace() || readEOS())) {
				readNextChar();
				if(!(current_token[token_pointer] >= 'A' && current_token[token_pointer] <= 'Z' || 
						current_token[token_pointer] >= 'a' && current_token[token_pointer] <= 'z' ||
						current_token[token_pointer] == '_' ||
						current_token[token_pointer] >= '0' && current_token[token_pointer] <= '9')) {
					//fail
					fail = true;
				}
			}
			token_pointer++;
			current_token[token_pointer] = '\0';
			if(fail) {
				System.out.println("Syntax error on identifier " + new String(current_token));
				return null;
			}
			((Identifier) t).setName(new String(current_token));
//			t.setAttribute(new String(current_token));
		} else if(t instanceof Int) {
			// if the token was recognized to be an int (that is, if
			// the first char is a number), but continues with another
			// char somewhere, it is identified as a syntax error
			if(current_token[token_pointer] != '\0') {
				if(!(current_token[token_pointer] >= '0' && current_token[token_pointer] <= '9')) {
					fail = true;
				}
				while(!(readWhiteSpace() || readEOS())) {
					readNextChar();
					if(!(current_token[token_pointer] >= '0' && current_token[token_pointer] <= '9')) {
						fail = true;
					}
				}
				token_pointer++;
				current_token[token_pointer] = '\0';
				if(fail) {
					System.out.println("Syntax error on number " + new String(current_token));
					return null;
				}
			}
			t.setAttribute(new String(current_token));
		} else if(t instanceof catpiler.frontend.scanner.keywords.String) {
			while(!(current_token[token_pointer] == '\"' && 
					current_token[token_pointer-1] != ':')) {
				readNextChar();
				if(current_token[token_pointer] == '\0') {
					fail = true;
					System.out.println("Syntax error on string " + new String(current_token) + 
							". Expected \".");
					return null;
				}
			}
			token_pointer++;
			current_token[token_pointer] = '\0';
			t.setAttribute(new String(current_token));
		} else if(t instanceof BTW) {
			// skip until next LF '\n'
			eraseComment(false);
			return lookupToken();
		} else if(t instanceof OBTW) {
			eraseComment(true);
			return lookupToken();
		}
		
		// return token or null if no token has been found
		return t;
	}
	
	/**
	 * returns true if it read whitespaces, false otherwise
	 * @return
	 */
	private boolean readWhiteSpace() {
		if(src_pointer < source.length && source[src_pointer] != '\0' 
			&& (source[src_pointer] == ' ' || source[src_pointer] == '\n')) {
			if(source[src_pointer] == '\n')
				lineCount++;
			src_pointer++;
			return true;
		}
		return false;
	}
	
	private void eraseComment(boolean multipleLines) {
		if(!multipleLines) {
			while(src_pointer < source.length && source[src_pointer] != '\n') {
				src_pointer++;
			}
			src_pointer++;
		} else {
			while(src_pointer < source.length) {
				if(lookupToken() instanceof TLDR) {
					// found the end
					return;
				}
			}
		}
	}
	
	/**
	 * returns true if the end of the string is reached
	 * @return
	 */
	private boolean readEOS() {
		if(src_pointer >= source.length || source[src_pointer] == '\0') {
			src_pointer++;
			return true;
		}
		return false;
	}
	
	/**
	 * reads the next char from the source array and appends
	 * its character to the current_token array (followed by a \0,
	 * to ensure that the old array is fully overwritten)
	 */
	private void readNextChar() {
		if(src_pointer < source.length) {
			current_token[token_pointer+1] = source[src_pointer];
			current_token[token_pointer+2] = '\0';
			src_pointer++;
			token_pointer++;
		} else {
			// if we reached the end of the source code, 
			// we just append \0
			current_token[token_pointer+1] = '\0';
		}
	}
	
	private int retract() {
		int tmp_tkn_ptr = token_pointer;
		src_pointer = src_pointer-token_pointer;
		token_pointer = 0;
		return tmp_tkn_ptr;
	}
	
	public void setSrcPointer(int pointer) {
		src_pointer = pointer;
	}
	
	/**
	 * Searches tokens in the source code.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	public Keyword[] search4Tokens() throws SyntaxException {
		Keyword[] tokens = new Keyword[100];
		
		int i = 0;
		src_pointer = 0;
		// searches tokens in the source code, appends them in
		// the tokens array and moves the src_pointer to the unread
		// characters
		while(src_pointer < source.length) {
			tokens[i] = lookupToken();
			i++;
		}
		
		return tokens;
	}

	public int getPointerBeforeToken() {
		return pointerBeforeToken;
	}
	
	public int getSrcPointer() {
		return src_pointer;
	}
	
	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner("var1 R 3 ");
		Keyword k1 = s.lookupToken();
		Assert.assertTrue(k1 instanceof Identifier);
		System.out.println("first token: " + k1.getTokenID());
		Keyword k2 = s.lookupToken();
		Assert.assertTrue(k2 instanceof R);
		System.out.println("second token: " + k2.getTokenID());
		Keyword k3 = s.lookupToken();
		Assert.assertTrue(k3 instanceof Int);
		System.out.println("third token: " + k3.getTokenID());
	}

//	public static void main(String[] args) {
////		Scanner s = new Scanner("BTW BOTHThisIsAnIdentifier 1test blafasel anotherID \n  \"someStr\" 1337 SAEM HOW DUZ I OIC");
////		Token[] tokens = null;
////		try {
////			if((tokens = s.search4Tokens()) != null) {
////				Assert.assertEquals(TokenTable.string , tokens[0]);
////				Assert.assertEquals(TokenTable.integer , tokens[1]);
////				Assert.assertEquals(TokenTable.op_eq, tokens[2]);
////				Assert.assertEquals(TokenTable.function_1, tokens[3]);
////				Assert.assertEquals(TokenTable.function_2, tokens[4]);
////				Assert.assertEquals(TokenTable.var_decl_1, tokens[5]);
////				Assert.assertEquals(TokenTable.fc_if_end, tokens[6]);
////				Assert.assertNull(tokens[11]);
////			} else {
////				fail("Could find correct tokens :(");
////			}
////		} catch (SyntaxException e) {
////			e.printStackTrace();
////		}
//		
////		Scanner s = new Scanner("OBTW BOTHThisIsAnIdentifier \n anotherID TLDR  \"someStr\" 1337 SAEM HOW DUZ I OIC");
////		Token[] tokens = null;
////		try {
////			if((tokens = s.search4Tokens()) != null) {
////				Assert.assertEquals(TokenTable.string , tokens[0]);
////				Assert.assertEquals(TokenTable.integer , tokens[1]);
////				Assert.assertEquals(TokenTable.op_eq, tokens[2]);
////				Assert.assertEquals(TokenTable.function_1, tokens[3]);
////				Assert.assertEquals(TokenTable.function_2, tokens[4]);
////				Assert.assertEquals(TokenTable.var_decl_1, tokens[5]);
////				Assert.assertEquals(TokenTable.fc_if_end, tokens[6]);
////				Assert.assertNull(tokens[11]);
////			} else {
////				fail("Could find correct tokens :(");
////			}
////		} catch (SyntaxException e) {
////			e.printStackTrace();
////		}
//		
//		Scanner s = new Scanner("BOTHThisIsAnIdentifier anotherID  \"someStr :\" test\" 1337 SAEM HOW DUZ I OIC");
//		Keyword[] tokens = null;
//		try {
//			if((tokens = s.search4Tokens()) != null) {
//				Assert.assertTrue(tokens[0] instanceof Identifier);
//				Assert.assertEquals(new String("BOTHThisIsAnIdentifier"),tokens[0].getAttribute().trim());
//				Assert.assertTrue(tokens[1] instanceof Identifier);
//				Assert.assertEquals(new String("anotherID"),tokens[1].getAttribute().trim());
//				Assert.assertTrue(tokens[2] instanceof catpiler.frontend.scanner.keywords.String);
//				Assert.assertEquals(new String("\"someStr :\" test\""),tokens[2].getAttribute().trim());
//				Assert.assertTrue(tokens[3] instanceof Int);
//				Assert.assertEquals(new String("1337"),tokens[3].getAttribute().trim());
//				Assert.assertTrue(tokens[4] instanceof SAEM);
//				Assert.assertTrue(tokens[5] instanceof HOW);
//				Assert.assertTrue(tokens[6] instanceof DUZ);
//				Assert.assertTrue(tokens[7] instanceof I);
//				Assert.assertTrue(tokens[8] instanceof OIC);
//				Assert.assertNull(tokens[9]);
//			} else {
//				fail("Could find correct tokens :(");
//			}
//		} catch (SyntaxException e) {
//			e.printStackTrace();
//		}
//		
////		Scanner s = new Scanner("BTW");
////		Token t = null;
////		try {
////			t = s.lookupToken();
////			// Comments will be erased. 
////			// We therefore do not expect any token
////			Assert.assertNull(t);
////		} catch (SyntaxException e) {
////			e.printStackTrace();
////		}
//	}
}
