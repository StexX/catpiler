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
import catpiler.frontend.scanner.keywords.A;
import catpiler.frontend.scanner.keywords.ALL;
import catpiler.frontend.scanner.keywords.AN;
import catpiler.frontend.scanner.keywords.ANY;
import catpiler.frontend.scanner.keywords.BIGGR;
import catpiler.frontend.scanner.keywords.BOTH;
import catpiler.frontend.scanner.keywords.CHARZ;
import catpiler.frontend.scanner.keywords.DIFF;
import catpiler.frontend.scanner.keywords.DIFFRINT;
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
import catpiler.frontend.scanner.keywords.Identifier;
import catpiler.frontend.scanner.keywords.Int;
import catpiler.frontend.scanner.keywords.KTHXBYE;
import catpiler.frontend.scanner.keywords.Keyword;
import catpiler.frontend.scanner.keywords.LF;
import catpiler.frontend.scanner.keywords.MEBBE;
import catpiler.frontend.scanner.keywords.MKAY;
import catpiler.frontend.scanner.keywords.NO;
import catpiler.frontend.scanner.keywords.NOT;
import catpiler.frontend.scanner.keywords.NUMBR;
import catpiler.frontend.scanner.keywords.OF;
import catpiler.frontend.scanner.keywords.OIC;
import catpiler.frontend.scanner.keywords.OUTTA;
import catpiler.frontend.scanner.keywords.PRODUKT;
import catpiler.frontend.scanner.keywords.QUOSHUNT;
import catpiler.frontend.scanner.keywords.R;
import catpiler.frontend.scanner.keywords.RLY;
import catpiler.frontend.scanner.keywords.SAEM;
import catpiler.frontend.scanner.keywords.SAY;
import catpiler.frontend.scanner.keywords.SMALLR;
import catpiler.frontend.scanner.keywords.SO;
import catpiler.frontend.scanner.keywords.SUM;
import catpiler.frontend.scanner.keywords.TLDR;
import catpiler.frontend.scanner.keywords.TROOF;
import catpiler.frontend.scanner.keywords.WAI;
import catpiler.frontend.scanner.keywords.WIN;
import catpiler.frontend.scanner.keywords.YA;
import catpiler.frontend.scanner.keywords.YOU;
import catpiler.frontend.scanner.keywords.YR;

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
		Keyword[] tokens = null;
		try {
			if((tokens = s.search4Tokens()) != null) {
				Assert.assertTrue(tokens[0] instanceof Identifier);
				Assert.assertEquals(new String("BOTHThisIsAnIdentifier"),tokens[0].getAttribute().trim());
				Assert.assertTrue(tokens[1] instanceof Identifier);
				Assert.assertEquals(new String("anotherID"),tokens[1].getAttribute().trim());
				Assert.assertTrue(tokens[2] instanceof catpiler.frontend.scanner.keywords.String);
				Assert.assertEquals(new String("\"someStr\""),tokens[2].getAttribute().trim());
				Assert.assertTrue(tokens[3] instanceof Int);
				Assert.assertEquals(new String("1337"),tokens[3].getAttribute().trim());
				Assert.assertTrue(tokens[4] instanceof SAEM);
				Assert.assertTrue(tokens[5] instanceof HOW);
				Assert.assertTrue(tokens[6] instanceof DUZ);
				Assert.assertTrue(tokens[7] instanceof I);
				Assert.assertTrue(tokens[8] instanceof OIC);
				Assert.assertNull(tokens[9]);
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
		
		testComment_1();
		testComment_2();
		testComment_3();
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
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof AN);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testOf() {
		Scanner s = new Scanner("OF");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof OF);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testFuncEnd_1() {
		Scanner s = new Scanner("IF");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof IF);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testFuncEnd_2() {
		Scanner s = new Scanner("YOU");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof YOU);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testFuncEnd_3() {
		Scanner s = new Scanner("SAY");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof SAY);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testFuncEnd_4() {
		Scanner s = new Scanner("SO");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof SO);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testFunc_1() {
		Scanner s = new Scanner("HOW");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof HOW);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testFunc_2() {
		Scanner s = new Scanner("DUZ");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof DUZ);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testIfEnd() {
		Scanner s = new Scanner("OIC");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof OIC);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testElse_1() {
		Scanner s = new Scanner("NO");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof NO);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testElse_2() {
		Scanner s = new Scanner("WAI");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof WAI);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testElseIf() {
		Scanner s = new Scanner("MEBBE");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof MEBBE);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testThen_1() {
		Scanner s = new Scanner("YA");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof YA);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testThen_2() {
		Scanner s = new Scanner("RLY");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof RLY);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testNeq() {
		Scanner s = new Scanner("DIFFRINT");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof DIFFRINT);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testEq_1() {
		Scanner s = new Scanner("BOTH");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof BOTH);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testEq_2() {
		Scanner s = new Scanner("SAEM");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof SAEM);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testOpEnd() {
		Scanner s = new Scanner("MKAY");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof MKAY);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testAny() {
		Scanner s = new Scanner("ANY");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof ANY);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testAll() {
		Scanner s = new Scanner("ALL");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof ALL);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testNot() {
		Scanner s = new Scanner("NOT");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof NOT);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testOr() {
		Scanner s = new Scanner("EITHER");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof EITHER);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testAnd() {
		Scanner s = new Scanner("BOTH");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof BOTH);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testMin() {
		Scanner s = new Scanner("SMALLR");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof SMALLR);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testMax() {
		Scanner s = new Scanner("BIGGR");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof BIGGR);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testQuot() {
		Scanner s = new Scanner("QUOSHUNT");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof QUOSHUNT);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testProd() {
		Scanner s = new Scanner("PRODUKT");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof PRODUKT);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testDiff() {
		Scanner s = new Scanner("DIFF");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof DIFF);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testSum() {
		Scanner s = new Scanner("SUM");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof SUM);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testLF() {
		Scanner s = new Scanner(":)");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof LF);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testFalse() {
		Scanner s = new Scanner("FAIL");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof FAIL);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testTrue() {
		Scanner s = new Scanner("WIN");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof WIN);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testBool() {
		Scanner s = new Scanner("TROOF");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof TROOF);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testInt() {
		Scanner s = new Scanner("NUMBR");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof NUMBR);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testString() {
		Scanner s = new Scanner("CHARZ");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof CHARZ);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testVarAssign() {
		Scanner s = new Scanner("R");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof R);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testVarDecl_1() {
		Scanner s = new Scanner("I");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof I);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testVarDecl_2() {
		Scanner s = new Scanner("HAS");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof HAS);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testVarDecl_3() {
		Scanner s = new Scanner("A");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof A);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testFileEnd() {
		Scanner s = new Scanner("KTHXBYE");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof KTHXBYE);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testFileBegin() {
		Scanner s = new Scanner("HAI");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof HAI);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testComment_1() {
		Scanner s = new Scanner("BTW");
		Keyword t = null;
		try {
			t = s.lookupToken();
			// Comments will be erased. 
			// We therefore do not expect any token
			Assert.assertNull(t);
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testComment_2() {
		Scanner s = new Scanner("OBTW");
		Keyword t = null;
		try {
			t = s.lookupToken();
			// Comments will be erased. 
			// We therefore do not expect any token
			Assert.assertNull(t);
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testComment_3() {
		Scanner s = new Scanner("TLDR");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof TLDR);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

	private void testLoop_1() {
		Scanner s = new Scanner("IM");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof IM);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testLoop_2() {
		Scanner s = new Scanner("IN");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof IN);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testLoop_3() {
		Scanner s = new Scanner("YR");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof YR);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void testLoopEnd() {
		Scanner s = new Scanner("OUTTA");
		Keyword t = null;
		try {
			if((t = s.lookupToken()) != null) {
				Assert.assertTrue(t instanceof OUTTA);
			} else {
				fail("No token found :(");
			}
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}

}
