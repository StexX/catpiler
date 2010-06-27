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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Set;

import org.junit.Assert;

import catpiler.backend.codegeneration.CodeGenerator;
import catpiler.backend.codegeneration.MemoryManager;
import catpiler.frontend.exception.ParseException;
import catpiler.frontend.exception.SyntaxException;
import catpiler.frontend.parser.symboltable.FunctionAccessManager;
import catpiler.frontend.parser.symboltable.FunctionItem;
import catpiler.frontend.parser.symboltable.Symboltable;
import catpiler.frontend.parser.symboltable.SymboltableEntry;
import catpiler.frontend.parser.symboltable.TypeItem;
import catpiler.frontend.scanner.Scanner;
import catpiler.frontend.scanner.keywords.A;
import catpiler.frontend.scanner.keywords.ALL;
import catpiler.frontend.scanner.keywords.AN;
import catpiler.frontend.scanner.keywords.ANY;
import catpiler.frontend.scanner.keywords.BIGGR;
import catpiler.frontend.scanner.keywords.BOTH;
import catpiler.frontend.scanner.keywords.CALL;
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
import catpiler.utils.ErrorReporter;
import catpiler.utils.StringLib;

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
	
	private MemoryManager memoryManager;
	
	private FunctionAccessManager functionAccessManager;
	
	private int loadStmt;
	
	public Parser() {
		codeGenerator = new CodeGenerator(null);
		memoryManager = new MemoryManager(codeGenerator);
		functionAccessManager = new FunctionAccessManager();
		if(symboltable == null) symboltable = new Symboltable("global", null);
		SymboltableEntry ste = new SymboltableEntry();
		ste.setName("IT");
		ste.setCategory("reg");
		ste.setReg("$v0");
		symboltable.put(ste);
		loadStmt = 0;
	}
	
	public Parser(java.lang.String filename) {
		java.lang.String outputName = filename.replace(".lol", ".cat");
		codeGenerator = new CodeGenerator(outputName);
		memoryManager = new MemoryManager(codeGenerator);
		functionAccessManager = new FunctionAccessManager();
		if(symboltable == null) symboltable = new Symboltable("global", null);
		SymboltableEntry ste = new SymboltableEntry();
		ste.setName("IT");
		ste.setCategory("reg");
		ste.setReg("$v0");
		symboltable.put(ste);
		loadStmt = 0;
	}
	
	public SymboltableEntry currentSymboltableEntry;
	
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
				Keyword moduleKeyword = lookAhead();
				if(moduleKeyword instanceof Identifier) {
					adjustSrcPointer();
				} else {
					ErrorReporter.markError("Missing module");
				}
				// [1] Open file moduleKeyword.cat
				// [2] Create new symbol-file
				// [3] Write module-caller into symbol-file. Format: (# caller: main.cat, anotherModule.cat)
				// [4] Start parsing -> will lead to next module import, if any exists
				// [5] Code generation starts when we are at the bottom of the module tree 
				//     write code into the symbol file. 
				// [6] After the parsing, take function names and store them in the symbol-file of the caller 
				//     Format: (# callee-functions: function1, function2)
				// [7] Store file size of module file in symbol-file to check whether content has changed
				//     Format: (# filesize: 439)
				// [8] Linker copies all files into one big file, don't mind about header, they are 
				//     commented for MIPS assembler
				// [9] If a file-size has changed, compile the particular file again and link it. 
			} else {
				ErrorReporter.markError("Missing 'HAS' keyword");
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
			TypeItem type = new TypeItem();
			if(!isIdentifier(lookAhead())) {
				ErrorReporter.markError("Missing structure label after 'STUFF'");
			} else {
				adjustSrcPointer();
				// TODO add it to the list of types
				type.setName(keyword.getAttribute());
				symboltable.put(type);
			}
			
			symboltable = new Symboltable(keyword.getAttribute(), symboltable);
			
			Keyword next = null;
			while(isVarInit(next = s.lookupToken()) || 
					isVarDecl(next) || 
					isVarAssign(next));
			if(!(next instanceof THATSIT)) {
				ErrorReporter.markError("Missing 'THATSIT' keyword");
			}
			
			Set<java.lang.String> registers = symboltable.getRegInScopeLevel();
			for(java.lang.String r : registers) {
				codeGenerator.releaseRegister(r);
			}
			codeGenerator.nft = codeGenerator.nft - registers.size();
			type.setSymbolTable(symboltable);
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
				ErrorReporter.markError("Missing 'HAS' keyword");
			} else {
				adjustSrcPointer();
			}
			if(!(lookAhead() instanceof A)) {
				ErrorReporter.markError("Missing 'A' keyword");
			} else {
				adjustSrcPointer();
			}
			Keyword id = lookAhead();
			if(!(id instanceof Identifier)) {
				ErrorReporter.markError("Missing variable");
			} else {
				adjustSrcPointer();
				if(symboltable == null) {
					symboltable = new Symboltable("main", null);
					System.out.println("THIS SHOULD NEVER HAPPEN!!");
				}

				if(symboltable.get(id.getAttribute()) != null) {
					ErrorReporter.markError("Variable " + id.getAttribute() + 
							" has already been defined");
				} else {
					// found global variables -> stored in global .data segment
					if(symboltable.getGlobalTable().equals(symboltable)) {
						SymboltableEntry entry = new SymboltableEntry();
						entry.setName(id.getAttribute());
						entry.setType(NOOB.tokenId);
						entry.setCategory("var");
						entry.setAddress(id.getAttribute());
					} else {
						SymboltableEntry entry = new SymboltableEntry();
						entry.setName(id.getAttribute());
						entry.setType(NOOB.tokenId);
						entry.setCategory("var");
						/* I do not know the size for undefined types...
						 * therefore I'll just create 1 word for a pointer 
						 * (which then points to the actual data) */
						int addr = new Integer(codeGenerator.getFp());
						// set address relative to frame pointer
						entry.setAddress(new Integer(codeGenerator.getSp() - addr).toString());
						codeGenerator.decreaseSp(4);
						symboltable.put(entry);
					}
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

		
		while(isVarInit(k) || isVarDecl(k)) {
			k = s.lookupToken();
		}
		isMain(k);
		while(isFunction(s.lookupToken()));
		
		// check whether every called function is really implemented
		if(!ErrorReporter.isError() && !functionAccessManager.check()) {
			ErrorReporter.markError("Not all functions have been correctly implemented: " + 
					functionAccessManager.getUndefinedFunctions(), false);
		}
		
		return true;
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
			
			symboltable = new Symboltable("main", symboltable);
			codeGenerator.put("main: ");
			codeGenerator.setFPisSP();
			
			Keyword next = null;
			while(isStatement(next = s.lookupToken()));
			if(!(next instanceof KTHXBYE)) {
				ErrorReporter.markError("Missing 'KTHXBYE' keyword");
			}
			
			codeGenerator.setSPisFP();
			// exit syscall:
			codeGenerator.put("ADDI", "$v0", "10");
			codeGenerator.put("syscall");
			
			Set<java.lang.String> registers = symboltable.getRegInScopeLevel();
			for(java.lang.String r : registers) {
				codeGenerator.releaseRegister(r);
			}
			codeGenerator.nft = codeGenerator.nft - registers.size();
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
		} else if(isPrint(keyword)) {
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
		if(keyword instanceof CALL) {
			Keyword funcId = lookAhead();
			if(!isIdentifier(funcId)) {
				ErrorReporter.markError("Expected function name after 'CALL' keyword");
			}
			adjustSrcPointer();
			
			Keyword k = s.lookupToken();
			int i = s.getPointerBeforeToken();
			
			ArrayList<java.lang.String> argTypes = new ArrayList<java.lang.String>();
			while(isOperation(k)) {
				k = s.lookupToken();
				i = s.getPointerBeforeToken();
				if(currentSymboltableEntry == null) {
					currentSymboltableEntry = symboltable.get("IT");
				}
				if(currentSymboltableEntry.getCategory().equals("heap")) {
					// TODO: change size
					memoryManager.increaseReferenceCount(new Integer(currentSymboltableEntry.getHeap()), 1);
				}
				/* store arguments in $a0 - $a3 */
				if(currentSymboltableEntry.getCategory().equals("reg")) {
					codeGenerator.move2Args(currentSymboltableEntry.getReg());
				} else if(currentSymboltableEntry.getCategory().equals("const")) {
					if(currentSymboltableEntry.getType().equals(NUMBR.tokenId)) {
						java.lang.String reg = codeGenerator.loadImmediately(
								currentSymboltableEntry.getAttribute());
						codeGenerator.move2Args(reg);
					}
				} else if(currentSymboltableEntry.getCategory().equals("var")) {
					java.lang.String reg = codeGenerator.loadWord(
							currentSymboltableEntry.getAddress());
					codeGenerator.move2Args(reg);
				}
				argTypes.add(currentSymboltableEntry.getType());
			}
			
			s.setSrcPointer(i);
			
			// linker must check whether function exists or not
			FunctionItem functionItem = new FunctionItem();
			functionItem.setFunctionName(funcId.getAttribute());
			functionItem.setArgumentTypes(argTypes);
			functionAccessManager.addFunctionCaller(functionItem);
			
			// call function
			codeGenerator.put("jal", funcId.getAttribute());
			
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isPrint(Keyword keyword) throws ParseException {
		if(keyword instanceof VISIBLE) {
			Keyword lookahead = lookAhead();
			if(isStringOp(lookahead)) {
				
				adjustSrcPointer();
				
				codeGenerator.put("ADDI", "$v0", "4");
				if(currentSymboltableEntry.getCategory().equals("reg")) {
					codeGenerator.put("move", "$a0", currentSymboltableEntry.getReg());
				} else if(currentSymboltableEntry.getCategory().equals("var")) {
					/* address to heap is stored on stack 
					 * -> add content of stack into register 
					 * (this should be equivalent to loading the address)*/
					codeGenerator.put("lw", "$a0", currentSymboltableEntry.getAddress());
				} else if(currentSymboltableEntry.getCategory().equals("const")) {
					StringLib.init(codeGenerator, memoryManager);
					StringLib.storeString(currentSymboltableEntry);
					codeGenerator.put("la", "$a0", currentSymboltableEntry.getHeap());
//					// TODO: assign and load address
//					int sizeOfStr = currentSymboltableEntry.getAttribute().length();
//					
//					int start = memoryManager.allocateMemory(sizeOfStr + 1);
//	//				codeGenerator.decreaseSp(wordAligned);
//					currentSymboltableEntry.setHeap("hp" + new Integer(start).toString());
//					int blockAmount = 0;
//					if(sizeOfStr % memoryManager.BLOCKSIZE != 0) {
//						blockAmount = ((int) (sizeOfStr / memoryManager.BLOCKSIZE)) +1;
//					} else {
//						blockAmount = (int)( sizeOfStr / memoryManager.BLOCKSIZE);
//					}
//					currentSymboltableEntry.setHeapsize(blockAmount);
//					currentSymboltableEntry.setCategory("heap");
//					codeGenerator.put("la", "$a0", "hp" + new Integer(start).toString());
				}
			} else if(isNumOp(lookahead)) {
				adjustSrcPointer();
				
				codeGenerator.put("ADDI", "$v0", "1");
				
				if(currentSymboltableEntry.getCategory().equals("reg")) {
					codeGenerator.put("move", "$a0", currentSymboltableEntry.getReg());
				} else if(currentSymboltableEntry.getCategory().equals("var")) {
					java.lang.String reg = codeGenerator.loadWord(currentSymboltableEntry.getAddress());
					codeGenerator.put("move", "$a0", reg);
					currentSymboltableEntry.setReg(reg);
					currentSymboltableEntry.setCategory("reg");
				} else if(currentSymboltableEntry.getCategory().equals("const")) {
					java.lang.String reg = 
							codeGenerator.loadImmediately(currentSymboltableEntry.getAttribute());
					codeGenerator.put("move", "$a0", reg);
					currentSymboltableEntry.setReg(reg);
					currentSymboltableEntry.setCategory("reg");
				}
			} else {
				ErrorReporter.markError("Expected a string");
			}
			codeGenerator.put("syscall");
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
					ErrorReporter.markError("Missing 'OF' keyword");
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
			
			if(symboltable == null) { 
				symboltable = new Symboltable("main", null);
				System.out.println("THIS SHOULD NEVER HAPPEN");
			}
			
			if(!isExpr(s.lookupToken())) {
				ErrorReporter.markError("Expected Expression");
			}
			
			if(!(currentSymboltableEntry == null || ErrorReporter.isError() == true)) {
				
				// store variable in register
				if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("reg")) {
					arg2 = currentSymboltableEntry.getReg();
				} else if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("var")) {
					// currentKeyword.getAddress() contains address - let's load it into a register
					arg2 = codeGenerator.loadWord(currentSymboltableEntry.getAddress());
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
					ErrorReporter.markError("Compiler is missing the symbol category");
				}
			} else {
				if(currentSymboltableEntry == null)
					ErrorReporter.markError("Variable not inititalized");
			}
			
			
			if(!(lookAhead() instanceof AN)) {
				ErrorReporter.markError("Missing 'AN' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!isExpr(s.lookupToken())) {
				ErrorReporter.markError("Expected expression");
			}
			
			
			// found a symbol?
			if(!(currentSymboltableEntry == null || ErrorReporter.isError() == true)) {
				
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
						arg2 = codeGenerator.loadWord(currentSymboltableEntry.getAddress());
						loadStmt++;
						// now set the attribute to the register name
						currentSymboltableEntry.setReg(arg2);
						currentSymboltableEntry.setCategory("reg");
					} else {
						arg3 = codeGenerator.loadWord(currentSymboltableEntry.getAddress());
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
								arg1 = "0";
							}
							
							currentSymboltableEntry = new SymboltableEntry();
							currentSymboltableEntry.setName("");
							currentSymboltableEntry.setType("TROOF");
							currentSymboltableEntry.setCategory("const");
							currentSymboltableEntry.setAttribute(arg1);
						}
						currentSymboltableEntry.setNeedRefresh(true);
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
					ErrorReporter.markError("Compiler is missing the symbol category");
				}
			} else {
				if(currentSymboltableEntry == null)
					ErrorReporter.markError("Variable not inititalized");
			}
			
			return true;
		} else if(keyword instanceof NOT) {
			if(!isExpr(s.lookupToken())) {
				ErrorReporter.markError("Expected expression");
			}
			
			if(!ErrorReporter.isError()) {
				if(currentSymboltableEntry.getCategory().equals("const")) {
					if(currentSymboltableEntry.getAttribute().equals("1")) {
						currentSymboltableEntry.setAttribute("0");
					} else {
						currentSymboltableEntry.setAttribute("1");
					}
					currentSymboltableEntry.setNeedRefresh(true);
				} else { 
					java.lang.String arg1;
					if(currentSymboltableEntry.getCategory().equals("var")) {
						arg1 = codeGenerator.loadWord(currentSymboltableEntry.getAddress());
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
			if(!ErrorReporter.isError()) {
				currentSymboltableEntry = new SymboltableEntry();
				currentSymboltableEntry.setName("");
				if(keyword.getAttribute().equals("1")) {
					currentSymboltableEntry.setAttribute("1");
				} else {
					currentSymboltableEntry.setAttribute("0");
				}
				currentSymboltableEntry.setCategory("const");
				currentSymboltableEntry.setNeedRefresh(true);
				currentSymboltableEntry.setType("TROOF");
			}
			return true;
		} else if(isIdentifier(keyword)) {
			
			if(symboltable.exists(keyword.getAttribute())) {
				SymboltableEntry ste = symboltable.get(keyword.getAttribute());
				if(!ste.getType().equals(TROOF.tokenId) && !ste.getType().equals(NOOB.tokenId)) {
					//ErrorReporter.markError("Type mismatch for variable " + ste.getName());
					return false;
				} else if(ste.getType().equals(NOOB.tokenId)) {
					ste.setType(TROOF.tokenId);
				}
				
				if(!ErrorReporter.isError()) {
					if(ste.getAttribute() == null) {
						// load variable if no value exists
						java.lang.String reg;
						if(ste.getCategory().equals("var")) {
							reg = codeGenerator.loadWord(ste.getAddress());
							loadStmt++;
//							reg = codeGenerator.loadImmediately(0);
							ste.setReg(reg);
							ste.setCategory("reg");
						} else {
							reg = ste.getReg();
						}
						ste.setNeedRefresh(true);
					} else {
						ste.setCategory("const");
						ste.setNeedRefresh(true);
					}
					currentSymboltableEntry = ste;
				}
			} else {
				ErrorReporter.markError("Unkown variable " + keyword.getAttribute());
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
				ErrorReporter.markError("Expected a value to compare");
			}
			
			SymboltableEntry ste1 = currentSymboltableEntry;
			
			if(!(lookAhead() instanceof AN)) {
				ErrorReporter.markError("Missing 'AN' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!isOperation(s.lookupToken())) {
				ErrorReporter.markError("Expected a value to compare");
			}
			
			SymboltableEntry ste2 = currentSymboltableEntry;
			
			if(!ErrorReporter.isError()) {
				if(!ste1.getType().equals(ste2.getType())) {
					ErrorReporter.markError("Type mismatch. Cannot compare " +
							ste1.getType() + " and " + ste2.getType());
				}
				
				if(ste1.getType().equals(NUMBR.tokenId) || 
						ste1.getType().equals(CHAR.tokenId) ||
						ste1.getType().equals(TROOF.tokenId)) {
					// if both arguments are constants, we just compare them without creating code
					if(ste1.getCategory().equals("const") && 
							ste2.getCategory().equals("const")) {
						currentSymboltableEntry = new SymboltableEntry();
						currentSymboltableEntry.setName("");
						currentSymboltableEntry.setCategory("const");
						currentSymboltableEntry.setType(TROOF.tokenId);
						if(keyword instanceof DIFFRINT) {
							if(ste1.getAttribute().equals(ste2.getAttribute())) {
								currentSymboltableEntry.setAttribute("0");
								codeGenerator.put("addi", "$v0", "$r0", "0");
							} else {
								currentSymboltableEntry.setAttribute("1");
								codeGenerator.put("addi", "$v0", "$r0", "1");
							}
						} else if(keyword instanceof BOTH) {
							if(ste1.getAttribute().equals(ste2.getAttribute())) {
								currentSymboltableEntry.setAttribute("1");
								codeGenerator.put("addi", "$v0", "$r0", "1");
							} else {
								currentSymboltableEntry.setAttribute("0");
								codeGenerator.put("addi", "$v0", "$r0", "0");
							}
						}
					} else {
						// load ste1 in register, if necessary
						if(ste1.getCategory().equals("var")) {
							java.lang.String reg = codeGenerator.loadWord(ste1.getAddress());
							ste1.setReg(reg);
							ste1.setCategory("reg");
						} else if(ste1.getCategory().equals("const")) {
							java.lang.String reg = codeGenerator.loadImmediately(ste1.getAttribute());
							ste1.setReg(reg);
							ste1.setCategory("reg");
						}
						
						// load ste2 in register, if necessary
						if(ste2.getCategory().equals("var")) {
							java.lang.String reg = codeGenerator.loadWord(ste2.getAddress());
							ste2.setReg(reg);
							ste2.setCategory("reg");
						} else if(ste2.getCategory().equals("const")) {
							java.lang.String reg = codeGenerator.loadImmediately(ste2.getAttribute());
							ste2.setReg(reg);
							ste2.setCategory("reg");
						}
						
						// generate code for != or == expression
						if(keyword instanceof DIFFRINT) {
							codeGenerator.put("bne", ste1.getReg(), ste2.getReg(), "3");
							codeGenerator.put("addi", "$v0", "$r0", "1");
							codeGenerator.put("j", "2");
							codeGenerator.put("add", "$v0", "$r0", "$r0");
						} else if(keyword instanceof BOTH) {
							codeGenerator.put("beq", ste1.getReg(), ste2.getReg(), "3");
							codeGenerator.put("addi", "$v0", "$r0", "1");
							codeGenerator.put("j", "2");
							codeGenerator.put("add", "$v0", "$r0", "$r0");
						}
					}
				} else if(ste1.getType().equals(NUMBRZ.tokenId) ||
						ste1.getType().equals(CHARZ.tokenId) ||
						ste1.getType().equals(TROOFZ.tokenId)) {
					// if both arguments are constants, we just compare them without creating code
					if(ste1.getCategory().equals("const") && 
							ste2.getCategory().equals("const")) {
						currentSymboltableEntry = new SymboltableEntry();
						currentSymboltableEntry.setName("");
						currentSymboltableEntry.setCategory("const");
						currentSymboltableEntry.setType(TROOF.tokenId);
						if(keyword instanceof DIFFRINT) {
							if(ste1.getAttribute().equals(ste2.getAttribute())) {
								currentSymboltableEntry.setAttribute("0");
								codeGenerator.put("addi", "$v0", "$r0", "0");
							} else {
								currentSymboltableEntry.setAttribute("1");
								codeGenerator.put("addi", "$v0", "$r0", "1");
							}
						} else if(keyword instanceof BOTH) {
							if(ste1.getAttribute().equals(ste2.getAttribute())) {
								currentSymboltableEntry.setAttribute("1");
								codeGenerator.put("addi", "$v0", "$r0", "1");
							} else {
								currentSymboltableEntry.setAttribute("0");
								codeGenerator.put("addi", "$v0", "$r0", "0");
							}
						}
					} else {
						// TODO: compare strings, number-arrays and boolean-arrays
						// - strings can be compared as in StringLib.strcmp()
						// - number- and boolean arrays 
						StringLib.init(codeGenerator, memoryManager);
						StringLib.strCmp(ste1, ste2);
					}
				}
			}
			
			return true;
		} else {
			if(keyword instanceof BOTH && 
							!(lookAhead() instanceof SAEM || 
									lookAhead() instanceof OF)) {
				ErrorReporter.markError("Missing 'SAEM' OR 'OF' keyword");
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
				ErrorReporter.markError("Missing 'OF' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!isBoolOp(s.lookupToken())) {
				ErrorReporter.markError("Expected boolean variable");
			}
			
			if(!(lookAhead() instanceof AN)) {
				ErrorReporter.markError("Missing 'AN' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!(isBoolOp(s.lookupToken()))) {
				ErrorReporter.markError("Too few arguments");
			}
			
			Keyword lookupTok = lookAhead();
			while(!(lookupTok instanceof MKAY)) {
				if(!(lookupTok instanceof AN)) {
					ErrorReporter.markError("Missing 'AN' or 'MKAY' keyword");
					break;
				} else {
					adjustSrcPointer();
					if(!isBoolOp(s.lookupToken())) {
						ErrorReporter.markError("Expected boolean variable");
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
			
			symboltable = new Symboltable("main", symboltable);
			boolean skipLoop = false;
			boolean needFixUp = false;
			int jumpFixup = 0; 
			int branchFixup = 0;
			
			if(!(lookAhead() instanceof YR)) {
				ErrorReporter.markError("Missing 'YR' keyword");
			} else {
				adjustSrcPointer();
			}
			
			Keyword id_loop = lookAhead();
			if(!(id_loop instanceof Identifier)) {
				ErrorReporter.markError("Loop does not have a name");
				// search for loop end and quit
				while(!(s.lookupToken() instanceof IM) && !(lookAhead() instanceof OUTTA));
				
				if(s.lookupToken() instanceof OUTTA);
				if(s.lookupToken() instanceof YR);
				if(isIdentifier(s.lookupToken()));
				return true;
			} else {
				adjustSrcPointer();
				loopLabel = id_loop.getAttribute();
			}
			
			Keyword tok = s.lookupToken();
			if(tok instanceof YR) {
				if(!(lookAhead() instanceof Identifier)) {
					ErrorReporter.markError("Too few arguments");
				} else {
					adjustSrcPointer();
				}
				tok = s.lookupToken();
			}
			
			jumpFixup = codeGenerator.getPc();
			loadStmt = 0;
			if(tok instanceof WILE || tok instanceof TIL) {
				if(!isExpr(s.lookupToken())) {
					ErrorReporter.markError("No limiting expression provided");
				}
				jumpFixup = jumpFixup + loadStmt;
				loadStmt = 0;
				// Expression, stored in $v0
				// Branch on equal to end of loop (store loopoffset)
				// Jump on end of loop to first expression
				if(currentSymboltableEntry == null) {
					currentSymboltableEntry = symboltable.get("IT");
				} else if(currentSymboltableEntry.getCategory().equals("const")) {
					if(currentSymboltableEntry.getAttribute().equals("0")) {
						System.out.println("Skipping else if and else statements");
						skipLoop = true;
					}
				} else {
					branchFixup = codeGenerator.getPc();
					if(tok instanceof WILE) {
						// if $v0 = 0 -> skip loop
						codeGenerator.put("BEQ", "$r0", "$v0", "offset");
					} else if(tok instanceof TIL) {
						// if $v0 = 1 -> skip loop
						codeGenerator.put("BGTZ", "$v0", "offset");
					}
					needFixUp = true;
				}
				tok = s.lookupToken();
			}
			
			// temporarily switch off code generation, if skipIf is true
			// switching of code generation is realized by setting the 
			// error flag to true, even if there is no error - 
			// hope that does not create confusion
			if(skipLoop && ErrorReporter.isError() == false) {
				ErrorReporter.setError(true);
			} else {
				skipLoop = false;
			}
			
			while(isStatement(tok)) {
				tok = s.lookupToken();
				if(currentSymboltableEntry == null)
					currentSymboltableEntry = symboltable.get("IT");
				if(currentSymboltableEntry.isNeedRefresh()) {
					codeGenerator.put(
							"ADDI", 
							currentSymboltableEntry.getReg(), 
							"$r0", 
							currentSymboltableEntry.getAttribute());
				}
			}
			
			if(skipLoop) {
				ErrorReporter.setError(true);
			}
			
			if(!(tok instanceof IM)) {
				ErrorReporter.markError("Expected loop end");
			}
			
			if(!(lookAhead() instanceof OUTTA)) {
				ErrorReporter.markError("Missing 'OUTTA' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!(lookAhead() instanceof YR)) {
				ErrorReporter.markError("Missing 'YR' label");
			} else {
				adjustSrcPointer();
			}
			
			Keyword id = lookAhead();
			if(!(id instanceof Identifier)) {
				ErrorReporter.markError("No loop end label provided");
			} else {
				adjustSrcPointer();
			}
			
			if(!(loopLabel.equals(id.getAttribute()))) {
				ErrorReporter.markError("Loop end label does " +
					"not match the loop begin label");
			}
			
			if(!ErrorReporter.isError()) {
				codeGenerator.put("J", new Integer(jumpFixup - codeGenerator.getPc()).toString());
			}
			if(needFixUp && !ErrorReporter.isError()) {
				codeGenerator.fixUp(branchFixup, codeGenerator.getPc() - branchFixup);
			}
			
			Set<java.lang.String> registers = symboltable.getRegInScopeLevel();
			for(java.lang.String r : registers) {
				codeGenerator.releaseRegister(r);
			}
			codeGenerator.nft = codeGenerator.nft - registers.size();
			symboltable = symboltable.getOutterTable();
			
			return true;
		} else {
			return false;
		}
	}

	public boolean isIf(Keyword keyword) throws ParseException {
		if(keyword instanceof ORLY) {
			
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
				codeGenerator.put("BEQ", "$r0", "$v0", "offset");
				needFixUp = true;
			}
			
			if(!(lookAhead() instanceof YA)) {
				ErrorReporter.markError("Missing 'YA' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!(lookAhead() instanceof RLY)) {
				ErrorReporter.markError("Missing 'RLY' keyword");
			} else {
				adjustSrcPointer();
			}
			
			symboltable = new Symboltable(symboltable.getName()+"_if", symboltable);
			
			// temporarily switch off code generation, if skipIf is true
			// switching of code generation is realized by setting the 
			// error flag to true, even if there is no error - 
			// hope that does not create confusion
			if(skipIf && ErrorReporter.isError() == false) {
				ErrorReporter.setError(true);
			} else {
				skipIf = false;
			}
			
			Keyword k = null;
			if(!isStatement(k = s.lookupToken())) {
				ErrorReporter.markError("Expected statement after condition");
			}
			
			while(isStatement(k = s.lookupToken()));
			
			if(!skipIf && !ErrorReporter.isError()) {
				if(!currentSymboltableEntry.getName().equals("IT") && currentSymboltableEntry.getCategory().equals("const")) {
					java.lang.String result = currentSymboltableEntry.getAttribute();
					currentSymboltableEntry = symboltable.get("IT");
					codeGenerator.put("ADDI", currentSymboltableEntry.getReg(), "$r0", result);
				}
				if(currentSymboltableEntry.isNeedRefresh()) {
					if(currentSymboltableEntry.getCategory().equals("var")) {
						java.lang.String reg = codeGenerator.loadWord(currentSymboltableEntry.getAddress());
						currentSymboltableEntry.setReg(reg);
						currentSymboltableEntry.setCategory("reg");
					}
					if(currentSymboltableEntry.getAttribute() != null) {
						codeGenerator.put(
								"ADDI", 
								currentSymboltableEntry.getReg(), 
								"$r0", 
								currentSymboltableEntry.getAttribute());
					} else if(currentSymboltableEntry.getAttribute() == null) {
						codeGenerator.put(
								"MOVE", "$v0", 
								currentSymboltableEntry.getReg());
					}
				}
				jumpFixupLine = codeGenerator.getPc();
				codeGenerator.put("J", "offset");
			}
			
			// switch code generation on again
			if(skipIf) {
				ErrorReporter.setError(false);
			}
			
//				if(skipElse && error == false) {
//					error = true;
//				} else {
//					skipElse = false;
//				}
			
			Set<java.lang.String> registersIf = symboltable.getRegInScopeLevel();
			for(java.lang.String r : registersIf) {
				codeGenerator.releaseRegister(r);
			}
			codeGenerator.nft = codeGenerator.nft - registersIf.size();
			symboltable = symboltable.getOutterTable();
			
			while(k instanceof MEBBE) {
				
				symboltable = new Symboltable(symboltable.getName()+"_elseif", symboltable);
				
				if(skipElse && ErrorReporter.isError() == false) {
					ErrorReporter.setError(true);
				} else {
					skipElse = false;
					System.out.println("Computing else statement");
				}
				
				if(needFixUp && !ErrorReporter.isError()) {
					codeGenerator.fixUp(fixupLine, codeGenerator.getPc() - fixupLine);
				}
				
				if(!isExpr(s.lookupToken())) {
					ErrorReporter.markError("Expected expression after 'MEBBE'");
				}
				if(currentSymboltableEntry.isNeedRefresh()) {
					if(currentSymboltableEntry.getAttribute() != null) {
						codeGenerator.put(
								"ADDI", 
								currentSymboltableEntry.getReg(), 
								"$r0", 
								currentSymboltableEntry.getAttribute());
					} else if(currentSymboltableEntry.getAttribute() == null) {
						codeGenerator.put(
								"MOVE", "$v0", 
								currentSymboltableEntry.getReg());
					}
				}
				
				if(currentSymboltableEntry ==  null) {
					currentSymboltableEntry = symboltable.get("IT");
				}
				if(!ErrorReporter.isError() && currentSymboltableEntry.getCategory().equals("const")) {
					if(!skipFollowing) {
						if(currentSymboltableEntry.getAttribute().equals("1")) {
							skipFollowing = true;
							System.out.println("Skipping the following statements");
						} else {
							skipElse = true;
							ErrorReporter.setError(true);
							System.out.println("Skipping this statement");
						}
					} else {
						System.out.println("Skipped following statement");
					}
				} else if(!ErrorReporter.isError()){
					fixupLine = codeGenerator.getPc();
					codeGenerator.put("BEQ", "$r0", "$v0", "offset");
					needFixUp = true;
				}
				
				if(!isStatement(k = s.lookupToken())) {
					ErrorReporter.markError("Expected statement after condition");
				}
				
				while(isStatement(k = s.lookupToken()));
				
				if(skipElse && !skipFollowing) {
					ErrorReporter.setError(false);
					skipElse = false;
				} else if(skipFollowing) {
					skipElse = true;
				}
				
				if(!ErrorReporter.isError()) {
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
				Set<java.lang.String> registers = symboltable.getRegInScopeLevel();
				for(java.lang.String r : registers) {
					codeGenerator.releaseRegister(r);
				}
				codeGenerator.nft = codeGenerator.nft - registers.size();
				symboltable = symboltable.getOutterTable();
			}
			if(k instanceof NO) {
				
				symboltable = new Symboltable(symboltable.getName()+"_else", symboltable);
				
				if(skipElse && ErrorReporter.isError() == false) {
					ErrorReporter.setError(true);
				} else {
					skipElse = false;
				}
				
				if(!(lookAhead() instanceof WAI)) {
					ErrorReporter.markError("Missing 'WAI' keyword");
				} else {
					adjustSrcPointer();
				}
				
				if(needFixUp && !ErrorReporter.isError()) {
					codeGenerator.fixUp(fixupLine, codeGenerator.getPc() - fixupLine);
				}
				if(!isStatement(k = s.lookupToken())) {
					ErrorReporter.markError("Expected statement after condition");
				}
				
				while(isStatement(k = s.lookupToken()));
				
				if(!ErrorReporter.isError()) {
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
				
				Set<java.lang.String> registers = symboltable.getRegInScopeLevel();
				for(java.lang.String r : registers) {
					codeGenerator.releaseRegister(r);
				}
				codeGenerator.nft = codeGenerator.nft - registers.size();
				symboltable = symboltable.getOutterTable();
			}
			
			if(skipElse || skipFollowing) {
				ErrorReporter.setError(false);
			}
			
			if(!(k instanceof OIC)) {
				ErrorReporter.markError("Missing 'OIC' keyword");
			}
			
			if(needFixUp && !ErrorReporter.isError()) {
				codeGenerator.fixUp(fixupLine, codeGenerator.getPc() - fixupLine);
			}
			
//			symboltable = symboltable.getOutterTable();
			
			return true;
		} else {
			return false;
		}
	}

	public boolean isOperation(Keyword keyword) throws ParseException {
		return isNumArray(keyword) || isBoolArray(keyword) ||
				isNumOp(keyword) || isBoolOp(keyword) || isStringOp(keyword);
	}

	public boolean isStringOp(Keyword keyword) {
		if(keyword instanceof String) {
			if(!ErrorReporter.isError()) {
				currentSymboltableEntry = new SymboltableEntry();
				currentSymboltableEntry.setName("");
				currentSymboltableEntry.setType(CHARZ.tokenId);
				currentSymboltableEntry.setCategory("const");
				currentSymboltableEntry.setAttribute(keyword.getAttribute());
			}
			return true;
		} else if(keyword instanceof Identifier) {
			if(symboltable == null) {
				symboltable = new Symboltable("main", null);
				System.out.println("THIS SHOULD NEVER HAPPEN");
			}
			if(symboltable.exists(keyword.getAttribute())) {
				SymboltableEntry ste = symboltable.get(keyword.getAttribute());
				if(!ste.getType().equals(CHARZ.tokenId) && !ste.getType().equals(NOOB.tokenId)) {
					//ErrorReporter.markError("Type mismatch for variable " + ste.getName());
					return false;
				} else if(ste.getType().equals(NOOB.tokenId)) {
					ste.setType(CHARZ.tokenId);
				}
				if(!ErrorReporter.isError()) {
					if(ste.getAttribute() == null) {
						// load variable if no value exists
						java.lang.String reg;
						if(ste.getCategory().equals("var")) {
//							reg = codeGenerator.loadImmediately(0);
							reg = codeGenerator.loadWord(ste.getAddress());
							loadStmt++;
							/* determine whether the loaded variable is in 
							 * lowest scope and add register address, 
							 * if this is the case.
							 * The check must be done to ensure that the
							 * variable gets loaded into a register if it is
							 * defined in an upper scope, loaded in a lower scope
							 * and used in another lower scope as well.
							 */
							if(symboltable.isInScopeLevel(ste.getName())) {
								ste.setReg(reg);
								ste.setCategory("reg");
								currentSymboltableEntry = ste;
							} else {
								// add symbol to current (lowest) scope
								SymboltableEntry tmpSte = new SymboltableEntry();
								tmpSte.setName(ste.getName());
								tmpSte.setReg(reg);
								tmpSte.setCategory("reg");
								tmpSte.setType(ste.getType());
								symboltable.put(tmpSte);
								currentSymboltableEntry = tmpSte;
							}
						} else {
							currentSymboltableEntry = ste;
						}
					} else {
						ste.setCategory("const");
						ste.setNeedRefresh(true);
						currentSymboltableEntry = ste;
					}
				}
			} else {
				ErrorReporter.markError("Unkown variable " + keyword.getAttribute());
			}
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
				ErrorReporter.markError("Missing 'OF' keyword");
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
			
			if(symboltable == null) {
				symboltable = new Symboltable("main", null);
				System.out.println("THIS SHOULD NEVER HAPPEN");
			}
			
			if(!isNumOp(s.lookupToken())) {
				ErrorReporter.markError("Expected numerical operation or number");
			}
			
			// look up identifier in the symboltable and store argument in register or
			// calculate immediately
			if(!(currentSymboltableEntry == null || ErrorReporter.isError() == true)) {
			
				// store variable in register
				if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("reg")) {
					arg2 = currentSymboltableEntry.getReg();
				} else if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("var")) {
					// currentKeyword.getAddress() contains address - let's load it into a register
					arg2 = codeGenerator.loadWord(currentSymboltableEntry.getAddress());
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
					ErrorReporter.markError("Compiler is missing the symbol category");
				}
			} else {
				if(currentSymboltableEntry == null)
					ErrorReporter.markError("Variable not inititalized");
			}
			
			// after the first parameter, we are expecting an AN keyword
			if(!(lookAhead() instanceof AN)) {
				ErrorReporter.markError("Missing 'AN' keyword");
			} else {
				adjustSrcPointer();
			}
			
			// ...and a second parameter
			if(!isNumOp(s.lookupToken())) {
				ErrorReporter.markError("Expected numerical operation or number");
			}
			
			// found a symbol?
			if(!(currentSymboltableEntry == null || ErrorReporter.isError() == true)) {
				
				// store variable in register
				if(currentSymboltableEntry.getCategory() != null && 
						currentSymboltableEntry.getCategory().equals("reg")) {
					// if the first parameter was a constant
					if(arg2IsConst) {
						// when operation is '-' or '/', we cannot make use of the commutative law
						// thus, load the number into a register
						if(op.equals("SUBI") || op.equals("DIVI")) {
							arg2 = codeGenerator.loadImmediately(arg3);
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
							arg2 = codeGenerator.loadImmediately(arg3);
							op = op.substring(0, op.length()-1);
							arg2IsConst = false;
						}
					}
					
					if(arg2IsConst) {
						// currentKeyword.getAddress() contains address - let's load it into a register
						arg2 = codeGenerator.loadWord(currentSymboltableEntry.getAddress());
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
						arg3 = codeGenerator.loadWord(currentSymboltableEntry.getAddress());
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
								ErrorReporter.markError("Divison by 0");
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
						currentSymboltableEntry.setNeedRefresh(true);
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
					ErrorReporter.markError("Compiler is missing the symbol category");
				}
			} else {
				if(currentSymboltableEntry == null)
					ErrorReporter.markError("Variable not inititalized");
			}
			
			return true;
		} else if(isNum(keyword)) {
			if(!ErrorReporter.isError()) {
				currentSymboltableEntry = new SymboltableEntry();
				currentSymboltableEntry.setName("");
				currentSymboltableEntry.setType("NUMBR");
				currentSymboltableEntry.setCategory("const");
				currentSymboltableEntry.setAttribute(keyword.getAttribute());
			}
			return true;
		} else if(isIdentifier(keyword)) {
			if(symboltable == null) {
				symboltable = new Symboltable("main", null);
				System.out.println("THIS SHOULD NEVER HAPPEN");
			}
			if(symboltable.exists(keyword.getAttribute())) {
				SymboltableEntry ste = symboltable.get(keyword.getAttribute());
				if(!ste.getType().equals("NUMBR") && !ste.getType().equals("NOOB")) {
					//ErrorReporter.markError("Type mismatch for variable " + ste.getName());
					return false;
				} else if(ste.getType().equals("NOOB")) {
					ste.setType("NUMBR");
				}
				
				if(!ErrorReporter.isError()) {
					if(ste.getAttribute() == null) {
						// load variable if no value exists
						java.lang.String reg;
						if(ste.getCategory().equals("var")) {
//							reg = codeGenerator.loadImmediately(0);
							reg = codeGenerator.loadWord(ste.getAddress());
							loadStmt++;
							/* determine whether the loaded variable is in 
							 * lowest scope and add register address, 
							 * if this is the case.
							 * The check must be done to ensure that the
							 * variable gets loaded into a register if it is
							 * defined in an upper scope, loaded in a lower scope
							 * and used in another lower scope as well.
							 */
							if(symboltable.isInScopeLevel(ste.getName())) {
								ste.setReg(reg);
								ste.setCategory("reg");
								currentSymboltableEntry = ste;
							} else {
								// add symbol to current (lowest) scope
								SymboltableEntry tmpSte = new SymboltableEntry();
								tmpSte.setName(ste.getName());
								tmpSte.setReg(reg);
								tmpSte.setCategory("reg");
								tmpSte.setType(ste.getType());
								symboltable.put(tmpSte);
								currentSymboltableEntry = tmpSte;
							}
						} else {
							currentSymboltableEntry = ste;
						}
					} else {
						ste.setCategory("const");
						ste.setNeedRefresh(true);
						currentSymboltableEntry = ste;
					}
				}
			} else {
				ErrorReporter.markError("Unkown variable " + keyword.getAttribute());
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isPointerArray(Keyword keyword) {
		if(keyword instanceof Identifier) { 
			SymboltableEntry ste = symboltable.get(keyword.getAttribute());
			if(ste != null) {
				if(ste.getType().equals(NUMBRZ.tokenId) || 
						ste.getType().equals(TROOFZ.tokenId) ||
						ste.getType().equals(CHARZ.tokenId))
					return true;
				else
					return false;
			} else {
				ErrorReporter.markError("Unkown variable " + keyword.getAttribute());
			}
		}
		return false;
	}
	
	public boolean isBoolArray(Keyword keyword) {
		if(keyword instanceof Identifier) { 
			SymboltableEntry ste = symboltable.get(keyword.getAttribute());
			if(ste != null) {
				if(ste.getType().equals(TROOFZ.tokenId))
					return true;
				else
					return false;
			} else {
				ErrorReporter.markError("Unkown variable " + keyword.getAttribute());
			}
		} else if(isBool(keyword)) {
			if(isBool(lookAhead())) {
				adjustSrcPointer();
				while(isBool(lookAhead())) adjustSrcPointer();
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public boolean isNumArray(Keyword keyword) {
		if(keyword instanceof Identifier) { 
			SymboltableEntry ste = symboltable.get(keyword.getAttribute());
			if(ste != null) {
				if(ste.getType().equals(NUMBRZ.tokenId))
					return true;
				else
					return false;
			} else {
				ErrorReporter.markError("Unkown variable " + keyword.getAttribute());
			}
		} else if(keyword instanceof Int) {
			if(lookAhead() instanceof Int) {
				adjustSrcPointer();
				while(lookAhead() instanceof Int) adjustSrcPointer();
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	public boolean isNum(Keyword keyword) {
		return keyword instanceof Int;
	}
	
	public boolean isVarAssign(Keyword keyword) throws ParseException {
		if(isIdentifier(keyword)) {
			if(symboltable == null) {
				symboltable = new Symboltable("main", null);
				System.out.println("THIS SHOULD NEVER HAPPEN");
			}
			
			Keyword k = lookAhead();
			if(k instanceof R) {
				adjustSrcPointer();
				if(!isOperation(s.lookupToken())) {
					ErrorReporter.markError("Expected value");
				}
				// TODO: check for type!!
				SymboltableEntry ste = symboltable.get(((Identifier) keyword).getAttribute());
				if(ste == null)
					ErrorReporter.markError("Unknown variable " + ((Identifier) keyword).getAttribute());
//				currentSymboltableEntry = ste;
				if(ste != null) {
					if(currentSymboltableEntry.getCategory().equals("const")) {
						if(currentSymboltableEntry.getAttribute() != null)
							ste.setAttribute(currentSymboltableEntry.getAttribute());
						if(currentSymboltableEntry.getType() != null)
							ste.setType(currentSymboltableEntry.getType());
						if(!ste.getCategory().equals("const")) {
							ste.setNeedRefresh(true);
						}
					} else if(currentSymboltableEntry.getName().equals("IT") && 
							ste.getCategory().equals("reg")) {
						codeGenerator.put("MOVE", ste.getReg(), currentSymboltableEntry.getReg());
					}
					currentSymboltableEntry = ste;
				}
				return true;
			} else {
				ErrorReporter.markError("Missing 'R' or 'IS NOW A' keywords after variable");
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	private boolean checkTypeConversions(java.lang.String lastType, Keyword newType, Identifier identifier) {
		TypeConversion.init(codeGenerator, symboltable, memoryManager);
		if(isType(newType)) {
			if(lastType == null) lastType = NOOB.tokenId;
			if(!lastType.equals(NOOB.tokenId)) {
				// change type of identifier from integer to...
				if(lastType.equals(NUMBR.tokenId)) {
					// ... NULL
					if(newType.getTokenID().equals(NOOB.tokenId)) {
						return false;
					}
					// ... int (no change)
					else if(newType.getTokenID().equals(NUMBR.tokenId)) {
						return true;
					}
					// ... int[]
					else if(newType.getTokenID().equals(NUMBRZ.tokenId)) {
						return false;
					}
					// ... boolean
					else if(newType.getTokenID().equals(TROOF.tokenId)) {
						TypeConversion.int2bool(identifier);
						return true;
					}
					// ... boolean[]
					else if(newType.getTokenID().equals(TROOFZ.tokenId)) {
						return false;
					}
					// ... a char
					else if(newType.getTokenID().equals(CHAR.tokenId)) {
						TypeConversion.int2char(identifier);
						return true;
					} else if(newType.getTokenID().equals(CHARZ.tokenId)) {
						if(!isNum(lookAhead())) {
							ErrorReporter.markError("Missing size of char array");
						} else {
							// if the size has changed, assign new heap space and copy each word
							int arraySize = new Integer(lookAhead().getAttribute());
							TypeConversion.int2string(identifier, arraySize);
						}
						return true;
					}
				}
				// change type of identifier from int[] to ...
				else if(lastType.equals(NUMBRZ.tokenId)) {
					// ... NULL
					if(newType.getTokenID().equals(NOOB.tokenId)) {
						TypeConversion.intarray2null(identifier);
						return true;
					} else if(newType.getTokenID().equals(NUMBR.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(NUMBRZ.tokenId)) {
						if(!isNum(lookAhead())) {
							ErrorReporter.markError("Missing size of char array");
						} else {
							// if the size has changed, assign new heap space and copy each word
							int arraySize = new Integer(lookAhead().getAttribute());
							int blocks = 0;
							if(arraySize % memoryManager.BLOCKSIZE == 0)
								blocks = arraySize / memoryManager.BLOCKSIZE;
							else
								blocks = ((int) arraySize / memoryManager.BLOCKSIZE) + 1;
							SymboltableEntry ste = symboltable.get(identifier.getAttribute());
							if(ste != null) {
								if(blocks > ste.getHeapsize()) {
									java.lang.String heapStart = "hp" + 
										new Integer(memoryManager.allocateMemory(blocks)).toString();
									// load word aus ste.getHeap in reg
									java.lang.String regFrom = codeGenerator.getNextFreeTemporary();
									codeGenerator.put("la", regFrom, ste.getHeap());
									java.lang.String regTo = codeGenerator.getNextFreeTemporary();
									codeGenerator.put("la", regTo, heapStart);
									for(int i=0; i<(ste.getHeapsize() * memoryManager.BLOCKSIZE); i=i+4) {
										java.lang.String regTmp = codeGenerator.getNextFreeTemporary();
										codeGenerator.put("lw", regTmp, new Integer(i).toString()+"(" + regFrom + ")");
										codeGenerator.put("sw", regTmp, new Integer(i).toString()+"(" + regFrom + ")");
									}
									// store word aus reg in heapStart
									ste.setHeap(heapStart);
									ste.setHeapsize(blocks);
								}
							}
						}
						return true;
					} else if(newType.getTokenID().equals(TROOF.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(TROOFZ.tokenId)) {
						if(!isNum(lookAhead())) {
							ErrorReporter.markError("Missing size of boolean array");
						} else {
							adjustSrcPointer();
						}
						SymboltableEntry ste = symboltable.get(identifier.getAttribute());
						if(ste.getCategory().equals("const")) {
							// convert to bool[]
							java.lang.String[] array = ste.getAttribute().split(" ");
							StringBuilder newAttribute = new StringBuilder();
							for(int i=0; i<array.length; i++) {
								if(!array[i].isEmpty()) {
									if(new Integer(array[i]) > 0) {
										newAttribute.append("1 ");
									} else {
										newAttribute.append("0 ");
									}
								}
							}
							ste.setAttribute(ste.getAttribute().replaceAll(" ", ""));
						} else {
							// TODO: delete currently assigned heap mem and assign new heap??
						}
						return true;
					} else if(newType.getTokenID().equals(CHAR.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(CHARZ.tokenId)) {
						if(!isNum(lookAhead())) {
							ErrorReporter.markError("Missing size of char array");
						} else {
							adjustSrcPointer();
						}
						SymboltableEntry ste = symboltable.get(identifier.getAttribute());
						if(ste.getCategory().equals("const")) {
							// convert to string
							ste.setAttribute(ste.getAttribute().replaceAll(" ", ""));
						} else {
							// convert each int into a character + 0byte
						}
					}
				}
				// change type of identifier from char to...
				else if(lastType.equals(CHAR.tokenId)) {
					// ... NULL
					if(newType.getTokenID().equals(NOOB.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(NUMBR.tokenId)) {
						return true;
					} else if(newType.getTokenID().equals(NUMBRZ.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(TROOF.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(TROOFZ.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(CHAR.tokenId)) {
						return true;
					} else if(newType.getTokenID().equals(CHARZ.tokenId)) {
						// assign new heap memory with blocksize = 1
						// put character + 0byte into heap
						// put heap-address into stack (is stack mem already allocated)
						return true;
					}
				}
				// change type of identifier from string to...
				else if(lastType.equals(CHARZ.tokenId)) {
					// ... NULL
					if(newType.getTokenID().equals(NOOB.tokenId)) {
						SymboltableEntry ste = symboltable.get(identifier.getAttribute());
						if(ste != null) {
							java.lang.String heapstart = ste.getHeap();
							// free memory
							memoryManager.freeMemory(heapstart, ste.getHeapsize());
						}
						// erase heap and heapsize
						ste.setHeap("");
						ste.setHeapsize(0);
						return true;
					} else if(newType.getTokenID().equals(NUMBR.tokenId)) {
						// check whether characters are really numbers
						SymboltableEntry ste = symboltable.get(identifier.getAttribute());
						if(ste.getCategory().equals("const")) {
							try {
								new Integer(ste.getAttribute());
							} catch (NumberFormatException e) {
								ErrorReporter.markError("Cannot convert string to number");
							}
						}
						return true;
					} else if(newType.getTokenID().equals(NUMBRZ.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(TROOF.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(TROOFZ.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(CHAR.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(CHARZ.tokenId)) {
						if(!isNum(lookAhead())) {
							ErrorReporter.markError("Missing size of char array");
						} else {
							// if the size has changed, assign new heap space and copy each word
							int arraySize = new Integer(lookAhead().getAttribute());
							int blocks = 0;
							if(arraySize % memoryManager.BLOCKSIZE == 0)
								blocks = arraySize / memoryManager.BLOCKSIZE;
							else
								blocks = ((int) arraySize / memoryManager.BLOCKSIZE) + 1;
							SymboltableEntry ste = symboltable.get(identifier.getAttribute());
							if(ste != null) {
								if(blocks > ste.getHeapsize()) {
									java.lang.String heapStart = "hp" + 
										new Integer(memoryManager.allocateMemory(blocks)).toString();
									// load word aus ste.getHeap in reg
									java.lang.String regFrom = codeGenerator.getNextFreeTemporary();
									codeGenerator.put("la", regFrom, ste.getHeap());
									java.lang.String regTo = codeGenerator.getNextFreeTemporary();
									codeGenerator.put("la", regTo, heapStart);
									for(int i=0; i<(ste.getHeapsize() * memoryManager.BLOCKSIZE); i=i+4) {
										java.lang.String regTmp = codeGenerator.getNextFreeTemporary();
										codeGenerator.put("lw", regTmp, new Integer(i).toString()+"(" + regFrom + ")");
										codeGenerator.put("sw", regTmp, new Integer(i).toString()+"(" + regFrom + ")");
									}
									// store word aus reg in heapStart
									ste.setHeap(heapStart);
									ste.setHeapsize(blocks);
								}
							}
						}
						return true;
					}
				}
				// change type of identifier from boolean to...
				else if(lastType.equals(TROOF.tokenId)) {
					// ... NULL
					if(newType.getTokenID().equals(NOOB.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(NUMBR.tokenId)) {
						if(identifier.getAttribute().equals(WIN.tokenId))
							identifier.setAttribute("1");
						else if(identifier.getAttribute().equals(FAIL.tokenId))
							identifier.setAttribute("0");
						return true;
					} else if(newType.getTokenID().equals(NUMBRZ.tokenId)) {
						return false;
					}
					// ... boolean
					else if(newType.getTokenID().equals(TROOF.tokenId)) {
						return true;
					} else if(newType.getTokenID().equals(TROOFZ.tokenId)) {
						return false;
					}
					// ... a string/char
					else if(newType.getTokenID().equals(CHAR.tokenId) || 
							newType.getTokenID().equals(CHARZ.tokenId)) {
						return false;
					}
				}
				// change type of identifier from boolean array to...
				else if(lastType.equals(TROOFZ.tokenId)) {
					// ... NULL
					if(newType.getTokenID().equals(NOOB.tokenId)) {
						// set heap address to null
						SymboltableEntry ste = symboltable.get(identifier.getAttribute());
						if(ste != null) {
							java.lang.String heapstart = ste.getHeap();
							// free memory
							memoryManager.freeMemory(heapstart, ste.getHeapsize());
						}
						// erase heap and heapsize
						ste.setHeap("");
						ste.setHeapsize(0);
						return true;
					} else if(newType.getTokenID().equals(NUMBR.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(NUMBRZ.tokenId)) {
						SymboltableEntry ste = symboltable.get(identifier.getAttribute());
						if(ste != null) {
							if(ste.getCategory().equals("const")) {
								java.lang.String[] values = ste.getAttribute().split(" ");
								StringBuilder newAttribute = new StringBuilder();
								for(java.lang.String s : values) {
									if(s.equals(WIN.tokenId)) {
										newAttribute.append("1 ");
									} else if(s.equals(FAIL.tokenId)) {
										newAttribute.append("0 ");
									}
								}
							} else if(ste.getCategory().equals("var") || ste.getCategory().equals("reg")) {
								
							}
						}
						return true;
					}
					// ... boolean
					else if(newType.getTokenID().equals(TROOF.tokenId)) {
						return false;
					} else if(newType.getTokenID().equals(TROOFZ.tokenId)) {
						return true;
					}
					// ... a string/char
					else if(newType.getTokenID().equals(CHAR.tokenId) || 
							newType.getTokenID().equals(CHARZ.tokenId)) {
						return false;
					}
				} else {
					return false;
				}
			} 
			// If the identifier was untyped, we change from NULL to...
			else {
				// ... NULL
				if(newType.getTokenID().equals(NOOB.tokenId)) {
					// do nothing
				}
				// ... int (no change)
				else if(newType.getTokenID().equals(NUMBR.tokenId)) {
					TypeConversion.null2int(identifier);
				}
				// ... int[]
				else if(newType.getTokenID().equals(NUMBRZ.tokenId)) {
					if(!isNum(lookAhead())) {
						ErrorReporter.markError("Missing size of char array");
					} else {
						int arraySize = new Integer(lookAhead().getAttribute());
						TypeConversion.null2intarray(identifier, arraySize);
					}
				}
				// ... boolean
				else if(newType.getTokenID().equals(TROOF.tokenId)) {
					TypeConversion.null2bool(identifier);
				}
				// ... boolean[]
				else if(newType.getTokenID().equals(TROOFZ.tokenId)) {
					if(!isNum(lookAhead())) {
						ErrorReporter.markError("Missing size of char array");
					} else {
						int arraySize = new Integer(lookAhead().getAttribute());
						TypeConversion.null2boolarray(identifier, arraySize);
					}
				}
				// ... a char
				else if(newType.getTokenID().equals(CHAR.tokenId)) {
					TypeConversion.null2char(identifier);
				} 
				// ... a string
				else if(newType.getTokenID().equals(CHARZ.tokenId)) {
					if(!isNum(lookAhead())) {
						ErrorReporter.markError("Missing size of char array");
					} else {
						int arraySize = new Integer(lookAhead().getAttribute());
						TypeConversion.null2string(identifier, arraySize);
					}
				} else {
					// newType = identifier
					TypeItem type = symboltable.getType(newType.getAttribute());
					TypeConversion.null2record(identifier, type.getSize());
				}
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	public boolean isVarDecl(Keyword keyword) throws ParseException {
		if(isIdentifier(keyword)) {
			
			if(lookAhead() instanceof IS) {
				adjustSrcPointer();
				
				if(symboltable == null) {
					symboltable = new Symboltable("main", null);
					System.out.println("THIS SHOULD NEVER HAPPEN");
				}
				
				if(!(lookAhead() instanceof NOW)) {
					ErrorReporter.markError("Missing 'NOW' keyword");
				} else {
					adjustSrcPointer();
				}
				
				if(!(lookAhead() instanceof A)) {
					ErrorReporter.markError("Missing 'A' keyword");
				} else {
					adjustSrcPointer();
				}
				
				Keyword typeKeyword = s.lookupToken();
				
				SymboltableEntry ste = symboltable.get(keyword.getAttribute());
				java.lang.String lastType;
				if(ste == null) {
					ErrorReporter.markError("Unkown variable " + keyword.getAttribute());
					ste = new SymboltableEntry();
					ste.setName(keyword.getAttribute());
					ste.setType(NOOB.tokenId);
					lastType = NOOB.tokenId;
				} else {
					lastType = ste.getType();
				}
				
//				if(typeKeyword instanceof CHARZ || typeKeyword instanceof NUMBRZ || typeKeyword instanceof TROOFZ) {
//					if(!isNum(lookAhead())) {
//						ErrorReporter.markError("Missing size of int array");
//					} else {
//						adjustSrcPointer();
//					}
//				}
				
				if(!checkTypeConversions(lastType, typeKeyword, (Identifier) keyword)) {
					ErrorReporter.markError(
							"Cannot convert " + lastType + 
							" to " + typeKeyword.getAttribute());
				} else {
					ste.setType(typeKeyword.getTokenID());
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
				keyword instanceof TROOFZ ||
				symboltable.existsType(keyword.getAttribute()));
	}

	public boolean isIdentifier(Keyword keyword) {
		return keyword instanceof Identifier;
	}
	
	public boolean isFunction(Keyword keyword) throws ParseException {
		if(keyword instanceof HOW) {
			
			if(!(lookAhead() instanceof DUZ)) {
				ErrorReporter.markError("Missing 'DUZ' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!(lookAhead() instanceof I)) {
				ErrorReporter.markError("Missing 'I' keyword");
			} else {
				adjustSrcPointer();
			}
			
			Keyword funcId = lookAhead();
			if(!(funcId instanceof Identifier)) {
				ErrorReporter.markError("Expected function label");
			} else {
				adjustSrcPointer();
			}
			
			// save callers symboltable
			Symboltable tmpSymboltable = symboltable;
			// if the caller function was the main function, we store the
			// content of the caller-symboltable in the $s-registers 
			// (as long as there is enough space)
			if(!tmpSymboltable.getName().equals("main")) {
				// save $s0 - $s7
				for(java.lang.String var : symboltable.getHashEntry().keySet()) {
					SymboltableEntry ste = symboltable.get(var);
					// TODO ...
				}
			}
			symboltable = new Symboltable(funcId.getAttribute(), symboltable.getGlobalTable());
			
			codeGenerator.put(funcId.getAttribute(), ": ");
			// fp := sp
			codeGenerator.setFPisSP();
			
			Keyword tok = lookAhead();
			ArrayList<java.lang.String> argTypes = new ArrayList<java.lang.String>();
			if(tok instanceof YR) {
				adjustSrcPointer();

				Keyword type = lookAhead();
				if(!isType(type)) {
					
					if(lookAhead() instanceof Identifier) {
						ErrorReporter.markError("Expected type declaration for argument " + 
								lookAhead().getAttribute());
						adjustSrcPointer();
					} else {
						ErrorReporter.markError("Expected another argument");
					}
					
				} else {
					adjustSrcPointer();
					argTypes.add(type.getTokenID());
					Keyword argument = lookAhead();
					if(!(lookAhead() instanceof Identifier)) {
						ErrorReporter.markError("Expected another argument");
					} else {
						adjustSrcPointer();
					}
					
					SymboltableEntry symboltableEntry = new SymboltableEntry();
					symboltableEntry.setReg("$a0");
					symboltableEntry.setCategory("reg");
					symboltableEntry.setName(argument.getAttribute());
					symboltableEntry.setType(type.getTokenID());
					symboltable.put(symboltableEntry);
				}
				
				int argCount = 1;
				while((tok = lookAhead()) instanceof AN) {
					adjustSrcPointer();
					if(!(lookAhead() instanceof YR)) {
						ErrorReporter.markError("Missing 'YR' keyword");
					} else {
						adjustSrcPointer();
					}
					Keyword nextType = lookAhead();
					if(!isType(nextType)) {
						if(lookAhead() instanceof Identifier) {
							ErrorReporter.markError("Expected type declaration for argument " + 
									lookAhead().getAttribute());
							adjustSrcPointer();
						} else {
							ErrorReporter.markError("Expected another argument");
						}
						
					} else {
						adjustSrcPointer();
						argTypes.add(nextType.getTokenID());
						Keyword argumentNext = lookAhead();
						if(!(argumentNext instanceof Identifier)) {
							ErrorReporter.markError("Expected another argument");
						} else {
							adjustSrcPointer();
						}
						
						if(argCount < 4) {
							SymboltableEntry symboltableEntry = new SymboltableEntry();
							symboltableEntry.setReg("$a" + new Integer(argCount).toString());
							symboltableEntry.setCategory("reg");
							symboltableEntry.setName(argumentNext.getAttribute());
							symboltableEntry.setType(nextType.getTokenID());
							symboltable.put(symboltableEntry);
							argCount++;
						} else {
							// TODO: arguments were possibly stored on the stack. Retrieve them!
						}
					}
				}
			}
			
			FunctionItem functionItem = new FunctionItem();
			functionItem.setFunctionName(funcId.getAttribute());
			functionItem.setArgumentTypes(argTypes);
			functionAccessManager.addFunctionDefinition(functionItem);
			
			adjustSrcPointer();
			while(!(tok instanceof IF)) {
				if(!(isStatement(tok) 
						|| (tok instanceof GTFO)
						|| (tok instanceof FOUND))) {
					ErrorReporter.markError("Expected statement");
				} else if(tok instanceof FOUND) {
					if(!(lookAhead() instanceof YR)) {
						ErrorReporter.markError("Missing 'YR' keyword");
					} else {
						adjustSrcPointer();
					}
					
					if(!isOperation(s.lookupToken())) {
						ErrorReporter.markError("Expected return value");
					}
					
					if(currentSymboltableEntry == null) {
						currentSymboltableEntry = symboltable.get("IT");
					}
					// stores return value in $v0/$v1
					codeGenerator.move2Return(currentSymboltableEntry.getReg());
					// set the fp back: sp := fp
					codeGenerator.setSPisFP();
					// returns to previous address
					codeGenerator.put("jr", "$ra");
				} else if(tok instanceof GTFO) {
					// returns to previous address
					codeGenerator.put("jr", "$ra");
				}
				tok = s.lookupToken();
			}
			
			if(!(lookAhead() instanceof YOU)) {
				ErrorReporter.markError("Missing 'YOU' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!(lookAhead() instanceof SAY)) {
				ErrorReporter.markError("Missing 'SAY' keyword");
			} else {
				adjustSrcPointer();
			}
			
			if(!(lookAhead() instanceof SO)) {
				ErrorReporter.markError("Missing 'SO' keyword");
			} else {
				adjustSrcPointer();
			}

			// set the fp back: sp := fp
			codeGenerator.setSPisFP();
			codeGenerator.put("jr", "$ra");
			symboltable = tmpSymboltable;
			
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
	
	public static void loadSourcecode(java.lang.String filename) {
		if(filename != null) {
			Parser p = new Parser(filename);
			try {
		
				File mainFile = new File(filename);
				java.util.Scanner filescanner = new java.util.Scanner(mainFile);
				StringBuilder stringBuilder = new StringBuilder();
				while(filescanner.hasNextLine()) {
					stringBuilder.append(filescanner.nextLine() + "\n");
				}
				p.s = new Scanner(stringBuilder.toString());
				Assert.assertTrue(p.isProgram(p.s.lookupToken()));
//				char[] buf = new char[200000];
//				int nc = 0;
//				if((nc = reader.read(buf)) != -1) {
//					stringBuilder.append(buf);
//					java.lang.String toParse = 
//						stringBuilder.toString().substring(0, nc);
//					p.s = new Scanner(toParse);
//					Assert.assertTrue(p.isProgram(p.s.lookupToken()));
//				}
			} catch (ParseException e) {
				e.printStackTrace();
				fail();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(java.lang.String[] args) {
		if(args[0] != null) {
			Parser.loadSourcecode(args[0]);
		}
	}
}
