/*
 * Parser.java Copyright (C) 2010 Stephanie Stroka 
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
package catpiler.frontend.parser;

import static org.junit.Assert.fail;

import org.junit.Assert;

import catpiler.frontend.exception.ParseException;
import catpiler.frontend.parser.symboltable.Symboltable;
import catpiler.frontend.scanner.Scanner;
import catpiler.frontend.scanner.keywords.A;
import catpiler.frontend.scanner.keywords.ALL;
import catpiler.frontend.scanner.keywords.AN;
import catpiler.frontend.scanner.keywords.ANY;
import catpiler.frontend.scanner.keywords.BIGGR;
import catpiler.frontend.scanner.keywords.BOTH;
import catpiler.frontend.scanner.keywords.CAN;
import catpiler.frontend.scanner.keywords.CHAR;
import catpiler.frontend.scanner.keywords.CHARZ;
import catpiler.frontend.scanner.keywords.DIFF;
import catpiler.frontend.scanner.keywords.DIFFRINT;
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
import catpiler.frontend.scanner.keywords.Identifier;
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
import catpiler.frontend.scanner.keywords.String;
import catpiler.frontend.scanner.keywords.THATSIT;
import catpiler.frontend.scanner.keywords.TIL;
import catpiler.frontend.scanner.keywords.TROOF;
import catpiler.frontend.scanner.keywords.TROOFZ;
import catpiler.frontend.scanner.keywords.WAI;
import catpiler.frontend.scanner.keywords.WILE;
import catpiler.frontend.scanner.keywords.WIN;
import catpiler.frontend.scanner.keywords.YA;
import catpiler.frontend.scanner.keywords.YOU;
import catpiler.frontend.scanner.keywords.YR;

/**
 * The Parser checks whether the program follows
 * the specified EBNF.
 * This is a recursive descendant parser, which means that
 * it starts with the non-terminal start symbol and works
 * his way recursively down to the terminal symbols of the 
 * program.
 * 
 * @author Stephanie Stroka
 * 			(stephanie.stroka@sbg.ac.at)
 *
 */
public class Parser {
	
	// junit test need scanner to be public
	public Scanner s = null;
	
	int tmpSrcPointer = 0;
	
	public boolean parseTest = false;

	private Symboltable symboltable;
	
	private boolean error;
	
	public Parser() {
		error = false;
	}
	
	private void markError(java.lang.String str) {
		error = true;
		System.out.println("Found an error at line " + s.getLineCount() + ": " + str);
	}
	
