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

import catpiler.backend.codegeneration.CodeGenerator;
import catpiler.frontend.exception.ParseException;
import catpiler.frontend.exception.SyntaxException;
import catpiler.frontend.parser.symboltable.Symboltable;
import catpiler.frontend.parser.symboltable.SymboltableEntry;
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
import catpiler.frontend.scanner.keywords.VISIBLE;
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
	
	private CodeGenerator codeGenerator;
	
	private boolean error;
	
	private int loadStmt;
	
	public Parser() {
		error = false;
		codeGenerator = new CodeGenerator(this);
		if(symboltable == null) symboltable = new Symboltable(null);
		SymboltableEntry ste = new SymboltableEntry();
		ste.setName("IT");
		ste.setCategory("reg");
		ste.setReg("$ra");
		symboltable.put(ste);
		loadStmt = 0;
	}
	
	public SymboltableEntry currentSymboltableEntry;
	
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
			
			symboltable = new Symboltable(symboltable);
			
			Keyword next = null;
			while(isVarInit(next = s.lookupToken()) || 
					isVarDecl(next) || 
					isVarAssign(next));
			if(!(next instanceof THATSIT)) {
				markError("Missing 'THATSIT' keyword");
			}
			
			symboltable = symboltable.getOutterTable();
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
				if(symboltable == null) {
					symboltable = new Symboltable(null);
					System.out.println("created new symboltable - isVarInit()");
				}

				if(symboltable.get(id.getAttribute()) != null) {
					markError("Variable " + id.getAttribute() + 
							" has already been defined");
				} else {
					SymboltableEntry entry = new SymboltableEntry();
					entry.setName(id.getAttribute());
					entry.setType(NOOB.tokenId);
					entry.setCategory("var");
					/* I do not know the size for undefined types...
					 * therefore I'll just create 1 byte for a pointer 
					 * (which then points to the actual data)*/
					int addr = new Integer(codeGenerator.getSp());
					entry.setAddress(new Integer(addr).toString());
					codeGenerator.setSp(4);
					symboltable.put(entry);
				}
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
			
			symboltable = new Symboltable(symboltable);
			
			Keyword next = null;
			while(isStatement(next = s.lookupToken()));
			if(!(next instanceof KTHXBYE)) {
				markError("Missing 'KTHXBYE' keyword");
			}
			symboltable = symboltable.getOutterTable();
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
		} else if(isExpr(keyword)) {
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
	
	public boolean isPrint(Keyword keyword) {
		if(keyword instanceof VISIBLE) {
			Keyword lookahead = lookAhead();
			if(!isStringOp(lookahead)) {
				markError("Expected a string");
			}
			adjustSrcPointer();
			return true;
		}
		return false;
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
			
			java.lang.String op = null;
			java.lang.String arg2 = null;
			java.lang.String arg3 = null;
			if(keyword instanceof BOTH)
				op = new java.lang.String("AND");
			else if(keyword instanceof EITHER)
				op = new java.lang.String("OR");
			
			if(symboltable == null) symboltable = new Symboltable(null);
			
			if(!isExpr(s.lookupToken())) {
				markError("Expected Expression");
			}
			
			if(!(currentSymboltableEntry == null || error == true)) {
				
				// store variable in register
				if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("reg")) {
					arg2 = currentSymboltableEntry.getReg();
				} else if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("var")) {
					// currentKeyword.getAddress() contains address - let's load it into a register
					arg2 = codeGenerator.load(new Integer(currentSymboltableEntry.getAddress()));
					loadStmt++;
					// now set the attribute to the register name
					currentSymboltableEntry.setReg(arg2);
					currentSymboltableEntry.setCategory("reg");
				} else if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("const")) {
					// switch arguments if one of them is a constant
					arg2 = null;
					op = op + "I";
					
					arg3 = currentSymboltableEntry.getAttribute();
				} else {
					markError("Compiler is missing the symbol category");
				}
			} else {
				if(currentSymboltableEntry == null)
					markError("Variable not inititalized");
			}
			
			
			if(!(lookAhead() instanceof AN)) {
				markError("Missing 'AN' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!isExpr(s.lookupToken())) {
				markError("Expected expression");
			}
			
			
			// found a symbol?
			if(!(currentSymboltableEntry == null || error == true)) {
				
				// store variable in register
				if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("reg")) {
					if(arg2 == null) {
						arg2 = currentSymboltableEntry.getReg();
					} else {
						arg3 = currentSymboltableEntry.getReg();
					}
					
					currentSymboltableEntry = symboltable.get("IT");
					currentSymboltableEntry.setType(TROOF.tokenId);
					java.lang.String arg1 = currentSymboltableEntry.getReg();
					codeGenerator.put(op, arg1, arg2, arg3);
					
				} else if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("var")) {
					// currentKeyword.getAddress() contains address - let's load it into a register
					if(arg2 == null) {
						arg2 = codeGenerator.load(new Integer(currentSymboltableEntry.getAddress()).intValue());
						loadStmt++;
						// now set the attribute to the register name
						currentSymboltableEntry.setReg(arg2);
						currentSymboltableEntry.setCategory("reg");
					} else {
						arg3 = codeGenerator.load(new Integer(currentSymboltableEntry.getAddress()).intValue());
						loadStmt++;
						// now set the attribute to the register name
						currentSymboltableEntry.setReg(arg3);
						currentSymboltableEntry.setCategory("reg");
					}
					
					currentSymboltableEntry = symboltable.get("IT");
					currentSymboltableEntry.setType(TROOF.tokenId);
					java.lang.String arg1 = currentSymboltableEntry.getReg();
					codeGenerator.put(op, arg1, arg2, arg3);
					
				} else if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("const")) {
					
					// if both arguments are constants, don't generate any code 
					if(arg2 == null) {
						if(op.equals("ANDI")) {
							java.lang.String arg1;
							if(currentSymboltableEntry.getAttribute().equals("1")) {
								if(arg3.equals("0")) {
									arg1 = "0";
								} else {
									arg1 = "1";
								}
							} else {
								arg1 = "0";
							}
							
							currentSymboltableEntry = new SymboltableEntry();
							currentSymboltableEntry.setName("");
							currentSymboltableEntry.setType("TROOF");
							currentSymboltableEntry.setCategory("const");
							currentSymboltableEntry.setAttribute(arg1);
							
						} else if(op.equals("ORI")) {
							java.lang.String arg1;
							if(currentSymboltableEntry.getAttribute().equals("1")) {
								arg1 = "1";
							} else if(currentSymboltableEntry.getAttribute().equals("0")) {
								if(arg3.equals("0")) {
									arg1 = "0";
								} else {
									arg1 = "1";
								}
							} else {
								// TODO: could be a problem
								arg1 = "0";
							}
							
							currentSymboltableEntry = new SymboltableEntry();
							currentSymboltableEntry.setName("");
							currentSymboltableEntry.setType("TROOF");
							currentSymboltableEntry.setCategory("const");
							currentSymboltableEntry.setAttribute(arg1);
						}
						
					} else {
						op = op + "I";
						if(currentSymboltableEntry.getAttribute().equals("1")) {
							arg3 = "1";
						} else {
							arg3 = "0";
						}
						
						currentSymboltableEntry = symboltable.get("IT");
						currentSymboltableEntry.setType(TROOF.tokenId);
						java.lang.String arg1 = currentSymboltableEntry.getReg();
						codeGenerator.put(op, arg1, arg2, arg3);
						
					}
				} else {
					markError("Compiler is missing the symbol category");
				}
			} else {
				if(currentSymboltableEntry == null)
					markError("Variable not inititalized");
			}
			
			return true;
		} else if(keyword instanceof NOT) {
			if(!isExpr(s.lookupToken())) {
				markError("Expected expression");
			}
			
			if(!error) {
				if(currentSymboltableEntry.getCategory().equals("const")) {
					if(currentSymboltableEntry.getAttribute().equals("1")) {
						currentSymboltableEntry.setAttribute("0");
					} else {
						currentSymboltableEntry.setAttribute("1");
					}
				} else { 
					java.lang.String arg1;
					if(currentSymboltableEntry.getCategory().equals("var")) {
						arg1 = codeGenerator.load(new Integer(currentSymboltableEntry.getAddress()));
						loadStmt++;
						currentSymboltableEntry.setReg(arg1);
						currentSymboltableEntry.setCategory("reg");
					} else {
						arg1 = currentSymboltableEntry.getReg();
					}
					// arg3 = !arg3 == arg3 = nor(arg3,0)
					codeGenerator.put("nor", arg1, currentSymboltableEntry.getReg(), "0");
				}
			}
			
			return true;
		} else if(isBool(keyword)) {
			if(!error) {
				currentSymboltableEntry = new SymboltableEntry();
				currentSymboltableEntry.setName("");
				if(keyword.getAttribute().equals("1")) {
					currentSymboltableEntry.setAttribute("1");
				} else {
					currentSymboltableEntry.setAttribute("0");
				}
				currentSymboltableEntry.setCategory("const");
				currentSymboltableEntry.setType("TROOF");
			}
			return true;
		} else if(isIdentifier(keyword)) {
			
			if(symboltable.exists(keyword.getAttribute())) {
				SymboltableEntry ste = symboltable.get(keyword.getAttribute());
				if(!ste.getType().equals(TROOF.tokenId) && !ste.getType().equals(NOOB.tokenId)) {
					//markError("Type mismatch for variable " + ste.getName());
					return false;
				} else if(ste.getType().equals(NOOB.tokenId)) {
					ste.setType(TROOF.tokenId);
				}
				
				if(!error) {
					if(ste.getAttribute() == null) {
						// load variable if no value exists
						java.lang.String reg;
						if(ste.getCategory().equals("var")) {
							reg = codeGenerator.load(new Integer(ste.getAddress()));
							loadStmt++;
//							reg = codeGenerator.loadImmediately(0);
							ste.setReg(reg);
							ste.setCategory("reg");
						} else {
							reg = ste.getReg();
						}
						// TODO: code optimization task: we do not need this every time
						codeGenerator.put("ADD", "$ra", "$r0", reg);
					} else {
						ste.setCategory("const");
					}
					currentSymboltableEntry = ste;
				}
			} else {
				markError("Unkown variable " + keyword.getAttribute());
			}
			return true;
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
		if(keyword instanceof WIN) {
			keyword.setAttribute("1");
			return true;
		} else if(keyword instanceof FAIL) {
			keyword.setAttribute("0");
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
			
			if(!(lookAhead() instanceof IN)) {
				return false;
			} else {
				adjustSrcPointer();
			}
			
			symboltable = new Symboltable(symboltable);
			boolean skipLoop = false;
			boolean needFixUp = false;
			int jumpFixup = 0; 
			int branchFixup = 0;
			
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
			
			jumpFixup = codeGenerator.getPc();
			loadStmt = 0;
			if(tok instanceof WILE || tok instanceof TIL) {
				if(!isExpr(s.lookupToken())) {
					markError("No limiting expression provided");
				}
				jumpFixup = jumpFixup + loadStmt;
				loadStmt = 0;
				// Expression, stored in $ra
				// Branch on equal to end of loop (store loopoffset)
				// Jump on end of loop to first expression
				currentSymboltableEntry = symboltable.get("IT");
				if(currentSymboltableEntry.getCategory().equals("const")) {
					if(currentSymboltableEntry.getAttribute().equals("0")) {
						System.out.println("Skipping else if and else statements");
						skipLoop = true;
					}
				} else {
					branchFixup = codeGenerator.getPc();
					if(tok instanceof WILE) {
						// if $ra = 0 -> skip loop
						codeGenerator.put("BEQ", "$r0", "$ra", "offset");
					} else if(tok instanceof TIL) {
						// if $ra = 1 -> skip loop
						codeGenerator.put("BGTZ", "$ra", "offset");
					}
					needFixUp = true;
				}
				tok = s.lookupToken();
			}
			
			// temporarily switch off code generation, if skipIf is true
			// switching of code generation is realized by setting the 
			// error flag to true, even if there is no error - 
			// hope that does not create confusion
			if(skipLoop && error == false) {
				error = true;
			} else {
				skipLoop = false;
			}
			
			while(isStatement(tok))
				tok = s.lookupToken();
			
			if(skipLoop) {
				error = true;
			}
			
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
			
			if(!error) {
				codeGenerator.put("J", new Integer(jumpFixup - codeGenerator.getPc()).toString());
			}
			if(needFixUp && !error) {
				codeGenerator.fixUp(branchFixup, codeGenerator.getPc() - branchFixup);
			}
			
			symboltable = symboltable.getOutterTable();
			
			return true;
		} else {
			return false;
		}
	}

	public boolean isIf(Keyword keyword) throws ParseException {
		if(keyword instanceof ORLY) {
			symboltable = new Symboltable(symboltable);
			boolean skipIf = false;
			boolean skipElse = false;
			boolean skipFollowing = false;
			boolean needFixUp = false;
			int fixupLine = 0;
			int jumpFixupLine = 0;
			
			if(currentSymboltableEntry ==  null) {
				currentSymboltableEntry = symboltable.get("IT");
			}
			if(currentSymboltableEntry.getCategory().equals("const")) {
				if(currentSymboltableEntry.getAttribute().equals("1")) {
					System.out.println("Skipping else if and else statements");
					skipElse = true;
					skipFollowing = true;
				} else {
					System.out.println("Skipping if statement");
					skipIf = true;
				}
			} else {
				fixupLine = codeGenerator.getPc();
				codeGenerator.put("BEQ", "$r0", "$ra", "offset");
				needFixUp = true;
			}
			
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
			
			// temporarily switch off code generation, if skipIf is true
			// switching of code generation is realized by setting the 
			// error flag to true, even if there is no error - 
			// hope that does not create confusion
			if(skipIf && error == false) {
				error = true;
			} else {
				skipIf = false;
			}
			
			Keyword k = null;
			if(!isStatement(k = s.lookupToken())) {
				markError("Expected statement after condition");
			}
			
			while(isStatement(k = s.lookupToken()));
			
			if(!skipIf && !error) {
				if(!currentSymboltableEntry.getName().equals("IT") && currentSymboltableEntry.getCategory().equals("const")) {
					java.lang.String result = currentSymboltableEntry.getAttribute();
					currentSymboltableEntry = symboltable.get("IT");
					codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), "$r0", result);
				}
				jumpFixupLine = codeGenerator.getPc();
				codeGenerator.put("J", "offset");
			}
			
			// switch code generation on again
			if(skipIf) {
				error = false;
			}
			
