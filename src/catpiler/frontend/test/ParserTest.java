package catpiler.frontend.test;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import catpiler.frontend.exception.ParseException;
import catpiler.frontend.parser.Parser;
import catpiler.frontend.scanner.Scanner;
import catpiler.utils.ErrorReporter;

public class ParserTest {

	@Test
	public void testStruct() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("" +
				"STUFF label \n" +
				"    I HAS A var1 \n" +
				"    I HAS A var2 \n" +
				"    var1 IS NOW A NUMBR \n" +
				"    var2 IS NOW A NUMBR \n" +
				"THATSIT");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isStruct(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testModule() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("" +
				"CAN HAS fileId ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isModuleImport(p.s.lookupToken()));
			
			// expecting file not found error
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testVarInit() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("" +
				"I HAS A var ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isVarInit(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testProgram() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("" +
				"CAN HAS fileId \n" +
				"STUFF label \n" +
				"    I HAS A var1 \n" +
				"    I HAS A var2 \n" +
				"    var1 IS NOW A NUMBR \n" +
				"    var2 IS NOW A NUMBR \n" +
				"THATSIT \n" +
				"HAI \n" +
				"    I HAS A var1 \n" +
				"    I HAS A var2 \n" +
				"    var1 IS NOW A NUMBR \n" +
				"    var2 IS NOW A NUMBR \n" +
				"    var1 R 3 \n" +
				"    var2 R 2 \n" +
				"    SUM OF var1 AN var2 \n" +
				"    PRODUKT OF var2 AN var1 \n" +
				"KTHXBYE \n" +
				"HOW DUZ I label \n" +
				"I HAS A var3 \n" +
				"var3 R \"some string\" \n" +
				"IF YOU SAY SO \n");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
			
			// expecting file not found error
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testProgram2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- expecting error notice 'testProgram2' ----");
		Scanner s = new Scanner("" +
				"CAN HAS fileId \n" +
				"STUFF label \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 IS NOW A NUMBR \n" +
				"var2 IS NOW A NUMBR \n" +
				"var1 R 3 \n" +
				"var2 R 2 \n" +
				"THATSIT \n" +
				"HAI \n" +
				"    I HAS var1 \n" +
				"    I HAS A var2 \n" +
				"    var1 IS NOW A NUMBR \n" +
				"    var2 IS NOW A NUMBR \n" +
				"    var1 R 3 \n" +
				"    var2 R 2 \n" +
				"    SUM OF var1 var2 \n" +
				"    PRODUKT OF var2 AN var1 \n" +
				"KTHXBYE \n" +
				"HOW I label \n" +
				"I HAS A var3 \n" +
				"var3 R \"some string\" \n" +
				"IF YOU SAY SO \n");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
			System.out.println("-----------------------------------------");
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testMain() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("" +
				"HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"I HAS A var3 \n" +
				"I HAS A var4 \n" +
				"var1 IS NOW A NUMBR \n" +
				"var2 IS NOW A NUMBR \n" +
				"var3 IS NOW A TROOF \n" +
				"var4 IS NOW A TROOF \n" +
				"var1 R 3 \n" +
				"var2 R 2 \n" +
				"var3 R WIN \n" +
				"var4 R FAIL \n" +
				"SUM OF var1 AN var2 \n" +
				"EITHER OF var3 AN var4 \n" +
				"ORLY? \n" +
				"YA RLY \n" +
				"    PRODUKT OF var2 AN var1 \n" +
				"OIC \n" +
				"KTHXBYE");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testStatement() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"I HAS A var3 \n" +
				"I HAS A var4 \n" +
				"EITHER OF var3 AN var4 \n" +
				"ORLY? \n" +
				"YA RLY \n" +
				"    PRODUKT OF var2 AN var1 \n" +
				"OIC ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isStatement(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFuncCall1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("CAN U function1 ?");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isFuncCall(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFuncCall2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("CAN U function1 var1 2 \"str\" ?");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isFuncCall(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFuncCall3() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("CAN U function1 var1 SUM OF 2 AN 3 ?");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isFuncCall(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testExpr1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("ANY OF WIN AN FAIL AN FAIL MKAY");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isExpr(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testExpr2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("BOTH SAEM 2 AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isExpr(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testExpr3() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("BOTH OF var1 AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isExpr(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("BOTH OF var1 AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("EITHER OF WIN AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp3() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("BOTH OF FAIL AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp4() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("WIN");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp5() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("FAIL");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolArray1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("FAIL FAIL WIN FAIL WIN WIN");
		p.s = s;
		ErrorReporter.setS(s);
		Assert.assertTrue(p.isBoolArray(p.s.lookupToken()));
		Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testNumArray1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("1 2 3 4 5 6");
		p.s = s;
		ErrorReporter.setS(s);
		Assert.assertTrue(p.isNumArray(p.s.lookupToken()));
		Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testGenExpr1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("BOTH SAEM var1 AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isGenExpr(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGenExpr2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("DIFFRINT var1 AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isGenExpr(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGenExpr3() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		// means: var1 <= 30
		Scanner s = new Scanner("BOTH SAEM 30 AN BIGGR OF 30 AN var1");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isGenExpr(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotGenExpr1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("BOTH OF var1 AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertFalse(p.isGenExpr(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotGenExpr2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- expecting error notice 'testNotGenExpr2' ----");
		Scanner s = new Scanner("BOTH var1 AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			p.isGenExpr(p.s.lookupToken());
			Assert.assertTrue(ErrorReporter.isError());
//			fail();
			System.out.println("-----------------------------------------");
		} catch (ParseException e) {
			//expected
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBool1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("WIN");
		p.s = s;
		ErrorReporter.setS(s);
		Assert.assertTrue(p.isBool(p.s.lookupToken()));
		Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testBool2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("FAIL");
		p.s = s;
		ErrorReporter.setS(s);
		Assert.assertTrue(p.isBool(p.s.lookupToken()));
		Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testNotBool() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("\"test\"");
		p.s = s;
		ErrorReporter.setS(s);
		Assert.assertFalse(p.isBool(p.s.lookupToken()));
	}
	
	@Test
	public void testBool4() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("1");
		p.s = s;
		ErrorReporter.setS(s);
		Assert.assertFalse(p.isBool(p.s.lookupToken()));
	}
	
	@Test
	public void testBool5() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("bla1");
		p.s = s;
		ErrorReporter.setS(s);
		Assert.assertFalse(p.isBool(p.s.lookupToken()));
	}
	
	@Test
	public void testInfExpr1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testInfExpr1' ----");
		Scanner s = new Scanner("ALL OF var1 AN var2 AN var3 MKAY");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isInfExpr(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testInfExpr2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testInfExpr2' ----");
		Scanner s = new Scanner("ANY OF var1 AN var2 AN var3 MKAY");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isInfExpr(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotInfExpr2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- expecting error notice 'testNotInfExpr2' ----");
		Scanner s = new Scanner("ANY OF var1 AN var2 AN var3");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			p.isInfExpr(p.s.lookupToken());
			Assert.assertTrue(ErrorReporter.isError());
			System.out.println("-----------------------------------------");
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotInfExpr3() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- expecting error notice 'testNotInfExpr2' ----");
		Scanner s = new Scanner("ANY OF var1 AN var2 AN var3 \n" +
				"AN var4 MMKAY \n");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			p.isInfExpr(p.s.lookupToken());
			Assert.assertTrue(ErrorReporter.isError());
			System.out.println("-----------------------------------------");
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBiExpr1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBiExpr1' ----");
		Scanner s = new Scanner("BOTH OF var1 AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isBiExpr(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBiExpr2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBiExpr2' ----");
		Scanner s = new Scanner("EITHER OF var1 AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isBiExpr(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBiExpr3() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBiExpr3' ----");
		Scanner s = new Scanner("BOTH SAEM var1 AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isBiExpr(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBiExpr4() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBiExpr4' ----");
		Scanner s = new Scanner("DIFFRINT var1 AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isBiExpr(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotBiExpr() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNotBiExpr' ----");
		Scanner s = new Scanner("SUM OF var1 AN var2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertFalse(p.isBiExpr(p.s.lookupToken()));
//			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFlowControl1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFlowControl1' ----");
//		Scanner s = new Scanner("BOTH OF WIN AN WIN " +
		Scanner s = new Scanner("" +
				"ORLY? " +
				"YA RLY " +
				"    PRODUKT OF var2 AN var1 " +
				"OIC ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isFlowControl(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFlowControl2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFlowControl2' ----");
//		Scanner s = new Scanner("BOTH OF WIN AN WIN \n" +
		Scanner s = new Scanner("" +
				"ORLY? \n" +
				"YA RLY \n" +
				"    IM IN YR loop TIL BOTH SAEM var2 AN 100 \n" +
				"        PRODUKT OF var2 AN var1 \n" +
				"    IM OUTTA YR loop \n" +
				"OIC ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isFlowControl(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testLoop1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testLoop1' ----");
		Scanner s = new Scanner("IM IN YR loop TIL BOTH SAEM var2 AN 100 \n" +
				"    PRODUKT OF var2 AN var1 \n" +
				"    BOTH SAEM var2 AN 30 \n" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var2 AN var3 \n" +
				"        OIC \n" +
				"IM OUTTA YR loop \n");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isLoop(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testLoop2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testLoop2' ----");
		Scanner s = new Scanner("IM IN YR loop YR arg1 WILE BOTH SAEM var2 AN 100 \n" +
				"    PRODUKT OF var2 AN var1 \n" +
				"    BOTH SAEM var2 AN 30 \n" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var2 AN var3 \n" +
				"        OIC \n" +
				"IM OUTTA YR loop ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isLoop(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIf1' ----");
//		Scanner s = new Scanner("BOTH SAEM var2 AN 30 \n" +
		Scanner s = new Scanner("" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var2 AN var3 \n" +
				"        MEBBE BOTH SAEM var2 AN 40 \n" +
				"            SUM OF var2 AN var4 \n" +
				"        MEBBE ALL OF WIN AN WIN AN WIN MKAY \n" +
				"            QUOSHUNT OF var3 AN var4 \n" +
				"        NO WAI \n" +
				"            PRODUKT OF var5 AN var6 \n" +
				"    OIC ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isIf(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIf2' ----");
//		Scanner s = new Scanner("BOTH SAEM var2 AN 30 \n" +
		Scanner s = new Scanner("" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var2 AN var3 \n" +
				"        NO WAI \n" +
				"            PRODUKT OF var5 AN var6 \n" +
				"    OIC ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isIf(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf3() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIf3' ----");
//		Scanner s = new Scanner("BOTH SAEM var2 AN 30 \n" +
		Scanner s = new Scanner("" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var2 AN var3 \n" +
				"        MEBBE BOTH SAEM var2 AN 40 \n" +
				"            SUM OF var2 AN var4 \n" +
				"        MEBBE ALL OF WIN AN WIN AN WIN MKAY \n" +
				"            QUOSHUNT OF var3 AN var4 \n" +
				"    OIC ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isIf(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf4() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIf4' ----");
//		Scanner s = new Scanner("BOTH SAEM var2 AN 30 \n" +
		Scanner s = new Scanner("" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var2 AN var3 \n" +
				"        MEBBE BOTH OF var2 AN WIN \n" +
				"            SUM OF var2 AN var4 \n" +
				"        MEBBE ALL OF WIN AN WIN AN WIN MKAY \n" +
				"            QUOSHUNT OF var3 AN var4 \n" +
				"    OIC ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isIf(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotIf() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- expecting error notice 'testNotIf' ----");
//		Scanner s = new Scanner("BOTH SAEM var2 AN 30 \n" +
		Scanner s = new Scanner("" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var2 AN var3 \n" +
				"        MEBBE SUM OF var2 AN var4 \n" +
				"    OIC ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			p.isIf(p.s.lookupToken());
			Assert.assertTrue(ErrorReporter.isError());
			System.out.println("-----------------------------------------");
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testOperation1' ----");
		Scanner s = new Scanner("BOTH OF var2 AN WIN ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testOperation2' ----");
		Scanner s = new Scanner("SUM OF var2 AN 30 ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation3() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testOperation3' ----");
		Scanner s = new Scanner("DIFF OF var2 AN 30 ");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation4() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testOperation4' ----");
		Scanner s = new Scanner("QUOSHUNT OF SUM OF DIFF OF var1 AN var2 AN 30 AN 40");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation5() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testOperation5' ----");
		Scanner s = new Scanner("QUOSHUNT OF 40 AN SUM OF DIFF OF var1 AN var2 AN 30");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testStringOp() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testStringOp' ----");
		Scanner s = new Scanner("\"test\"");
		p.s = s;
		ErrorReporter.setS(s);
		Assert.assertTrue(p.isStringOp(p.s.lookupToken()));
		Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testNumOp1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp1' ----");
		Scanner s = new Scanner("DIFF OF 30 AN DIFF OF 40 AN 50");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp2' ----");
		Scanner s = new Scanner("30");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp3() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp3' ----");
		Scanner s = new Scanner("BIGGR OF 30 AN 40");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp4() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp4' ----");
		Scanner s = new Scanner("SMALLR OF 30 AN 40");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNum() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNum' ----");
		Scanner s = new Scanner("30");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testVarAssign1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testVarAssign1' ----");
		Scanner s = new Scanner("var1 R SUM OF 1 AN 2");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isVarAssign(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testVarAssign2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testVarAssign2' ----");
		Scanner s = new Scanner("var1 R 1 2 3 4 5");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isVarAssign(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testType() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("NUMBR");
		p.s = s;
		ErrorReporter.setS(s);
		System.out.println("---- 'testType' ----");
		Assert.assertTrue(p.isType(p.s.lookupToken()));
		Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testVarDecl() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testVarDecl' ----");
		Scanner s = new Scanner("var1 IS NOW A NUMBR");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isVarDecl(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIdentifier1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIdentifier1' ----");
		Scanner s = new Scanner("var1");
		p.s = s;
		ErrorReporter.setS(s);
		Assert.assertTrue(p.isIdentifier(p.s.lookupToken()));
		Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testIdentifier2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIdentifier2' ----");
		Scanner s = new Scanner("var_1");
		p.s = s;
		ErrorReporter.setS(s);
		Assert.assertTrue(p.isIdentifier(p.s.lookupToken()));
		Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testNotIdentifier() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- expecting error notice 'testNotIdentifier' ----");
		Scanner s = new Scanner("1var");
		p.s = s;
		ErrorReporter.setS(s);
		Assert.assertFalse(p.isIdentifier(p.s.lookupToken()));
//		Assert.assertTrue(ErrorReporter.isError());
		System.out.println("-----------------------------------------");
	}
	
	@Test
	public void testFunction1() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFunction1' ----");
		Scanner s = new Scanner("" +
				"HOW DUZ I func_name \n" +
				"    BOTH SAEM var1 AN var2 \n" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var1 AN var2 \n" +
				"    OIC \n" +
				"IF YOU SAY SO");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isFunction(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFunction2() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFunction2' ----");
		Scanner s = new Scanner("" +
				"HOW DUZ I func_name YR NUMBR arg1 AN YR CHARZ arg2 AN YR TROOF arg3 \n" +
				"    BOTH SAEM var1 AN var2 \n" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var1 AN var2 \n" +
				"    OIC \n" +
				"IF YOU SAY SO");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isFunction(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFunction3() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFunction3' ----");
		Scanner s = new Scanner("" +
				"HOW DUZ I func_name YR arg1 AN YR arg2 AN YR arg3 \n" +
				"    BOTH SAEM var1 AN var2 \n" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var1 AN var2 \n" +
				"    OIC \n" +
				"FOUND YR var1 \n" +
				"IF YOU SAY SO");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isFunction(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFunction4() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFunction4' ----");
		Scanner s = new Scanner("" +
				"HOW DUZ I func_name YR arg1 AN YR arg2 AN YR arg3 \n" +
				"    SUM OF var1 AN 20 \n" +
				"    GTFO \n" +
				"    BOTH SAEM var1 AN var2 \n" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var1 AN var2 \n" +
				"    OIC \n" +
				"IF YOU SAY SO");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isFunction(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumArrayDeref() {
		ErrorReporter.setError(false);
		Parser p = new Parser();
		p.parseTest = true;
		Scanner s = new Scanner("" +
				"HAI \n" +
				"    I HAS A var1 \n" +
				"    I HAS A var2 \n" +
				"    var1 IS NOW A NUMBRZ 5\n" +
				"    var2 IS NOW A NUMBR \n" +
				"    var2 R 2 \n" +
				"    var1.var2 R 5 \n" +
				"KTHXBYE \n");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
			System.out.println("-----------------------------------------");
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
}
