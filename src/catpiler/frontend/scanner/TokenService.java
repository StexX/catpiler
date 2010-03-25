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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import catpiler.frontend.exception.NoTokenFoundException;
import catpiler.frontend.scanner.keywords.A;
import catpiler.frontend.scanner.keywords.ALL;
import catpiler.frontend.scanner.keywords.AN;
import catpiler.frontend.scanner.keywords.ANY;
import catpiler.frontend.scanner.keywords.BIGGR;
import catpiler.frontend.scanner.keywords.BOTH;
import catpiler.frontend.scanner.keywords.BTW;
import catpiler.frontend.scanner.keywords.CHAR;
import catpiler.frontend.scanner.keywords.CHARZ;
import catpiler.frontend.scanner.keywords.DIFF;
import catpiler.frontend.scanner.keywords.DIFFRINT;
import catpiler.frontend.scanner.keywords.DOWANT;
import catpiler.frontend.scanner.keywords.DUZ;
import catpiler.frontend.scanner.keywords.EITHER;
import catpiler.frontend.scanner.keywords.FAIL;
import catpiler.frontend.scanner.keywords.HAI;
import catpiler.frontend.scanner.keywords.HAS;
import catpiler.frontend.scanner.keywords.HOW;
import catpiler.frontend.scanner.keywords.I;
import catpiler.frontend.scanner.keywords.IF;
import catpiler.frontend.scanner.keywords.IM;
import catpiler.frontend.scanner.keywords.IN;
import catpiler.frontend.scanner.keywords.Int;
import catpiler.frontend.scanner.keywords.KTHXBYE;
import catpiler.frontend.scanner.keywords.Keyword;
import catpiler.frontend.scanner.keywords.LF;
import catpiler.frontend.scanner.keywords.MEBBE;
import catpiler.frontend.scanner.keywords.MKAY;
import catpiler.frontend.scanner.keywords.NO;
import catpiler.frontend.scanner.keywords.NOT;
import catpiler.frontend.scanner.keywords.NUMBR;
import catpiler.frontend.scanner.keywords.NUMBRZ;
import catpiler.frontend.scanner.keywords.OBTW;
import catpiler.frontend.scanner.keywords.OF;
import catpiler.frontend.scanner.keywords.OIC;
import catpiler.frontend.scanner.keywords.ORLY;
import catpiler.frontend.scanner.keywords.OUTTA;
import catpiler.frontend.scanner.keywords.PRODUKT;
import catpiler.frontend.scanner.keywords.QUOSHUNT;
import catpiler.frontend.scanner.keywords.QUOTE;
import catpiler.frontend.scanner.keywords.R;
import catpiler.frontend.scanner.keywords.RLY;
import catpiler.frontend.scanner.keywords.SAEM;
import catpiler.frontend.scanner.keywords.SAY;
import catpiler.frontend.scanner.keywords.SMALLR;
import catpiler.frontend.scanner.keywords.SO;
import catpiler.frontend.scanner.keywords.STUFF;
import catpiler.frontend.scanner.keywords.SUM;
import catpiler.frontend.scanner.keywords.THATSIT;
import catpiler.frontend.scanner.keywords.TLDR;
import catpiler.frontend.scanner.keywords.TROOF;
import catpiler.frontend.scanner.keywords.TROOFZ;
import catpiler.frontend.scanner.keywords.WAI;
import catpiler.frontend.scanner.keywords.WIN;
import catpiler.frontend.scanner.keywords.YA;
import catpiler.frontend.scanner.keywords.YOU;
import catpiler.frontend.scanner.keywords.YR;


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
	private Map<String,Class> mapOfPotentialTokens;

	/*
	 * counter for non-null elements in arrayOfPotentialTokens
	 */
	private int potToken_count;
	
	private int posArray;
	
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
		posArray = 0;
		
		mapOfPotentialTokens = new HashMap<String, Class>();
		mapOfPotentialTokens.put(BTW.tokenId,BTW.class);
		mapOfPotentialTokens.put(HAI.tokenId,HAI.class);
		mapOfPotentialTokens.put(KTHXBYE.tokenId,KTHXBYE.class);
		mapOfPotentialTokens.put(I.tokenId,I.class);
		mapOfPotentialTokens.put(HAS.tokenId,HAS.class);
		mapOfPotentialTokens.put(A.tokenId,A.class);
		mapOfPotentialTokens.put(R.tokenId,R.class);
		mapOfPotentialTokens.put(CHARZ.tokenId,CHARZ.class);
		mapOfPotentialTokens.put(TROOF.tokenId,TROOF.class);
		mapOfPotentialTokens.put(NUMBR.tokenId,NUMBR.class);
		mapOfPotentialTokens.put(WIN.tokenId,WIN.class);
		mapOfPotentialTokens.put(FAIL.tokenId,FAIL.class);
		mapOfPotentialTokens.put(LF.tokenId,LF.class);
		mapOfPotentialTokens.put(QUOTE.tokenId,QUOTE.class);
		mapOfPotentialTokens.put(OF.tokenId,OF.class);
		mapOfPotentialTokens.put(AN.tokenId,AN.class);
		mapOfPotentialTokens.put(SUM.tokenId,SUM.class);
		mapOfPotentialTokens.put(DIFF.tokenId,DIFF.class);
		mapOfPotentialTokens.put(PRODUKT.tokenId,PRODUKT.class);
		mapOfPotentialTokens.put(QUOSHUNT.tokenId,QUOSHUNT.class);
		mapOfPotentialTokens.put(BIGGR.tokenId,BIGGR.class);
		mapOfPotentialTokens.put(SMALLR.tokenId,SMALLR.class);
		mapOfPotentialTokens.put(BOTH.tokenId,BOTH.class);
		mapOfPotentialTokens.put(EITHER.tokenId,EITHER.class);
		mapOfPotentialTokens.put(ALL.tokenId,ALL.class);
		mapOfPotentialTokens.put(ANY.tokenId,ANY.class);
		mapOfPotentialTokens.put(MKAY.tokenId,MKAY.class);
		mapOfPotentialTokens.put(SAEM.tokenId,SAEM.class);
		mapOfPotentialTokens.put(DIFFRINT.tokenId,DIFFRINT.class);
		mapOfPotentialTokens.put(NOT.tokenId,NOT.class);
		mapOfPotentialTokens.put(IM.tokenId,IM.class);
		mapOfPotentialTokens.put(IN.tokenId,IN.class);
		mapOfPotentialTokens.put(YR.tokenId,YR.class);
		mapOfPotentialTokens.put(OUTTA.tokenId,OUTTA.class);
		mapOfPotentialTokens.put(ORLY.tokenId,ORLY.class);
		mapOfPotentialTokens.put(YA.tokenId,YA.class);
		mapOfPotentialTokens.put(RLY.tokenId,RLY.class);
		mapOfPotentialTokens.put(MEBBE.tokenId,MEBBE.class);
		mapOfPotentialTokens.put(NO.tokenId,NO.class);
		mapOfPotentialTokens.put(WAI.tokenId,WAI.class);
		mapOfPotentialTokens.put(OIC.tokenId,OIC.class);
		mapOfPotentialTokens.put(HOW.tokenId,HOW.class);
		mapOfPotentialTokens.put(DUZ.tokenId,DUZ.class);
		mapOfPotentialTokens.put(IF.tokenId,IF.class);
		mapOfPotentialTokens.put(YOU.tokenId,YOU.class);
		mapOfPotentialTokens.put(SAY.tokenId,SAY.class);
		mapOfPotentialTokens.put(SO.tokenId,SO.class);
		mapOfPotentialTokens.put(OBTW.tokenId,OBTW.class);
		mapOfPotentialTokens.put(TLDR.tokenId,TLDR.class);
		mapOfPotentialTokens.put(CHAR.tokenId,CHAR.class);
		mapOfPotentialTokens.put(TROOFZ.tokenId,TROOFZ.class);
		mapOfPotentialTokens.put(NUMBRZ.tokenId,NUMBRZ.class);
		mapOfPotentialTokens.put(STUFF.tokenId,STUFF.class);
		mapOfPotentialTokens.put(THATSIT.tokenId,THATSIT.class);
		mapOfPotentialTokens.put(DOWANT.tokenId,DOWANT.class);
		
		potToken_count = mapOfPotentialTokens.size();
	}
	
	/**
	 * Checks whether a given char array (token[]) is a 
	 * string, an integer, a keyword or an identifier token.
	 * 
	 * The token[] is compared to the keyword tokens in the 
	 * mapOfPotentialTokens. If it doesn't match, the 
	 * array element will be set to null.
	 * Before comparing it to the keyword tokens, we check whether
	 * it starts with a quote " or with a number. If it starts with
	 * a quote, we identify it as a string, if it starts with a number,
	 * we identify it as an integer. If none of both matches,
	 * we search for a keyword token.
	 * 
	 * A counter counts how many elements are left in the 
	 * mapOfPotentialTokens. If the counter is 1 at the end, 
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
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
//	public String lookupToken(char token[], int pos) throws EOFException {
//		int i = 0;
//		String retToken = null;
//		if(token[0] == '\0') {
//			throw new EOFException();
//		} else if (token[0] == '\"') {
//			// found a string
//			return TokenTable.string;
//		} else if(token[0] >= '0' && token[0] <= '9') {
//			// found an integer
//			return TokenTable.integer;
//		}
//		while(potToken_count > 0) {
//			// searching for a keyword
//			String t = mapOfPotentialTokens[i];
//			if(t != null) {
//				if((token[pos] == '\0' && pos == t.length())
//						|| (pos < t.length() && token[pos] == t.charAt(pos))) {
//					// found a matching keyword. Temporarily 
//					// storing it in the retToken.
//					retToken = mapOfPotentialTokens[i];
//				} else {
//					// current keyword token doesn't match ->
//					// set element in array to null and decrease 
//					// element counter
//					mapOfPotentialTokens[i] = null;
//					potToken_count--;
//				}
//			}
//			if(i < TokenTable.TOKEN_COUNT-1)
//				// Didn't reach the end of the array yet ->
//				// Increasing iterate counter
//				i++;
//			else {
//				if(potToken_count == 1) {
//					// Found a keyword token :)
//					return retToken;
//				} else if(potToken_count == 0) {
//					// No keyword token found. 
//					// Assuming that we found an identifier.
//					return TokenTable.id;
//				} else {
//					// Found more than one token. 
//					// Need more information to identify token.
//					return null;
//				}
//			}
//		}
//		// Found more than one token. 
//		// Need more information to identify token.
//		return null;
//	}
	
	public Keyword returnFirstMatchinToken(char token[], int pos) throws EOFException, NoTokenFoundException, InstantiationException, IllegalAccessException {
		if(token[0] == '\0') {
			throw new EOFException();
		} else if (token[0] == '\"') {
			// found a string
			return new catpiler.frontend.scanner.keywords.String();
		} else if(token[0] >= '0' && token[0] <= '9') {
			// found an integer
			return new Int();
		}
		while(mapOfPotentialTokens.size() > 0) {
			Iterator<String> it = mapOfPotentialTokens.keySet().iterator();
			// searching for a keyword
			String t = it.next();
			if(t != null) {
				if((token[pos] == '\0' && pos == t.length())
						|| (pos < t.length() && token[pos] == t.charAt(pos))) {
					// found a matching keyword.
					Keyword ret = (Keyword) mapOfPotentialTokens.get(t).newInstance();
					mapOfPotentialTokens.remove(t);
					return ret;
				} 
				mapOfPotentialTokens.remove(t);
			}
		}
		// Found more than one token. 
		// Need more information to identify token.
		return null;
	}
	
}