//				if(skipElse && error == false) {
//					error = true;
//				} else {
//					skipElse = false;
//				}
			
			while(k instanceof MEBBE) {
				
				if(skipElse && error == false) {
					error = true;
				} else {
					skipElse = false;
					System.out.println("Computing else statement");
				}
				
				if(needFixUp && !error) {
					codeGenerator.fixUp(fixupLine, codeGenerator.getPc() - fixupLine);
				}
				
				if(!isExpr(s.lookupToken())) {
					markError("Expected expression after 'MEBBE'");
				}
				
				if(currentSymboltableEntry ==  null) {
					currentSymboltableEntry = symboltable.get("IT");
				}
				if(!error && currentSymboltableEntry.getCategory().equals("const")) {
					if(!skipFollowing) {
						if(currentSymboltableEntry.getAttribute().equals("1")) {
							skipFollowing = true;
							System.out.println("Skipping the following statements");
						} else {
							skipElse = true;
							error = true;
							System.out.println("Skipping this statement");
						}
					} else {
						System.out.println("Skipped following statement");
					}
				} else if(!error){
					fixupLine = codeGenerator.getPc();
					codeGenerator.put("BEQ", "$r0", "$ra", "offset");
					needFixUp = true;
				}
				
				if(!isStatement(k = s.lookupToken())) {
					markError("Expected statement after condition");
				}
				
				while(isStatement(k = s.lookupToken()));
				
				if(skipElse && !skipFollowing) {
					error = false;
					skipElse = false;
				} else if(skipFollowing) {
					skipElse = true;
				}
				
				if(!error) {
					if(!currentSymboltableEntry.getName().equals("IT") && currentSymboltableEntry.getCategory().equals("const")) {
						java.lang.String result = currentSymboltableEntry.getAttribute();
						currentSymboltableEntry = symboltable.get("IT");
						codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), "$r0", result);
					}
					codeGenerator.fixUp(jumpFixupLine, 
							codeGenerator.getPc() - jumpFixupLine);
					jumpFixupLine = codeGenerator.getPc();
					codeGenerator.put("J", "offset");
				}
			}
			if(k instanceof NO) {
				
				if(skipElse && error == false) {
					error = true;
				} else {
					skipElse = false;
				}
				
				if(!(lookAhead() instanceof WAI)) {
					markError("Missing 'WAI' keyword");
				} else {
					adjustSrcPointer();
				}
				
				if(needFixUp && !error) {
					codeGenerator.fixUp(fixupLine, codeGenerator.getPc() - fixupLine);
				}
				if(!isStatement(k = s.lookupToken())) {
					markError("Expected statement after condition");
				}
				
				while(isStatement(k = s.lookupToken()));
				
				if(!error) {
					if(!currentSymboltableEntry.getName().equals("IT") && currentSymboltableEntry.getCategory().equals("const")) {
						java.lang.String result = currentSymboltableEntry.getAttribute();
						currentSymboltableEntry = symboltable.get("IT");
						codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), "$r0", result);
					}
					codeGenerator.fixUp(jumpFixupLine, 
							codeGenerator.getPc() - jumpFixupLine);
					jumpFixupLine = codeGenerator.getPc();
