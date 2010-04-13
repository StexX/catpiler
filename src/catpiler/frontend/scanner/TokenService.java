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
import catpiler.frontend.scanner.keywords.CAN;
import catpiler.frontend.scanner.keywords.CHAR;
import catpiler.frontend.scanner.keywords.CHARZ;
import catpiler.frontend.scanner.keywords.DIFF;
import catpiler.frontend.scanner.keywords.DIFFRINT;
import catpiler.frontend.scanner.keywords.DOWANT;
import catpiler.frontend.scanner.keywords.DUZ;
import catpiler.frontend.scanner.keywords.EITHER;
import catpiler.frontend.scanner.keywords.FAIL;
import catpiler.frontend.scanner.keywords.FOUND;
import catpiler.frontend.scanner.keywords.GTFO;
import catpiler.frontend.scanner.keywords.HAI;
import catpiler.frontend.scanner.keywords.HAS;
import catpiler.frontend.scanner.keywords.HOW;
import catpiler.frontend.scanner.keywords.I;
import catpiler.frontend.scanner.keywords.IF;
import catpiler.frontend.scanner.keywords.IM;
import catpiler.frontend.scanner.keywords.IN;
import catpiler.frontend.scanner.keywords.IS;
import catpiler.frontend.scanner.keywords.Int;
import catpiler.frontend.scanner.keywords.KTHXBYE;
import catpiler.frontend.scanner.keywords.Keyword;
import catpiler.frontend.scanner.keywords.MEBBE;
import catpiler.frontend.scanner.keywords.MKAY;
import catpiler.frontend.scanner.keywords.NO;
import catpiler.frontend.scanner.keywords.NOOB;
import catpiler.frontend.scanner.keywords.NOT;
import catpiler.frontend.scanner.keywords.NOW;
import catpiler.frontend.scanner.keywords.NUMBR;
import catpiler.frontend.scanner.keywords.NUMBRZ;
import catpiler.frontend.scanner.keywords.OBTW;
import catpiler.frontend.scanner.keywords.OF;
import catpiler.frontend.scanner.keywords.OIC;
import catpiler.frontend.scanner.keywords.ORLY;
import catpiler.frontend.scanner.keywords.OUTTA;
import catpiler.frontend.scanner.keywords.PRODUKT;
import catpiler.frontend.scanner.keywords.QUOSHUNT;
import catpiler.frontend.scanner.keywords.R;
import catpiler.frontend.scanner.keywords.RLY;
import catpiler.frontend.scanner.keywords.SAEM;
import catpiler.frontend.scanner.keywords.SAY;
import catpiler.frontend.scanner.keywords.SMALLR;
import catpiler.frontend.scanner.keywords.SO;
import catpiler.frontend.scanner.keywords.STUFF;
import catpiler.frontend.scanner.keywords.SUM;
import catpiler.frontend.scanner.keywords.THATSIT;
import catpiler.frontend.scanner.keywords.TIL;
import catpiler.frontend.scanner.keywords.TLDR;
import catpiler.frontend.scanner.keywords.TROOF;
import catpiler.frontend.scanner.keywords.TROOFZ;
import catpiler.frontend.scanner.keywords.WAI;
import catpiler.frontend.scanner.keywords.WILE;
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
		mapOfPotentialTokens.put(CAN.tokenId,CAN.class);
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
		mapOfPotentialTokens.put(NOW.tokenId,NOW.class);
		mapOfPotentialTokens.put(NOOB.tokenId,NOOB.class);
		mapOfPotentialTokens.put(FOUND.tokenId,FOUND.class);
		mapOfPotentialTokens.put(GTFO.tokenId,GTFO.class);
		mapOfPotentialTokens.put(WILE.tokenId,WILE.class);
		mapOfPotentialTokens.put(TIL.tokenId,TIL.class);
		mapOfPotentialTokens.put(IS.tokenId,IS.class);
		
		potToken_count = mapOfPotentialTokens.size();
	}
	
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
