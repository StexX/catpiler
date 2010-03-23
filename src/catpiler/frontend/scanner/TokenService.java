/*
 * TokenService.java Copyright (C) 2010 Stephanie Stroka 
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


/**
 * Outsourcing of Token-Finder, to obtian a better overview.
 * Initializes all keyword tokens in a arrayOfPotentialTokens,
 * tries to match the current_token with a keyword token
 * and returns either a keyword, string, integer or identifier 
 * token.
 * 
 * @author Stephanie Stroka
 * 			(stephanie.stroka@sbg.ac.at)
 *
 */
public class TokenService {
	
	/*
	 * array of potential tokens, holds all keyword tokens.
	 * Entries that don't match the current_token will be 
	 * set to null.
	 */
	private Token[] arrayOfPotentialTokens;

	/*
	 * counter for non-null elements in arrayOfPotentialTokens
	 */
	private int potToken_count;
	
	/**
	 * Default constructor calls init()
	 */
	public TokenService() {
		init();
	}
	
	/**
	 * Appends all keyword tokens to the arrayOfPotentialTokens.
	 * If a new keyword token is defined, it needs to be added to
	 * the token array.
	 */
	private void init() {
		potToken_count = TokenTable.TOKEN_COUNT;
		
		arrayOfPotentialTokens = new Token[TokenTable.TOKEN_COUNT];
		arrayOfPotentialTokens[0] = TokenTable.comment_1;
		arrayOfPotentialTokens[1] = TokenTable.file_begin;
		arrayOfPotentialTokens[2] = TokenTable.file_end;
		arrayOfPotentialTokens[3] = TokenTable.var_decl_1;
		arrayOfPotentialTokens[4] = TokenTable.var_decl_2;
		arrayOfPotentialTokens[5] = TokenTable.var_decl_3;
		arrayOfPotentialTokens[6] = TokenTable.var_assign;
		arrayOfPotentialTokens[7] = TokenTable.type_chararray;
		arrayOfPotentialTokens[8] = TokenTable.type_bool;
		arrayOfPotentialTokens[9] = TokenTable.type_int;
		arrayOfPotentialTokens[10] = TokenTable.val_true;
		arrayOfPotentialTokens[11] = TokenTable.val_false;
		arrayOfPotentialTokens[12] = TokenTable.char_lf;
		arrayOfPotentialTokens[13] = TokenTable.char_quote;
		arrayOfPotentialTokens[14] = TokenTable.op_of;
		arrayOfPotentialTokens[15] = TokenTable.op_seq;
		arrayOfPotentialTokens[16] = TokenTable.op_sum;
		arrayOfPotentialTokens[17] = TokenTable.op_diff;
		arrayOfPotentialTokens[18] = TokenTable.op_prod;
		arrayOfPotentialTokens[19] = TokenTable.op_quot;
		arrayOfPotentialTokens[20] = TokenTable.op_max;
		arrayOfPotentialTokens[21] = TokenTable.op_min;
		arrayOfPotentialTokens[22] = TokenTable.op_both;
		arrayOfPotentialTokens[23] = TokenTable.op_or;
		arrayOfPotentialTokens[24] = TokenTable.op_all;
		arrayOfPotentialTokens[25] = TokenTable.op_any;
		arrayOfPotentialTokens[26] = TokenTable.op_end;
		arrayOfPotentialTokens[27] = TokenTable.op_eq;
		arrayOfPotentialTokens[28] = TokenTable.op_neq;
		arrayOfPotentialTokens[29] = TokenTable.op_not;
		arrayOfPotentialTokens[30] = TokenTable.fc_loop_1;
		arrayOfPotentialTokens[31] = TokenTable.fc_loop_2;
		arrayOfPotentialTokens[32] = TokenTable.fc_loop_3;
		arrayOfPotentialTokens[33] = TokenTable.fc_loop_end;
		arrayOfPotentialTokens[34] = TokenTable.fc_if;
		arrayOfPotentialTokens[35] = TokenTable.fc_then_1;
		arrayOfPotentialTokens[36] = TokenTable.fc_then_2;
		arrayOfPotentialTokens[37] = TokenTable.fc_else_if;
		arrayOfPotentialTokens[38] = TokenTable.fc_else_1;
		arrayOfPotentialTokens[39] = TokenTable.fc_else_2;
		arrayOfPotentialTokens[40] = TokenTable.fc_if_end;
		arrayOfPotentialTokens[41] = TokenTable.function_1;
		arrayOfPotentialTokens[42] = TokenTable.function_2;
		arrayOfPotentialTokens[43] = TokenTable.function_end_1;
		arrayOfPotentialTokens[44] = TokenTable.function_end_2;
		arrayOfPotentialTokens[45] = TokenTable.function_end_3;
		arrayOfPotentialTokens[46] = TokenTable.function_end_4;
		arrayOfPotentialTokens[47] = TokenTable.whitespace;
		arrayOfPotentialTokens[48] = TokenTable.comment_2;
		arrayOfPotentialTokens[49] = TokenTable.comment_3;
		arrayOfPotentialTokens[50] = TokenTable.type_char;
		arrayOfPotentialTokens[51] = TokenTable.type_boolarray;
		arrayOfPotentialTokens[52] = TokenTable.type_intarray;
		arrayOfPotentialTokens[53] = TokenTable.struct_begin;
		arrayOfPotentialTokens[54] = TokenTable.struct_end;
	}
	