	/**
	 * Checks whether the keyword-token indicates the start of
	 * a module-call <<CAN HAS identifier>>
	 * 
	 * @param keyword
	 * @return
	 * @throws ParseException
	 */
	public boolean isModuleImport(Keyword keyword) throws ParseException {
		if(keyword instanceof CAN) {
			if(lookAhead() instanceof HAS) {
				adjustSrcPointer();
				if(lookAhead() instanceof Identifier) {
					adjustSrcPointer();
				} else {
					markError("Missing module");
				}
			} else {
				markError("Missing 'HAS' keyword");
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks whether the keyword-token indicates the start of 
	 * a structure definition <<STUFF label ... THATSIT>>
	 * 
	 * @param keyword
	 * @return
	 * @throws ParseException
	 */
	public boolean isStruct(Keyword keyword) throws ParseException {
		if(keyword instanceof STUFF) {
			if(!isIdentifier(lookAhead())) {
				markError("Missing structure label after 'STUFF'");
			} else {
				adjustSrcPointer();
				// TODO add it to the list of types
			}
			Keyword next = null;
			while(isVarInit(next = s.lookupToken()) || 
					isVarDecl(next) || 
					isVarAssign(next));
			if(!(next instanceof THATSIT)) {
				markError("Missing 'THATSIT' keyword");
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks whether the keyword-token indicates the start 
	 * of a variable initialization <<I HAS A identifier>>
	 * 
	 * @param keyword
	 * @return
	 * @throws ParseException
	 */
	public boolean isVarInit(Keyword keyword) throws ParseException {
		if(keyword instanceof I) {
			if(!(lookAhead() instanceof HAS)) {
				markError("Missing 'HAS' keyword");
			} else {
				adjustSrcPointer();
			}
			if(!(lookAhead() instanceof A)) {
				markError("Missing 'A' keyword");
			} else {
				adjustSrcPointer();
			}
			Keyword id = lookAhead();
			if(!(id instanceof Identifier)) {
				markError("Missing variable");
			} else {
				adjustSrcPointer();
				if(symboltable == null)
					symboltable = new Symboltable(null);
				id.setAttribute("0");
				((Identifier) id).setType(NOOB.tokenId);
				symboltable.put((Identifier) id);
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks whether the whole program follows
	 * the specified EBNF structure
	 * The program may either start with a module-import,
	 * a structure definition or directly with the main program.
	 * 
	 * @param keyword
	 * @return
	 * @throws ParseException
	 */
	public boolean isProgram(Keyword keyword) throws ParseException {
		Keyword k = keyword;
		while(isModuleImport(k)) {
			k = s.lookupToken();
		}
		while(isStruct(k)) {
			k = s.lookupToken();
		}
		// TODO: Modules don't need a main method,
		//       figure out how to handle them (separate method)
		if(isMain(k)) {
			while(isFunction(s.lookupToken()));
			return true;
		} else {
			throw new ParseException();
		}
	}
	
	/**
	 * Checks the consistency of the main part of the
	 * program <<HAI ... KTHXBYE>>
	 * 
	 * @param keyword
	 * @return
	 * @throws SyntaxException
	 * @throws ParseException
	 */
	public boolean isMain(Keyword keyword) throws ParseException {
		if(keyword instanceof HAI) {
			symboltable = new Symboltable(null);
			Keyword next = null;
			while(isStatement(next = s.lookupToken()));
			if(!(next instanceof KTHXBYE)) {
				markError("Missing 'KTHXBYE' keyword");
			}
			symboltable = null;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks whether the keyword-token indicates the 
	 * start of a statement.
	 * 
	 * @param keyword
	 * @return
	 * @throws SyntaxException
	 * @throws ParseException
	 */
	public boolean isStatement(Keyword keyword) throws ParseException {
		if(isVarInit(keyword)) {
			return true;
		} else if(isVarDecl(keyword)) {
			return true;
		} else if(isVarAssign(keyword)) {
			return true;
		} else if(isFlowControl(keyword)) {
			return true;
		} else if(isOperation(keyword)) {
			return true;
		} else if(isFuncCall(keyword)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks whether the given keyword-token indicates 
	 * a function call
	 * 
	 * @param keyword
	 * @return
	 * @throws SyntaxException
	 * @throws ParseException
	 */
	public boolean isFuncCall(Keyword keyword) throws ParseException {
		if(isIdentifier(keyword)) {
			Keyword k = s.lookupToken();
			while(isExpr(k)) {
				k = s.lookupToken();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks whether the keyword indicates the start 
	 * of an expression
	 * 
	 * @param keyword
	 * @return
	 * @throws SyntaxException
	 * @throws ParseException
	 */
	public boolean isExpr(Keyword keyword) throws ParseException {
		return (isBiExpr(keyword) || isInfExpr(keyword) );
				//|| isBool(keyword) || isIdentifier(keyword));
	}

	/**
	 * Checks whether the keyword indicates the start
	 * of a boolean operation 
	 * <<BOTH OF | EITHER OF expr AN expr>>
	 * @param keyword
	 * @return
	 * @throws SyntaxException
	 * @throws ParseException
	 */
	public boolean isBoolOp(Keyword keyword) throws ParseException {
		if(keyword instanceof BOTH || keyword instanceof EITHER) {
			if(!(lookAhead() instanceof OF)) {
				if(keyword instanceof BOTH) {
					return false;
				} else {
					markError("Missing 'OF' keyword");
				}
			} else {
				adjustSrcPointer();
			}
			if(!isExpr(s.lookupToken())) {
				markError("Expected Expression");
			}
			if(!(lookAhead() instanceof AN)) {
				markError("Missing 'AN' keyword");
			} else {
				adjustSrcPointer();
			}
			if(!isExpr(s.lookupToken())) {
				markError("Expected expression");
			}
			
			return true;
		} else if(keyword instanceof NOT) {
			if(!isExpr(s.lookupToken())) {
				markError("Expected expression");
			}
			return true;
		} else if(isBool(keyword)) {
			return true;
		} else if(isIdentifier(keyword)) {
			if(((Identifier)keyword).getType() != null &&
					((Identifier)keyword).getType().equals("TROOF") || parseTest) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isGenExpr(Keyword keyword) throws ParseException {
		Keyword lookahead = lookAhead();
		if(keyword instanceof DIFFRINT || 
			(keyword instanceof BOTH && lookahead instanceof SAEM) ) {
			if(keyword instanceof BOTH) {
				adjustSrcPointer();
			}
			if(!isOperation(s.lookupToken())) {
				markError("Expected a value to compare");
			}
			
			if(!(lookAhead() instanceof AN)) {
				markError("Missing 'AN' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!isOperation(s.lookupToken())) {
				markError("Expected a value to compare");
			}
			
			return true;
		} else {
			if(keyword instanceof BOTH && 
							!(lookAhead() instanceof SAEM || 
									lookAhead() instanceof OF)) {
				markError("Missing 'SAEM' OR 'OF' keyword");
			}
			return false;
		}
	}

	public boolean isBool(Keyword keyword) {
		if(keyword instanceof WIN || keyword instanceof FAIL) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isInfExpr(Keyword keyword) throws ParseException {
		if(keyword instanceof ALL || keyword instanceof ANY) {
			
			if(!(lookAhead() instanceof OF)) {
				markError("Missing 'OF' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!isBoolOp(s.lookupToken())) {
				markError("Expected boolean variable");
			}
			
			if(!(lookAhead() instanceof AN)) {
				markError("Missing 'AN' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!(isBoolOp(s.lookupToken()))) {
				markError("Too few arguments");
			}
			
			Keyword lookupTok = lookAhead();
			while(!(lookupTok instanceof MKAY)) {
				if(!(lookupTok instanceof AN)) {
					markError("Missing 'AN' or 'MKAY' keyword");
					break;
				} else {
					adjustSrcPointer();
					if(!isBoolOp(s.lookupToken())) {
						markError("Expected boolean variable");
					}
					lookupTok = lookAhead();
				}
			}
			if(lookupTok instanceof MKAY) {
				adjustSrcPointer();
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean isBiExpr(Keyword keyword) throws ParseException {
		if(isBoolOp(keyword) || isGenExpr(keyword)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isFlowControl(Keyword keyword) throws ParseException {
		return (isIf(keyword) || isLoop(keyword));
	}

	public boolean isLoop(Keyword keyword) throws ParseException {
		java.lang.String loopLabel = null;
		if(keyword instanceof IM) {
			symboltable = new Symboltable(symboltable);
			
			if(!(lookAhead() instanceof IN)) {
				return false;
			} else {
				adjustSrcPointer();
			}
			
			if(!(lookAhead() instanceof YR)) {
				markError("Missing 'YR' keyword");
			} else {
				adjustSrcPointer();
			}
			
			Keyword id_loop = lookAhead();
			if(!(id_loop instanceof Identifier)) {
				markError("Loop does not have a name");
				// TODO search for loop end
			} else {
				adjustSrcPointer();
				loopLabel = id_loop.getAttribute();
			}
			
			Keyword tok = s.lookupToken();
			if(tok instanceof YR) {
				if(!(lookAhead() instanceof Identifier)) {
					markError("Too few arguments");
				} else {
					adjustSrcPointer();
				}
				tok = s.lookupToken();
			}
			
			if(tok instanceof WILE || tok instanceof TIL) {
				if(!isExpr(s.lookupToken())) {
					markError("No limiting expression provided");
				}
				tok = s.lookupToken();
			}
			
			while(isStatement(tok))
				tok = s.lookupToken();
					
			if(!(tok instanceof IM)) {
				markError("Expected loop end");
			}
			
			if(!(lookAhead() instanceof OUTTA)) {
				markError("Missing 'OUTTA' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!(lookAhead() instanceof YR)) {
				markError("Missing 'YR' label");
			} else {
				adjustSrcPointer();
			}
			
			Keyword id = lookAhead();
			if(!(id instanceof Identifier)) {
				markError("No loop end label provided");
			} else {
				adjustSrcPointer();
			}
			
			if(!(loopLabel.equals(id.getAttribute()))) {
				markError("Loop end label does " +
					"not match the loop begin label");
			}
			symboltable = symboltable.getOutterTable();
			
			return true;
		} else {
			return false;
		}
	}

	public boolean isIf(Keyword keyword) throws ParseException {
		if(isExpr(keyword)) {
			if(lookAhead() instanceof ORLY) {
				adjustSrcPointer();
				symboltable = new Symboltable(symboltable);
				
				if(!(lookAhead() instanceof YA)) {
					markError("Missing 'YA' keyword");
				} else {
					adjustSrcPointer();
				}
				
				if(!(lookAhead() instanceof RLY)) {
					markError("Missing 'RLY' keyword");
				} else {
					adjustSrcPointer();
				}
				
				Keyword k = null;
				if(!isStatement(k = s.lookupToken())) {
					markError("Expected statement after condition");
				}
				
				while(isStatement(k = s.lookupToken()));
				
				while(k instanceof MEBBE) {
					if(!isExpr(s.lookupToken())) {
						markError("Expected expression after 'MEBBE'");
					}
					
					if(!isStatement(k = s.lookupToken())) {
						markError("Expected statement after condition");
					}
					
					while(isStatement(k = s.lookupToken()));
				}
				if(k instanceof NO) {
					if(!(lookAhead() instanceof WAI)) {
						markError("Missing 'WAI' keyword");
					} else {
						adjustSrcPointer();
					}
					
					if(!isStatement(k = s.lookupToken())) {
						markError("Expected statement after condition");
					}
					
					while(isStatement(k = s.lookupToken()));
				}
				if(!(k instanceof OIC)) {
					markError("Missing 'OIC' keyword");
				}
				symboltable = symboltable.getOutterTable();
				
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isOperation(Keyword keyword) throws ParseException {
		return isNumOp(keyword) || isBoolOp(keyword) || isStringOp(keyword);
	}

	public boolean isStringOp(Keyword keyword) {
		if(keyword instanceof String) {
			return true;
		} else if(keyword instanceof Identifier && 
				(((Identifier)keyword).getType() != null &&
				((Identifier)keyword).getType().equals("CHARZ") || parseTest)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isNumOp(Keyword keyword) throws ParseException {
		if(keyword instanceof SUM || keyword instanceof DIFF ||
				keyword instanceof QUOSHUNT || keyword instanceof PRODUKT ||
				keyword instanceof BIGGR || keyword instanceof SMALLR) {
			
			if(!(lookAhead() instanceof OF)) {
				markError("Missing 'OF' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!isNumOp(s.lookupToken())) {
				markError("Expected numerical operation or number");
			}
			
			if(!(lookAhead() instanceof AN)) {
				markError("Missing 'AN' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!isNumOp(s.lookupToken())) {
				markError("Expected numerical operation or number");
			}
			return true;
		} else if(isNum(keyword)) {
			return true;
		} else if(isIdentifier(keyword)) {
			if(((Identifier) keyword).getType() != null && 
					((Identifier) keyword).getType().equals("NUMBR") || 
					parseTest) {
						return true;
					} else {
						return false;
					}
		} else {
			return false;
		}
	}

	public boolean isNum(Keyword keyword) {
		return keyword instanceof Int;
	}
	
	public boolean isVarAssign(Keyword keyword) throws ParseException {
		if(isIdentifier(keyword)) {
			Keyword k = lookAhead();
			if(k instanceof R) {
				adjustSrcPointer();
				if(!isOperation(s.lookupToken())) {
					markError("Expected value");
				}
				return true;
			} else {
				markError("Missing 'R' or 'IS NOW A' keywords after variable");
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isVarDecl(Keyword keyword) throws ParseException {
		if(isIdentifier(keyword)) {
			
			if(lookAhead() instanceof IS) {
				adjustSrcPointer();
				
				if(!(lookAhead() instanceof NOW)) {
					markError("Missing 'NOW' keyword");
				} else {
					adjustSrcPointer();
				}
				
				if(!(lookAhead() instanceof A)) {
					markError("Missing 'A' keyword");
				} else {
					adjustSrcPointer();
				}
				
				Keyword typeKeyword = s.lookupToken();
				if(isType(typeKeyword)) {
					((Identifier) keyword).setType(typeKeyword.getTokenID());
				} else {
					markError("Expected a type");
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isType(Keyword keyword) {
		return (keyword instanceof CHAR ||
				keyword instanceof NUMBR ||
				keyword instanceof TROOF ||
				keyword instanceof CHARZ ||
				keyword instanceof NUMBRZ ||
				keyword instanceof TROOFZ);
	}

	public boolean isIdentifier(Keyword keyword) {
		return keyword instanceof Identifier;
	}
	
	public boolean isFunction(Keyword keyword) throws ParseException {
		if(keyword instanceof HOW) {
			
			symboltable = new Symboltable(null);
			
			if(!(lookAhead() instanceof DUZ)) {
				markError("Missing 'DUZ' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!(lookAhead() instanceof I)) {
				markError("Missing 'I' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!(lookAhead() instanceof Identifier)) {
				markError("Expected function label");
			} else {
				adjustSrcPointer();
			}
			
			Keyword tok = lookAhead();
			if(tok instanceof YR) {
				adjustSrcPointer();
				if(!(lookAhead() instanceof Identifier)) {
					markError("Expected another argument");
				} else {
					adjustSrcPointer();
				}
				
				while((tok = lookAhead()) instanceof AN) {
					adjustSrcPointer();
					if(!(lookAhead() instanceof YR)) {
						markError("Missing 'YR' keyword");
					} else {
						adjustSrcPointer();
					}
					if(!(lookAhead() instanceof Identifier)) {
						markError("Expected another argument");
					} else {
						adjustSrcPointer();
					}
				}
			}
			
			adjustSrcPointer();
			while(!(tok instanceof IF)) {
				if(!(isStatement(tok) 
						|| (tok instanceof GTFO)
						|| (tok instanceof FOUND))) {
					markError("Expected statement");
				} else if(tok instanceof FOUND) {
					if(!(lookAhead() instanceof YR)) {
						markError("Missing 'YR' keyword");
					} else {
						adjustSrcPointer();
					}
					
					if(!isExpr(s.lookupToken())) {
						markError("Expected return value");
					}
				}
				tok = s.lookupToken();
			}
			
			if(!(lookAhead() instanceof YOU)) {
				markError("Missing 'YOU' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!(lookAhead() instanceof SAY)) {
				markError("Missing 'SAY' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!(lookAhead() instanceof SO)) {
				markError("Missing 'SO' keyword");
			} else {
				adjustSrcPointer();
			}
			
			symboltable = symboltable.getOutterTable();
			
			return true;
		} else {
			return false;
		}
		
	}
	
	public Keyword lookAhead() {
		Keyword nextToken = s.lookupToken();
		int i = s.getPointerBeforeToken();
		tmpSrcPointer = s.getSrcPointer();
		s.setSrcPointer(i);
		return nextToken;
	}
	
	public void adjustSrcPointer() {
		s.setSrcPointer(tmpSrcPointer);
	}

	public void parse() throws ParseException {
		s = new Scanner("" +
				"CAN HAS fileId " +
				"HAI " +
				"I HAS A var1 " +
				"I HAS A var2 " +
				"var1 IS NOW A NUMBR " +
				"var2 IS NOW A NUMBR " +
				"var1 R 3 " +
				"var2 R 2 " +
				"SUM OF var1 AN var2 " +
				"KTHXBYE");

		isProgram(s.lookupToken());
	}
	
	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public static void main(java.lang.String[] args) {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("" +
				"HAI " +
				"I HAS A var1 " +
				"I HAS A var2 " +
				"var1 IS NOW A NUMBR " +
				"var2 IS NOW A NUMBR " +
				"var1 R 3 " +
				"var2 R 2 " +
				"var3 R WIN " +
				"var4 R FAIL " +
				"SUM OF var1 AN var2 " +
				"EITHER OF var3 AN var4 " +
				"ORLY? " +
				"YA RLY " +
				"    PRODUKT OF var2 AN var1 " +
				"OIC " +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
}
