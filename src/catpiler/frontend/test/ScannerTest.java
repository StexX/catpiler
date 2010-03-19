/*
 * ScannerTest.java Copyright (C) 2010 Stephanie Stroka 
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
package catpiler.frontend.test;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import catpiler.frontend.exception.SyntaxException;
import catpiler.frontend.scanner.Scanner;
import catpiler.frontend.scanner.Token;
import catpiler.frontend.scanner.TokenTable;

/**
 * JUnit 4 test for Scanner
 * TODO: Needs further test cases
 * 
 * @author Stephanie Stroka
 * 			(stephanie.stroka@sbg.ac.at)
 *
 */
public class ScannerTest {

	/**
	 * Tests whether the Scanner throws a SyntaxException if 
	 * the source code contains syntax failures.
	 */
	@Test
	public void testExpectingFailures() {
		Scanner s1 = new Scanner("BOTHThisIsAnIdentifier 1a1notherID SAEM HOW DUZ I OIC");
		try {
			s1.search4Tokens();
			fail("Expecting exception");
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
		
		Scanner s2 = new Scanner("BOTHThisIsAnIdentifier 1372g a1n_otherID SAEM HOW DUZ I OIC");
		try {
			s2.search4Tokens();
			fail("Expecting exception");
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests whether a bunch of code can be correctly identified as 
	 * tokens.
	 */
	@Test
	public void testSearchTokens() {
		Scanner s = new Scanner("BOTHThisIsAnIdentifier anotherID  \"someStr\" 1337 SAEM HOW DUZ I OIC");
		Token[] tokens = null;
		try {
			if((tokens = s.search4Tokens()) != null) {
				Assert.assertEquals(TokenTable.id , tokens[0]);
				Assert.assertEquals(TokenTable.id , tokens[1]);
				Assert.assertEquals(TokenTable.string , tokens[2]);
				Assert.assertEquals(TokenTable.integer , tokens[3]);
				Assert.assertEquals(TokenTable.op_eq, tokens[4]);
				Assert.assertEquals(TokenTable.function_1, tokens[5]);
				Assert.assertEquals(TokenTable.function_2, tokens[6]);
				Assert.assertEquals(TokenTable.var_decl_1, tokens[7]);
				Assert.assertEquals(TokenTable.fc_if_end, tokens[8]);
				Assert.assertNull(tokens[11]);
			} else {
				fail("Could find correct tokens :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests matching of every single token
	 */
	@Test
	public void testLookupToken() {
		
		testComment();		
		testFileBegin();		
		testFileEnd();		
		testVarDecl_1();
		testVarDecl_2();
		testVarDecl_3();
		testVarAssign();
		testString();
		testInt();
		testBool();
		testTrue();
		testFalse();
		testLF();
		testQuote();
		testOf();
		testSeq();
		testSum();
		testDiff();
		testProd();
		testQuot();
		testMax();
		testMin();
		testAnd();
		testOr();
		testNot();
		testAll();
		testAny();
		testOpEnd();
		testEq_1();
		testEq_2();
		testNeq();
		testIf();
		testThen_1();
		testThen_2();
		testElseIf();
		testElse_1();
		testElse_2();
		testIfEnd();
		testFunc_1();
		testFunc_2();
		testFuncEnd_1();
		testFuncEnd_2();
		testFuncEnd_3();
		testFuncEnd_4();
		testLoop_1();
		testLoop_2();
		testLoop_3();
		testLoopEnd();
	}
	
	private void testSeq() {
		Scanner s = new Scanner("AN");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_seq, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testOf() {
		Scanner s = new Scanner("OF");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_of, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testFuncEnd_1() {
		Scanner s = new Scanner("IF");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.function_end_1, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testFuncEnd_2() {
		Scanner s = new Scanner("YOU");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.function_end_2, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testFuncEnd_3() {
		Scanner s = new Scanner("SAY");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.function_end_3, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testFuncEnd_4() {
		Scanner s = new Scanner("SO");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.function_end_4, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testFunc_1() {
		Scanner s = new Scanner("HOW");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.function_1, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testFunc_2() {
		Scanner s = new Scanner("DUZ");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.function_2, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testIfEnd() {
		Scanner s = new Scanner("OIC");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.fc_if_end, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testElse_1() {
		Scanner s = new Scanner("NO WAI");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.fc_else_1, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testElse_2() {
		Scanner s = new Scanner("WAI");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.fc_else_2, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testElseIf() {
		Scanner s = new Scanner("MEBBE");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.fc_else_if, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testThen_1() {
		Scanner s = new Scanner("YA");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.fc_then_1, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testThen_2() {
		Scanner s = new Scanner("RLY");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.fc_then_2, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testIf() {
		Scanner s = new Scanner("ORLY?");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.fc_if, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testNeq() {
		Scanner s = new Scanner("DIFFRINT");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_neq, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testEq_1() {
		Scanner s = new Scanner("BOTH");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_both, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testEq_2() {
		Scanner s = new Scanner("SAEM");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_eq, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testOpEnd() {
		Scanner s = new Scanner("MKAY");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_end, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testAny() {
		Scanner s = new Scanner("ANY");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_any, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testAll() {
		Scanner s = new Scanner("ALL");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_all, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testNot() {
		Scanner s = new Scanner("NOT");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_not, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testOr() {
		Scanner s = new Scanner("EITHER");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_or, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testAnd() {
		Scanner s = new Scanner("BOTH");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_both, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testMin() {
		Scanner s = new Scanner("SMALLR");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_min, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testMax() {
		Scanner s = new Scanner("BIGGR");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_max, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testQuot() {
		Scanner s = new Scanner("QUOSHUNT");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_quot, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testProd() {
		Scanner s = new Scanner("PRODUKT");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_prod, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testDiff() {
		Scanner s = new Scanner("DIFF");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_diff, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testSum() {
		Scanner s = new Scanner("SUM");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.op_sum, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testQuote() {
		Scanner s = new Scanner(":\"");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.char_quote, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testLF() {
		Scanner s = new Scanner(":)");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.char_lf, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testFalse() {
		Scanner s = new Scanner("FAIL");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.val_false, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testTrue() {
		Scanner s = new Scanner("WIN");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.val_true, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testBool() {
		Scanner s = new Scanner("TROOF");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.type_bool, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testInt() {
		Scanner s = new Scanner("NUMBR");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.type_int, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testString() {
		Scanner s = new Scanner("YARN");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.type_string, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testVarAssign() {
		Scanner s = new Scanner("R");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.var_assign, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testVarDecl_1() {
		Scanner s = new Scanner("I");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.var_decl_1, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testVarDecl_2() {
		Scanner s = new Scanner("HAS");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.var_decl_2, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testVarDecl_3() {
		Scanner s = new Scanner("A");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.var_decl_3, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testFileEnd() {
		Scanner s = new Scanner("KTHXBYE");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.file_end, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testFileBegin() {
		Scanner s = new Scanner("HAI");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.file_begin, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testComment() {
		Scanner s = new Scanner("BTW");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.comment, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testLoop_1() {
		Scanner s = new Scanner("IM");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.fc_loop_1, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testLoop_2() {
		Scanner s = new Scanner("IN");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.fc_loop_2, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testLoop_3() {
		Scanner s = new Scanner("YR");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.fc_loop_3, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testLoopEnd() {
		Scanner s = new Scanner("OUTTA");
		Token t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertEquals(TokenTable.fc_loop_end, t);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

}