	/**
	 * Checks whether a given char array (token[]) is a 
	 * string, an integer, a keyword or an identifier token.
	 * 
	 * The token[] is compared to the keyword tokens in the 
	 * arrayOfPotentialTokens. If it doesn't match, the 
	 * array element will be set to null.
	 * Before comparing it to the keyword tokens, we check whether
	 * it starts with a quote " or with a number. If it starts with
	 * a quote, we identify it as a string, if it starts with a number,
	 * we identify it as an integer. If none of both matches,
	 * we search for a keyword token.
	 * 
	 * A counter counts how many elements are left in the 
	 * arrayOfPotentialTokens. If the counter is 1 at the end, 
	 * we found a token, if it is = 0, we found an identifier if 
	 * it is > 1, we found more than one keyword tokens.
	 * 
	 * If more than one tokens are found, the method returns null,
	 * forcing the caller to read another character and rerun the
	 * lookupToken() method.
	 * 
	 * @param token
	 * @param pos
	 * @return
	 * @throws EOFException 
	 */
	public Token lookupToken(char token[], int pos) throws EOFException {
		int i = 0;
		Token retToken = null;
		if(token[0] == '\0') {
			throw new EOFException();
		} else if (token[0] == '\"') {
			// found a string
			return TokenTable.string;
		} else if(token[0] >= '0' && token[0] <= '9') {
			// found an integer
			return TokenTable.integer;
		}
		while(potToken_count > 0) {
			// searching for a keyword
			Token t = arrayOfPotentialTokens[i];
			if(t != null) {
				if((token[pos] == '\0' && pos == t.getTokenID().length)
						|| (pos < t.getTokenID().length && token[pos] == t.getTokenID()[pos])) {
					// found a matching keyword. Temporarily 
					// storing it in the retToken.
					retToken = arrayOfPotentialTokens[i];
				} else {
					// current keyword token doesn't match ->
					// set element in array to null and decrease 
					// element counter
					arrayOfPotentialTokens[i] = null;
					potToken_count--;
				}
			}
			if(i < TokenTable.TOKEN_COUNT-1)
				// Didn't reach the end of the array yet ->
				// Increasing iterate counter
				i++;
			else {
				if(potToken_count == 1) {
					// Found a keyword token :)
					return retToken;
				} else if(potToken_count == 0) {
					// No keyword token found. 
					// Assuming that we found an identifier.
					return TokenTable.id;
				} else {
					// Found more than one token. 
					// Need more information to identify token.
					return null;
				}
			}
		}
		// Found more than one token. 
		// Need more information to identify token.
		return null;
	}
}