//						codeGenerator.put("J", "offset");
				}
			}
			
			if(skipElse || skipFollowing) {
				error = false;
			}
			
			if(!(k instanceof OIC)) {
				markError("Missing 'OIC' keyword");
			}
			
			if(needFixUp && !error) {
				codeGenerator.fixUp(fixupLine, codeGenerator.getPc() - fixupLine);
			}
			
			symboltable = symboltable.getOutterTable();
			
			return true;
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
		} else if(keyword instanceof Identifier) {
			if(symboltable == null) symboltable = new Symboltable(null);
			SymboltableEntry ste;
			if((ste = symboltable.get(keyword.getAttribute())) == null) {
				markError("Unkown variable " + keyword.getAttribute());
			}
			if(ste.getType().equals("CHARZ") || parseTest) {
				return true;
			} else {
				return false;
			}
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
			
			java.lang.String op = null;
			java.lang.String arg2 = null;
			java.lang.String arg3 = null;
			boolean arg2IsConst = false;
			if(keyword instanceof SUM)
				op = new java.lang.String("ADD");
			else if(keyword instanceof DIFF || keyword instanceof BIGGR || keyword instanceof SMALLR)
				op = new java.lang.String("SUB");
			else if(keyword instanceof PRODUKT)
				op = new java.lang.String("MUL");
			else if(keyword instanceof QUOSHUNT)
				op = new java.lang.String("DIV");
			
			if(symboltable == null) symboltable = new Symboltable(null);
			
			if(!isNumOp(s.lookupToken())) {
				markError("Expected numerical operation or number");
			}
			
			// look up identifier in the symboltable and store argument in register or
			// calculate immediately
			if(!(currentSymboltableEntry == null || error == true)) {
			
				// store variable in register
				if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("reg")) {
					arg2 = currentSymboltableEntry.getReg();
				} else if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("var")) {
					// currentKeyword.getAddress() contains address - let's load it into a register
					arg2 = codeGenerator.load(new Integer(currentSymboltableEntry.getAddress()).intValue());
					loadStmt++;
					// now set the attribute to the register name
					currentSymboltableEntry.setReg(arg2);
					currentSymboltableEntry.setCategory("reg");
				} else if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("const")) {
					// switch arguments if one of them is a constant
					op = op + "I";
					arg3 = currentSymboltableEntry.getAttribute();
					arg2IsConst = true;
				} else {
					markError("Compiler is missing the symbol category");
				}
			} else {
				if(currentSymboltableEntry == null)
					markError("Variable not inititalized");
			}
			
			// after the first parameter, we are expecting an AN keyword
			if(!(lookAhead() instanceof AN)) {
				markError("Missing 'AN' keyword");
			} else {
				adjustSrcPointer();
			}
			
			// ...and a second parameter
			if(!isNumOp(s.lookupToken())) {
				markError("Expected numerical operation or number");
			}
			
			// found a symbol?
			if(!(currentSymboltableEntry == null || error == true)) {
				
				// store variable in register
				if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("reg")) {
					// if the first parameter was a constant
					if(arg2IsConst) {
						// when operation is '-' or '/', we cannot make use of the commutative law
						// thus, load the number into a register
						if(op.equals("SUBI") || op.equals("DIVI")) {
							arg2 = codeGenerator.loadImmediately(new Integer(arg3));
							op = op.substring(0, op.length()-1);
							arg2IsConst = false;
						}
					}
					if(arg2IsConst) {
						
						arg2 = currentSymboltableEntry.getReg();
						
						currentSymboltableEntry = symboltable.get("IT");
						currentSymboltableEntry.setType(NUMBR.tokenId);
						java.lang.String arg1 = currentSymboltableEntry.getReg();
						codeGenerator.put(op, arg1, arg2, arg3);
						
						if(keyword instanceof BIGGR) {
							codeGenerator.put("BGTZ", currentSymboltableEntry.getReg(), "3");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), "$r0",  arg3);
							codeGenerator.put("J", "2");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg2, "0");							
						} else if(keyword instanceof SMALLR) {
							codeGenerator.put("BGTZ", currentSymboltableEntry.getReg(), "3");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg2, "0");
							codeGenerator.put("J", "2");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), "$r0", arg3);
						}
					} else {
						arg3 = currentSymboltableEntry.getReg();
						
						currentSymboltableEntry = symboltable.get("IT");
						currentSymboltableEntry.setType(NUMBR.tokenId);
						java.lang.String arg1 = currentSymboltableEntry.getReg();
						codeGenerator.put(op, arg1, arg2, arg3);
						
						if(keyword instanceof BIGGR) {
							codeGenerator.put("BGTZ", currentSymboltableEntry.getReg(), "3");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg3, "0");							
							codeGenerator.put("J", "2");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg2, "0");
						} else if(keyword instanceof SMALLR) {
							codeGenerator.put("BGTZ", currentSymboltableEntry.getReg(), "3");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg2, "0");
							codeGenerator.put("J", "2");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg3, "0");
						}
					}
					
				} else if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("var")) {
					
					// if the first parameter was a constant
					if(arg2IsConst) {
						// when operation is '-' or '/', we cannot make use of the commutative law
						// thus, load the number into a register
						if(op.equals("SUBI") || op.equals("DIVI")) {
							arg2 = codeGenerator.loadImmediately(new Integer(arg3));
							op = op.substring(0, op.length()-1);
							arg2IsConst = false;
						}
					}
					
					if(arg2IsConst) {
						// currentKeyword.getAddress() contains address - let's load it into a register
						arg2 = codeGenerator.load(new Integer(currentSymboltableEntry.getAddress()).intValue());
						loadStmt++;
						// now set the attribute to the register name
						currentSymboltableEntry.setReg(arg2);
						currentSymboltableEntry.setCategory("reg");
						
						currentSymboltableEntry = symboltable.get("IT");
						currentSymboltableEntry.setType(NUMBR.tokenId);
						java.lang.String arg1 = currentSymboltableEntry.getReg();
						codeGenerator.put(op, arg1, arg2, arg3);
						
						if(keyword instanceof BIGGR) {
							codeGenerator.put("BGTZ", currentSymboltableEntry.getReg(), "3");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), "$r0", arg3);
							codeGenerator.put("J", "2");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg2, "0");							
						} else if(keyword instanceof SMALLR) {
							codeGenerator.put("BGTZ", currentSymboltableEntry.getReg(), "3");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg2, "0");
							codeGenerator.put("J", "2");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), "$r0", arg3);
						}
					} else {
						arg3 = codeGenerator.load(new Integer(currentSymboltableEntry.getAddress()).intValue());
						loadStmt++;
						// now set the attribute to the register name
						currentSymboltableEntry.setReg(arg3);
						currentSymboltableEntry.setCategory("reg");
						
						currentSymboltableEntry = symboltable.get("IT");
						currentSymboltableEntry.setType(NUMBR.tokenId);
						java.lang.String arg1 = currentSymboltableEntry.getReg();
						codeGenerator.put(op, arg1, arg2, arg3);
						
						if(keyword instanceof BIGGR) {
							codeGenerator.put("BGTZ", currentSymboltableEntry.getReg(), "3");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg3, "0");							
							codeGenerator.put("J", "2");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg2, "0");
						} else if(keyword instanceof SMALLR) {
							codeGenerator.put("BGTZ", currentSymboltableEntry.getReg(), "3");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg2, "0");
							codeGenerator.put("J", "2");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg3, "0");
						}
					}
					
				} else if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("const")) {
					
					// if both arguments are constants, don't generate any code 
					if(arg2IsConst) {
						if(op.equals("ADDI")) {
							Integer arg1 = new Integer(arg3) + 
								new Integer(currentSymboltableEntry.getAttribute());
							
							currentSymboltableEntry = new SymboltableEntry();
							currentSymboltableEntry.setName("");
							currentSymboltableEntry.setType("NUMBR");
							currentSymboltableEntry.setCategory("const");
							currentSymboltableEntry.setAttribute(arg1.toString());
							
						} else if(op.equals("SUBI")) {
							// the following is necessary to store the arg2 value for BIGGR/SMALLR comparison
							arg2 = currentSymboltableEntry.getAttribute();
							Integer arg1 = new Integer(arg3) - 
								new Integer(currentSymboltableEntry.getAttribute());
							
							currentSymboltableEntry = new SymboltableEntry();
							currentSymboltableEntry.setName("");
							currentSymboltableEntry.setType("NUMBR");
							currentSymboltableEntry.setCategory("const");
							currentSymboltableEntry.setAttribute(arg1.toString());
							
						} else if(op.equals("DIVI")) {
							if(currentSymboltableEntry.getAttribute().equals("0")) {
								markError("Divison by 0");
							} else {
								Integer arg1 = new Integer(arg3) / 
									new Integer(currentSymboltableEntry.getAttribute());
								
								currentSymboltableEntry = new SymboltableEntry();
								currentSymboltableEntry.setName("");
								currentSymboltableEntry.setType("NUMBR");
								currentSymboltableEntry.setCategory("const");
								currentSymboltableEntry.setAttribute(arg1.toString());
								
							}
						} else if(op.equals("MULI")) {
							Integer arg1 = new Integer(arg3) * 
								new Integer(currentSymboltableEntry.getAttribute());
							
							currentSymboltableEntry = new SymboltableEntry();
							currentSymboltableEntry.setName("");
							currentSymboltableEntry.setType("NUMBR");
							currentSymboltableEntry.setCategory("const");
							currentSymboltableEntry.setAttribute(arg1.toString());
							
						}
						
						if(keyword instanceof BIGGR) {
							if(new Integer(currentSymboltableEntry.getAttribute()) > 0) {
								currentSymboltableEntry.setAttribute(arg3);
							} else {
								currentSymboltableEntry.setAttribute(arg2);
							}
						} else if(keyword instanceof SMALLR) {
							if(new Integer(currentSymboltableEntry.getAttribute()) > 0) {
								currentSymboltableEntry.setAttribute(arg2);
							} else {
								currentSymboltableEntry.setAttribute(arg3);
							}
						}
					} else {
						op = op + "I";
						arg3 = currentSymboltableEntry.getAttribute();
						
						currentSymboltableEntry = symboltable.get("IT");
						currentSymboltableEntry.setType(NUMBR.tokenId);
						java.lang.String arg1 = currentSymboltableEntry.getReg();
						codeGenerator.put(op, arg1, arg2, arg3);
						
						if(keyword instanceof BIGGR) {
							codeGenerator.put("BGTZ", currentSymboltableEntry.getReg(), "3");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), "$r0", arg3);
							codeGenerator.put("J", "2");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg2, "0");
						} else if(keyword instanceof SMALLR) {
							codeGenerator.put("BGTZ", currentSymboltableEntry.getReg(), "3");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), arg2, "0");							
							codeGenerator.put("J", "2");
							codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), "$r0", arg3);
						}
					}
				} else {
					markError("Compiler is missing the symbol category");
				}
			} else {
				if(currentSymboltableEntry == null)
					markError("Variable not inititalized");
			}
			
			return true;
		} else if(isNum(keyword)) {
			if(!error) {
				currentSymboltableEntry = new SymboltableEntry();
				currentSymboltableEntry.setName("");
				currentSymboltableEntry.setType("NUMBR");
				currentSymboltableEntry.setCategory("const");
				currentSymboltableEntry.setAttribute(keyword.getAttribute());
			}
			return true;
		} else if(isIdentifier(keyword)) {
			if(symboltable == null) symboltable = new Symboltable(null);
			if(symboltable.exists(keyword.getAttribute())) {
				SymboltableEntry ste = symboltable.get(keyword.getAttribute());
				if(!ste.getType().equals("NUMBR") && !ste.getType().equals("NOOB")) {
					//markError("Type mismatch for variable " + ste.getName());
					return false;
				} else if(ste.getType().equals("NOOB")) {
					ste.setType("NUMBR");
				}
				
				if(!error) {
					if(ste.getAttribute() == null) {
						// load variable if no value exists
						java.lang.String reg;
						if(ste.getCategory().equals("var")) {
//							reg = codeGenerator.loadImmediately(0);
							reg = codeGenerator.load(new Integer(ste.getAddress()));
							loadStmt++;
							ste.setReg(reg);
							ste.setCategory("reg");
						} else {
							reg = ste.getReg();
						}
						// TODO: code optimization task: we do not need this every time
						codeGenerator.put("ADD", "$ra", "$r0", reg);
					} else {
						ste.setCategory("const");
					}
					currentSymboltableEntry = ste;
				}
			} else {
				markError("Unkown variable " + keyword.getAttribute());
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean isNum(Keyword keyword) {
		return keyword instanceof Int;
	}
	
	public boolean isVarAssign(Keyword keyword) throws ParseException {
		if(isIdentifier(keyword)) {
			if(symboltable == null) symboltable = new Symboltable(null);
			SymboltableEntry ste = symboltable.get(((Identifier) keyword).getAttribute());
			if(ste == null)
				markError("Unknown variable " + ((Identifier) keyword).getAttribute());
			Keyword k = lookAhead();
			if(k instanceof R) {
				adjustSrcPointer();
				if(!isOperation(s.lookupToken())) {
					markError("Expected value");
				}
				if(ste != null) {
					ste.setAttribute(currentSymboltableEntry.getAttribute());
					ste.setType(currentSymboltableEntry.getType());
					ste.setAddress(currentSymboltableEntry.getAddress());
					ste.setCategory(currentSymboltableEntry.getCategory());
					ste.setReg(currentSymboltableEntry.getReg());
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
	
	
	private void checkTypeConversions(java.lang.String lastType, Keyword newType, Identifier identifier) {
		if(isType(newType)) {
			if(lastType == null) lastType = NOOB.tokenId;
			if(!lastType.equals(NOOB.tokenId)) {
				// change type of identifier from integer to...
				if(lastType.equals(NUMBR.tokenId)) {
					// ... NULL
					if(newType.getTokenID().equals(NOOB.tokenId)) {
						// TODO: set address to null
					} else if(newType.getTokenID().equals(NUMBR.tokenId)) {
						// do nothing
					} else if(newType.getTokenID().equals(NUMBRZ.tokenId)) {
						markError("Cannot convert integer to integer array");
					}
					// ... boolean
					else if(newType.getTokenID().equals(TROOF.tokenId)) {
						if(new Integer(identifier.getAttribute()) > 0) {
							identifier.setAttribute(WIN.tokenId);
						} else {
							identifier.setAttribute(FAIL.tokenId);
						}
					} else if(newType.getTokenID().equals(TROOFZ.tokenId)) {
						markError("Cannot convert integer to boolean array");
					}
					// ... a string/char
					else if(newType.getTokenID().equals(CHAR.tokenId) || 
							newType.getTokenID().equals(CHARZ.tokenId)) {
						// do nothing until it's stored in the register
						// TODO: check for memory size, integer may not exceed 1 byte!!!!
					}
				}
				// change type of identifier from int[] to ...
				else if(lastType.equals(NUMBRZ.tokenId)) {
					NUMBRZ numbrz = ((NUMBRZ) newType);
					// ... NULL
					if(newType.getTokenID().equals(NOOB.tokenId)) {
						// TODO: set address to null
					} else if(newType.getTokenID().equals(NUMBR.tokenId)) {
						markError("Cannot convert integer array to integer");
					} else if(newType.getTokenID().equals(NUMBRZ.tokenId)) {
						// do nothing
					} else if(newType.getTokenID().equals(TROOF.tokenId)) {
						markError("Cannot convert integer array to boolean");
					} else if(newType.getTokenID().equals(TROOFZ.tokenId)) {
						// TODO: refactor
//						if(identifier.getAttributeArrayList() == null && 
//							identifier.getAttribute() != null) {
//							// Array is defined as e.g. "NUMBRZ R 1 2 3 4 5"
//							identifier.setAttributeArrayList(
//								new ArrayList<java.lang.String>(
//										Arrays.asList(identifier.getAttribute().split(" "))
//									));
//						}
//						ArrayList<java.lang.String> b_list = new ArrayList<java.lang.String>(
//								identifier.getAttributeArrayList().size()
//						);
//						// 
//						for(int i=0; i<identifier.getAttributeArrayList().size(); i++) {
//							if(new Integer(identifier.getAttributeArrayList().get(i)) > 0) {
//								b_list.add(WIN.tokenId);
//							} else {
//								b_list.add(FAIL.tokenId);
//							}
//						}
					} else if(newType.getTokenID().equals(CHAR.tokenId)) {
						markError("Cannot convert integer array to char");
					} else if(newType.getTokenID().equals(CHARZ.tokenId)) {
						// convert to string
						numbrz.getAttribute().replaceAll(" ", "");
					}
				}
				// change type of identifier from char to...
				else if(lastType.equals(CHAR.tokenId)) {
					// ... NULL
					if(newType.getTokenID().equals(NOOB.tokenId)) {
						// TODO: set address to null
					} else if(newType.getTokenID().equals(NUMBR.tokenId)) {
						// do nothing 
					} else if(newType.getTokenID().equals(NUMBRZ.tokenId)) {
						markError("Cannot convert char to integer array");
					} else if(newType.getTokenID().equals(TROOF.tokenId)) {
						markError("Cannot convert char to boolean");
					} else if(newType.getTokenID().equals(TROOFZ.tokenId)) {
						markError("Cannot convert char to boolean array");
					} else if(newType.getTokenID().equals(CHAR.tokenId)) {
						// do nothing
					} else if(newType.getTokenID().equals(CHARZ.tokenId)) {
						// do nothing, will be stored as a char array
					}
				}
				// change type of identifier from string to...
				else if(lastType.equals(CHARZ.tokenId)) {
					// ... NULL
					if(newType.getTokenID().equals(NOOB.tokenId)) {
						// TODO: set address to null
					} else if(newType.getTokenID().equals(NUMBR.tokenId)) {
						// do nothing
						// TODO: check memory size
					} else if(newType.getTokenID().equals(NUMBRZ.tokenId)) {
						markError("Cannot convert char to integer array");
					} else if(newType.getTokenID().equals(TROOF.tokenId)) {
						markError("Cannot convert char to boolean");
					} else if(newType.getTokenID().equals(TROOFZ.tokenId)) {
						markError("Cannot convert char to boolean array");
					} else if(newType.getTokenID().equals(CHAR.tokenId)) {
						// to convert a string to a char, the array element should simply be accessed
						// conversion isn't possible in that case
						markError("Cannot convert string to single char");
					} else if(newType.getTokenID().equals(CHARZ.tokenId)) {
						// do nothing
					}
				}
				// change type of identifier from boolean to...
				else if(lastType.equals(TROOF.tokenId)) {
					// ... NULL
					if(newType.getTokenID().equals(NOOB.tokenId)) {
						// TODO: set address to null
					} else if(newType.getTokenID().equals(NUMBR.tokenId)) {
						if(identifier.getAttribute().equals(WIN.tokenId))
							identifier.setAttribute("1");
						else if(identifier.getAttribute().equals(FAIL.tokenId))
							identifier.setAttribute("0");
					} else if(newType.getTokenID().equals(NUMBRZ.tokenId)) {
						markError("Cannot convert boolean to integer array");
					}
					// ... boolean
					else if(newType.getTokenID().equals(TROOF.tokenId)) {
						// do nothing
					} else if(newType.getTokenID().equals(TROOFZ.tokenId)) {
						markError("Cannot convert boolean to boolean array");
					}
					// ... a string/char
					else if(newType.getTokenID().equals(CHAR.tokenId) || 
							newType.getTokenID().equals(CHARZ.tokenId)) {
						markError("Cannot convert boolean to char or string");
					}
				}
				// change type of identifier from boolean array to...
				else if(lastType.equals(TROOFZ.tokenId)) {
					// ... NULL
					if(newType.getTokenID().equals(NOOB.tokenId)) {
						// TODO: set address to null
					} else if(newType.getTokenID().equals(NUMBR.tokenId)) {
						markError("Cannot convert boolean array to integer");
					} else if(newType.getTokenID().equals(NUMBRZ.tokenId)) {
						// TODO: refactor
//						if(identifier.getAttributeArrayList() == null && 
//								identifier.getAttribute() != null) {
//							// Array is defined as e.g. "NUMBRZ R 1 2 3 4 5"
//							identifier.setAttributeArrayList(
//								new ArrayList<java.lang.String>(Arrays.asList(identifier.getAttribute().split(" "))
//							));
//						}
//						ArrayList<java.lang.String> b_list = new ArrayList<java.lang.String>(identifier.getAttributeArrayList().size());
//						// 
//						for(int i=0; i<identifier.getAttributeArrayList().size(); i++) {
//							if(identifier.getAttributeArrayList().get(i).equals(WIN.tokenId)) {
//								b_list.add("1");
//							} else {
//								b_list.add("0");
//							}
//						}
					}
					// ... boolean
					else if(newType.getTokenID().equals(TROOF.tokenId)) {
						// do nothing
					} else if(newType.getTokenID().equals(TROOFZ.tokenId)) {
						markError("Cannot convert boolean to boolean array");
					}
					// ... a string/char
					else if(newType.getTokenID().equals(CHAR.tokenId) || 
							newType.getTokenID().equals(CHARZ.tokenId)) {
						markError("Cannot convert boolean to char or string");
					}
				}
			}
		} else {
			markError("Expected a type");
		}
	}

	public boolean isVarDecl(Keyword keyword) throws ParseException {
		if(isIdentifier(keyword)) {
			
			if(lookAhead() instanceof IS) {
				adjustSrcPointer();
				
				if(symboltable == null) symboltable = new Symboltable(null);
				
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
				
				SymboltableEntry ste = symboltable.get(keyword.getAttribute());
				java.lang.String lastType;
				if(ste == null) {
					markError("Unkown variable " + keyword.getAttribute());
					ste = new SymboltableEntry();
					ste.setName(keyword.getAttribute());
					ste.setType(NOOB.tokenId);
					lastType = NOOB.tokenId;
				} else {
					lastType = ste.getType();
				}
				
				checkTypeConversions(lastType, typeKeyword, (Identifier) keyword);
				ste.setType(typeKeyword.getTokenID());
				
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
			
			symboltable = new Symboltable(symboltable);
			
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
				if(!isType(lookAhead())) {
					if(lookAhead() instanceof Identifier) {
						markError("Expected type declaration for argument " + 
								lookAhead().getAttribute());
						adjustSrcPointer();
					} else {
						markError("Expected another argument");
					}
				} else {
					adjustSrcPointer();
					if(!(lookAhead() instanceof Identifier)) {
						markError("Expected another argument");
					} else {
						adjustSrcPointer();
					}
				}
				
				if(symboltable == null) symboltable = new Symboltable(null);
				
				while((tok = lookAhead()) instanceof AN) {
					adjustSrcPointer();
					if(!(lookAhead() instanceof YR)) {
						markError("Missing 'YR' keyword");
					} else {
						adjustSrcPointer();
					}
					if(!isType(lookAhead())) {
						if(lookAhead() instanceof Identifier) {
							markError("Expected type declaration for argument " + 
									lookAhead().getAttribute());
							adjustSrcPointer();
						} else {
							markError("Expected another argument");
						}
					} else {
						adjustSrcPointer();
						if(!(lookAhead() instanceof Identifier)) {
							markError("Expected another argument");
						} else {
							adjustSrcPointer();
						}
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
