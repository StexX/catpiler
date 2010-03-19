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
import static org.junit.Assert.fail;

import org.junit.Assert;

import catpiler.frontend.exception.SyntaxException;

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
	
	/*
	 * counter for white spaces, to determine
	 * the correct position in the source 
	 * after erasing blanks.
	 */
	private int whitespace_count;
	
	/**
	 * Initializes the Scanner variables
	 * @param input
	 */
	public Scanner(String input) {
		source = input.toCharArray();
		src_pointer = 0;
		current_token = new char[100];
		token_pointer = 0;
		whitespace_count = 0;
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
	public Token lookupToken() throws SyntaxException {
		TokenService tService = new TokenService();
		Token t = null;
		token_pointer = 0;
		// erase all the white spaces first
		while(readWhiteSpace()) {
			src_pointer++;
			whitespace_count++;
		}
		// read character that is not a whitespace
		readNextChar();
		// look up the token that matches the current_token char array
		// if more then one tokens have been found, null is returned,
		// which forces the loop to read another character and try to
		// detect the token again
		while((t = tService.lookupToken(current_token, token_pointer)) == null) {
			token_pointer++;
			// if the next char is a whitespace, finalize current_token
			// and loop up token that exactly matches the current_token 
			// char array
			// TODO: error handling if no token matches exactly.
			if(readWhiteSpace()) {
				current_token[token_pointer+1] = '\0';
			} else {
				readNextChar();
			}
		}
		boolean id = false;
		// check whether the rest of the characters match the token
		// and if it is followed by a whitespace or end-of-string,
		// because there may exist identifiers which begin with the 
		// same characters as a reserved keyword.
		while(!(readWhiteSpace() || readEOS())) {
			token_pointer++;
			readNextChar();
			// if the token was recognized to be an int (that is, if
			// the first char is a number), but continues with another
			// char somewhere, it is identified as a syntax error
			if(t.equals(TokenTable.integer)) {
				if(!(current_token[token_pointer] >= '0' && current_token[token_pointer] <= '9')) {
					//fail
					throw new SyntaxException();
				}
			} else if(t.equals(TokenTable.id)) {
				// if the token was recognized to be an id, but contains
				// characters other than letters, digits and underscores,
				// if is identified as a syntax error
				if(!(current_token[token_pointer] >= 'A' && current_token[token_pointer] <= 'Z' || 
						current_token[token_pointer] >= 'a' && current_token[token_pointer] <= 'z' ||
						current_token[token_pointer] == '_') ||
						current_token[token_pointer] >= '0' && current_token[token_pointer] <= '9') {
					//fail
					throw new SyntaxException();
				}
				id = true;
			} else if((!t.equals(TokenTable.id)) && (!t.equals(TokenTable.string)) 
					&& token_pointer >= t.getTokenID().length) {
				// if the current_token is bigger than the identified token,
				// the id flag is set to true. This happens if we find an identifier
				// that starts with the same characters as a reserved keyword.
				// Just needs to be checked if the token isn't recognized as an id anyhow
				id = true;
			} else if((!t.equals(TokenTable.id)) && (!t.equals(TokenTable.string)) 
					&& !(t.getTokenID()[token_pointer] == current_token[token_pointer])) {
				// the same happens if the found token has the same length as the identified 
				// keyword, but holds different characters at the end of the char array
				id = true;
			}
		}
		// if the id flag was set, reset the token to an id
		if(id) {
			t = TokenTable.id;
		} 
		
		// append attributes, where necessary
		if(t.equals(TokenTable.id)) {
			t.setAttribute(new String(current_token));	
		} else if(t.equals(TokenTable.string)) {
			if(current_token[token_pointer] != '\"') {
				//fail
				throw new SyntaxException();
			}
			t.setAttribute(new String(current_token));
		} else if(t.equals(TokenTable.integer)) {
			t.setAttribute(new String(current_token));
		} else if(t.equals(TokenTable.comment_1)) {
			// skip until next LF '\n'
			eraseComment(false);
		} else if(t.equals(TokenTable.comment_2)) {
			eraseComment(true);
		}
		
		// return token
		// TODO: what if it's null?
		return t;
	}
	
	/**
	 * returns true if it read whitespaces, false otherwise
	 * @return
	 */
	private boolean readWhiteSpace() {
		if(src_pointer < source.length && source[src_pointer] != '\0' && source[src_pointer] == ' ') {
			return true;
		}
		return false;
	}
	
	private void eraseComment(boolean multipleLines) {
		if(!multipleLines) {
			while(src_pointer < source.length && source[src_pointer] != '\n') {
				src_pointer++;
				if(readWhiteSpace()) {
					whitespace_count++;
				}
			}
		} else {
			whitespace_count = 0;
			while(src_pointer < source.length) {
//				while(readWhiteSpace()) {
//					src_pointer++;
//					whitespace_count++;
//				}
				try {
					if(lookupToken().equals(TokenTable.comment_3)) {
						// found the end
//						src_pointer = src_pointer + whitespace_count;
						whitespace_count = 0;
						return;
					}
				} catch (SyntaxException e) {
					// don't care about SyntaxExceptions in the comment
				}
//				src_pointer++;
//				if(readWhiteSpace()) {
//					whitespace_count++;
//				}
			}
		}
	}
	
	/**
	 * returns true if the end of the string is reached
	 * @return
	 */
	private boolean readEOS() {
		if(src_pointer >= source.length || source[src_pointer] == '\0') {
			return true;
		}
		return false;
	}
	
	private boolean readEOL() {
		if(src_pointer < source.length || source[src_pointer] == '\n') {
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
			current_token[token_pointer] = source[src_pointer];
			current_token[token_pointer+1] = '\0';
			src_pointer++;
		} else {
			// if we reached the end of the source code, 
			// we just append \0
			current_token[token_pointer] = '\0';
		}
	}
	
	/**
	 * Searches tokens in the source code.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	public Token[] search4Tokens() throws SyntaxException {
		Token[] tokens = new Token[100];
		
		int i = 0;
		Token t = null;
		src_pointer = 0;
		// searches tokens in the source code, appends them in
		// the tokens array and moves the src_pointer to the unread
		// characters
		while(src_pointer < source.length && (t = lookupToken()) != null) {
			src_pointer = src_pointer + whitespace_count;
			tokens[i] = t;
			i++;
			whitespace_count = 0;
		}
		
		return tokens;
	}
	
	public static void main(String[] args) {
//		Scanner s = new Scanner("BTW BOTHThisIsAnIdentifier anotherID \n  \"someStr\" 1337 SAEM HOW DUZ I OIC");
//		Token[] tokens = null;
//		try {
//			if((tokens = s.search4Tokens()) != null) {
//				Assert.assertEquals(TokenTable.string , tokens[0]);
//				Assert.assertEquals(TokenTable.integer , tokens[1]);
//				Assert.assertEquals(TokenTable.op_eq, tokens[2]);
//				Assert.assertEquals(TokenTable.function_1, tokens[3]);
//				Assert.assertEquals(TokenTable.function_2, tokens[4]);
//				Assert.assertEquals(TokenTable.var_decl_1, tokens[5]);
//				Assert.assertEquals(TokenTable.fc_if_end, tokens[6]);
//				Assert.assertNull(tokens[11]);
//			} else {
//				fail("Could find correct tokens :(");
//			}
//		} catch (SyntaxException e) {
//			e.printStackTrace();
//		}
		
//		Scanner s = new Scanner("OBTW BOTHThisIsAnIdentifier \n anotherID TLDR  \"someStr\" 1337 SAEM HOW DUZ I OIC");
//		Token[] tokens = null;
//		try {
//			if((tokens = s.search4Tokens()) != null) {
//				Assert.assertEquals(TokenTable.string , tokens[0]);
//				Assert.assertEquals(TokenTable.integer , tokens[1]);
//				Assert.assertEquals(TokenTable.op_eq, tokens[2]);
//				Assert.assertEquals(TokenTable.function_1, tokens[3]);
//				Assert.assertEquals(TokenTable.function_2, tokens[4]);
//				Assert.assertEquals(TokenTable.var_decl_1, tokens[5]);
//				Assert.assertEquals(TokenTable.fc_if_end, tokens[6]);
//				Assert.assertNull(tokens[11]);
//			} else {
//				fail("Could find correct tokens :(");
//			}
//		} catch (SyntaxException e) {
//			e.printStackTrace();
//		}
	}
}
