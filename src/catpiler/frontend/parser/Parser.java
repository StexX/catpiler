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
package catpiler.frontend.parser;

import static org.junit.Assert.fail;

import org.junit.Assert;

import catpiler.frontend.exception.ParseException;
import catpiler.frontend.exception.SyntaxException;
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
import catpiler.frontend.scanner.keywords.TROOF;
import catpiler.frontend.scanner.keywords.TROOFZ;
import catpiler.frontend.scanner.keywords.TIL;
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

	/**
	 * Checks whether the keyword-token indicates the start of
	 * a module-call <<CAN HAS identifier>>
	 * 
	 * @param keyword
	 * @return
	 * @throws SyntaxException
	 * @throws ParseException
	 */
	public boolean isModule(Keyword keyword) throws SyntaxException, ParseException {
		if(keyword instanceof CAN) {
			if((keyword = s.lookupToken()) instanceof HAS) {
				if((keyword = s.lookupToken()) instanceof Identifier) {
					return true;
				} else {
					throw new ParseException();
				}
			} else {
				throw new ParseException();
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Checks whether the keyword-token indicates the start of 
	 * a structure definition <<STUFF ... THATSIT>>
	 * 
	 * @param keyword
	 * @return
	 * @throws SyntaxException
	 * @throws ParseException
	 */
	public boolean isStruct(Keyword keyword) throws SyntaxException, ParseException {
		if(keyword instanceof STUFF) {
			Keyword next = null;
			while(isVarInit(next = s.lookupToken()) || 
					isVarDecl(next) || 
					isVarAssign(next));
			if(next instanceof THATSIT) {
				return true;
			} else {
				return false;
			}
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
	 * @throws SyntaxException
	 * @throws ParseException
	 */
	public boolean isVarInit(Keyword keyword) throws SyntaxException, ParseException {
		if(keyword instanceof I) {
			if(s.lookupToken() instanceof HAS) {
				if(s.lookupToken() instanceof A) {
					if(s.lookupToken() instanceof Identifier) {
						return true;
					} else {
						throw new ParseException();
					}
				} else {
					throw new ParseException();
				}
			} else {
				throw new ParseException();
			}
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
	 * @throws SyntaxException
	 * @throws ParseException
	 */
	public boolean isProgram(Keyword keyword) throws SyntaxException, ParseException {
		Keyword k = keyword;
		while(isModule(k)) {
			k = s.lookupToken();
		}
		while(isStruct(k)) {
			k = s.lookupToken();
		}
		if(isMain(k)) {
			k = s.lookupToken();
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
	public boolean isMain(Keyword keyword) throws SyntaxException, ParseException {
		if(keyword instanceof HAI) {
			Keyword next = null;
			while(isStatement(next = s.lookupToken()));
			if(next instanceof KTHXBYE) {
				return true;
			} else {
				throw new ParseException();
			}
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
	public boolean isStatement(Keyword keyword) throws SyntaxException, ParseException {
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
	public boolean isFuncCall(Keyword keyword) throws SyntaxException, ParseException {
		if(isIdentifier(keyword)) {
			Keyword k = lookAhead();
			while(isExpr(k)) {
				s.setSrcPointer(tmpSrcPointer);
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
	public boolean isExpr(Keyword keyword) throws SyntaxException, ParseException {
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
	public boolean isBoolOp(Keyword keyword) throws SyntaxException, ParseException {
		if(keyword instanceof BOTH || keyword instanceof EITHER) {
			if(lookAhead() instanceof OF) {
				s.setSrcPointer(tmpSrcPointer);
				if(isExpr(s.lookupToken())) {
					if(s.lookupToken() instanceof AN) {
						if(isExpr(s.lookupToken())) {
							return true;
						} else {
							throw new ParseException();
						}
					} else {
						throw new ParseException();
					}
				} else {
					throw new ParseException();
				}
			} else {
				if(keyword instanceof BOTH) {
					return false;
				} else {
					throw new ParseException();
				}
			}
		} else if(keyword instanceof NOT) {
			if(isExpr(s.lookupToken())) {
				return true;
			} else {
				throw new ParseException();
			}
		} else if(isBool(keyword) || isIdentifier(keyword)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isGenExpr(Keyword keyword) throws SyntaxException, ParseException {
		Keyword lookahead = lookAhead();
		if(keyword instanceof DIFFRINT || 
			(keyword instanceof BOTH && lookahead instanceof SAEM) ) {
			if(keyword instanceof BOTH) {
				s.setSrcPointer(tmpSrcPointer);
			}
			if(isOperation(s.lookupToken())) {
				if(s.lookupToken() instanceof AN) {
					if(isOperation(s.lookupToken())) {
						return true;
					} else {
						throw new ParseException();
					}
				} else {
					throw new ParseException();
				}
			} else {
				throw new ParseException();
			}
		} else {
			if(keyword instanceof BOTH && 
							!(lookahead instanceof SAEM || 
									lookahead instanceof OF)) {
				s.setSrcPointer(tmpSrcPointer);
				throw new ParseException();
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

	public boolean isInfExpr(Keyword keyword) throws ParseException, SyntaxException {
		if(keyword instanceof ALL || keyword instanceof ANY) {
			if(s.lookupToken() instanceof OF) {
				if(isBoolOp(s.lookupToken())) {
					if(s.lookupToken() instanceof AN) {
						if(isBoolOp(s.lookupToken())) {
							Keyword lookupTok = s.lookupToken();
							while(!(lookupTok instanceof MKAY)) {
								if(!(lookupTok instanceof AN)) {
									throw new ParseException();
								}
								if(!isBoolOp(s.lookupToken())) {
									throw new ParseException();
								}
								lookupTok = s.lookupToken();
							}
							return true;
						} else {
							throw new ParseException();
						}
					} else {
						throw new ParseException();
					}
				} else {
					throw new ParseException();
				}
			} else {
				throw new ParseException();
			}
		} else {
			return false;
		}
	}

	public boolean isBiExpr(Keyword keyword) throws SyntaxException, ParseException {
		if(isBoolOp(keyword) || isGenExpr(keyword)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isFlowControl(Keyword keyword) throws SyntaxException, ParseException {
		return (isIf(keyword) || isLoop(keyword));
	}

	public boolean isLoop(Keyword keyword) throws SyntaxException, ParseException {
		java.lang.String loopLabel = null;
		if(keyword instanceof IM) {
			if(lookAhead() instanceof IN) {
				s.setSrcPointer(tmpSrcPointer);
				if(s.lookupToken() instanceof YR) {
					Keyword id_loop = null;
					if((id_loop = s.lookupToken()) instanceof Identifier) {
						loopLabel = id_loop.getAttribute();
					} else {
						throw new ParseException();
					}
					Keyword tok = null;
					if((tok = s.lookupToken()) instanceof YR) {
						if(!(s.lookupToken() instanceof Identifier)) {
							throw new ParseException();
						}
						tok = s.lookupToken();
					} 
					
					if(tok instanceof WILE || tok instanceof TIL) {
						if(!isExpr(s.lookupToken())) {
							throw new ParseException();
						}
					}
					
					tok = s.lookupToken();
					while(isStatement(tok)) 
						tok = s.lookupToken();
					
					if(tok instanceof IM) {
						if(s.lookupToken() instanceof OUTTA) {
							if(s.lookupToken() instanceof YR) {
								Keyword id = null;
								if((id = s.lookupToken()) instanceof Identifier) {
									if(loopLabel.equals(id.getAttribute())) {
										return true;
									} else {
										throw new ParseException();
									}
								} else {
									throw new ParseException();
								}
							} else {
								throw new ParseException();
							}
						} else {
							throw new ParseException();
						}
					} else {
						throw new ParseException();
					}
				} else {
					throw new ParseException();
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isIf(Keyword keyword) throws SyntaxException, ParseException {
		if(isExpr(keyword)) {
			if(lookAhead() instanceof ORLY) {
				s.setSrcPointer(tmpSrcPointer);
				if(s.lookupToken() instanceof YA) {
					if(s.lookupToken() instanceof RLY) {
						Keyword k = null;
						if(!isStatement(k = s.lookupToken())) {
							throw new ParseException();
						}
						while(isStatement(k = s.lookupToken()));
						while(k instanceof MEBBE) {
							if(isExpr(s.lookupToken())) {
								if(!isStatement(k = s.lookupToken())) {
									throw new ParseException();
								}
								while(isStatement(k = s.lookupToken()));
							} else {
								throw new ParseException();
							}
						} 
						if(k instanceof NO) {
							if(s.lookupToken() instanceof WAI) {
								if(!isStatement(k = s.lookupToken())) {
									throw new ParseException();
								}
								while(isStatement(k = s.lookupToken()));
							} else {
								throw new ParseException();
							}
						}
						if(k instanceof OIC) {
							return true;
						} else {
							throw new ParseException();
						}
					} else {
						throw new ParseException();
					}
				} else {
					throw new ParseException();
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isOperation(Keyword keyword) throws SyntaxException, ParseException {
		return isNumOp(keyword) || isBoolOp(keyword) || isStringOp(keyword);
	}

	public boolean isStringOp(Keyword keyword) {
		return keyword instanceof String || keyword instanceof Identifier;
	}

	public boolean isNumOp(Keyword keyword) throws SyntaxException, ParseException {
		if(keyword instanceof SUM || keyword instanceof DIFF ||
				keyword instanceof QUOSHUNT || keyword instanceof PRODUKT ||
				keyword instanceof BIGGR || keyword instanceof SMALLR) {
			if(s.lookupToken() instanceof OF) {
				if(isNumOp(s.lookupToken())) {
					if(s.lookupToken() instanceof AN) {
						if(isNumOp(s.lookupToken())) {
							return true;
						} else {
							throw new ParseException();
						}
					} else {
						throw new ParseException();
					}
				} else {
					throw new ParseException();
				}
			} else {
				throw new ParseException();
			}
		} else if(isNum(keyword) || isIdentifier(keyword)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isNum(Keyword keyword) {
		return keyword instanceof Int;
	}
	
	public boolean isVarAssign(Keyword keyword) throws SyntaxException, ParseException {
		if(isIdentifier(keyword)) {
			Keyword k = lookAhead();
			if(k instanceof R) {
				s.setSrcPointer(tmpSrcPointer);
				if(isOperation(s.lookupToken())) {
					return true;
				} else {
					throw new ParseException();
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isVarDecl(Keyword keyword) throws ParseException, SyntaxException {
		if(isIdentifier(keyword)) {
			if(lookAhead() instanceof IS) {
				s.setSrcPointer(tmpSrcPointer);
				if(s.lookupToken() instanceof NOW) {
					if(s.lookupToken() instanceof A) {
						if(isType(s.lookupToken())) {
							return true;
						} else {
							throw new ParseException();
						}
					} else {
						throw new ParseException();
					}
				} else {
					throw new ParseException();
				}
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
	
	public boolean isFunction(Keyword keyword) throws ParseException, SyntaxException {
		if(keyword instanceof HOW) {
			if(s.lookupToken() instanceof DUZ) {
				if(s.lookupToken() instanceof I) {
					if(s.lookupToken() instanceof Identifier) {
						
						Keyword tok = null;
						if((tok = s.lookupToken()) instanceof YR) {
							if(s.lookupToken() instanceof Identifier) {
								while(s.lookupToken() instanceof AN) {
									if(s.lookupToken() instanceof YR) {
										if(!(s.lookupToken() instanceof Identifier)) {
											throw new ParseException();
										}
									} else {
										throw new ParseException();
									}
								}
							} else {
								throw new ParseException();
							}
						}
						
						while(isStatement(tok) || 
								tok instanceof GTFO || 
								tok instanceof FOUND) {
							
							if(tok instanceof FOUND) {
								if(!(s.lookupToken() instanceof YR)) {
									throw new ParseException();
								}
								if(!isExpr(s.lookupToken())) {
									throw new ParseException();
								}
							}
							tok = s.lookupToken();
						}
						
						if(tok instanceof IF) {
							if(s.lookupToken() instanceof YOU) {
								if(s.lookupToken() instanceof SAY) {
									if(s.lookupToken() instanceof SO) {
										return true;
									} else {
										throw new ParseException();
									}
								} else {
									throw new ParseException();
								}
							} else {
								throw new ParseException();
							}
						} else {
							throw new ParseException();
						}
						
					} else {
						throw new ParseException();
					}
				} else {
					throw new ParseException();
				}
			} else {
				throw new ParseException();
			}
		} else {
			return false;
		}
		
	}
	
	public Keyword lookAhead() throws SyntaxException {
		Keyword nextToken = s.lookupToken();
		int i = s.getPointerBeforeToken();
		tmpSrcPointer = s.getSrcPointer();
		s.setSrcPointer(i);
		return nextToken;
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

		try {
			isProgram(s.lookupToken());
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(java.lang.String[] args) {
//		Parser p = new Parser();
//		try {
//			p.parse();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		Parser p = new Parser();
		p.s = new Scanner("IM IN YR loop YR arg1 WILE BOTH SAEM var2 AN 100 " +
				"    PRODUKT OF var2 AN var1 " +
				"    BOTH SAEM var2 AN 30 " +
				"    ORLY? " +
				"    YA RLY " +
				"        SUM OF var2 AN var3 " +
				"    OIC " +
				"IM OUTTA YR loop ");
		try {
			Assert.assertTrue(p.isLoop(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
}
