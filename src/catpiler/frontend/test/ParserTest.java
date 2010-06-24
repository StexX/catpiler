package catpiler.frontend.test;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import catpiler.frontend.exception.ParseException;
import catpiler.frontend.parser.Parser;
import catpiler.frontend.scanner.Scanner;

public class ParserTest {

	@Test
	public void testStruct() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("" +
				"STUFF label \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 IS NOW A NUMBR \n" +
				"var2 IS NOW A NUMBR \n" +
				"var1 R 3 \n" +
				"var2 R 2 \n" +
				"THATSIT");

		try {
			Assert.assertTrue(p.isStruct(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testModule() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("" +
				"CAN HAS fileId ");
		try {
			Assert.assertTrue(p.isModuleImport(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testVarInit() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("" +
				"I HAS A var ");
		try {
			Assert.assertTrue(p.isVarInit(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testProgram() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("" +
				"CAN HAS fileId \n" +
				"STUFF label \n" +
				"    I HAS A var1 \n" +
				"    I HAS A var2 \n" +
				"    var1 IS NOW A NUMBR \n" +
				"    var2 IS NOW A NUMBR \n" +
				"    var1 R 3 \n" +
				"    var2 R 2 \n" +
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
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testProgram2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- expecting error notice 'testProgram2' ----");
		p.s = new Scanner("" +
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
				"    var1 3 \n" +
				"    var2 R 2 \n" +
				"    SUM OF var1 var2 \n" +
				"    PRODUKT OF var2 AN var1 \n" +
				"KTHXBYE \n" +
				"HOW I label \n" +
				"I HAS A var3 \n" +
				"var3 R \"some string\" \n" +
				"IF YOU SAY SO \n");
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
			System.out.println("-----------------------------------------");
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testMain() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("" +
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
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testStatement() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"I HAS A var3 \n" +
				"I HAS A var4 \n" +
				"EITHER OF var3 AN var4 \n" +
				"ORLY? \n" +
				"YA RLY \n" +
				"    PRODUKT OF var2 AN var1 \n" +
				"OIC ");
		try {
			Assert.assertTrue(p.isStatement(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFuncCall1() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("function1");
		try {
			Assert.assertTrue(p.isFuncCall(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFuncCall2() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("function1 var1 2 \"str\"");
		try {
			Assert.assertTrue(p.isFuncCall(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFuncCall3() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("function1 var1 SUM OF 2 AN 3");
		try {
			Assert.assertTrue(p.isFuncCall(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testExpr1() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("ANY OF WIN AN FAIL AN FAIL MKAY");
		try {
			Assert.assertTrue(p.isExpr(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testExpr2() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("BOTH SAEM 2 AN var2");
		try {
			Assert.assertTrue(p.isExpr(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testExpr3() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("BOTH OF var1 AN var2");
		try {
			Assert.assertTrue(p.isExpr(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp1() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("BOTH OF var1 AN var2");
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp2() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("EITHER OF WIN AN var2");
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp3() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("BOTH OF FAIL AN var2");
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp4() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("WIN");
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp5() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("FAIL");
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGenExpr1() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("BOTH SAEM var1 AN var2");
		try {
			Assert.assertTrue(p.isGenExpr(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGenExpr2() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("DIFFRINT var1 AN var2");
		try {
			Assert.assertTrue(p.isGenExpr(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGenExpr3() {
		Parser p = new Parser();
		p.parseTest = true;
		// means: var1 <= 30
		p.s = new Scanner("BOTH SAEM 30 AN BIGGR OF 30 AN var1");
		try {
			Assert.assertTrue(p.isGenExpr(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotGenExpr1() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("BOTH OF var1 AN var2");
		try {
			Assert.assertFalse(p.isGenExpr(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotGenExpr2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- expecting error notice 'testNotGenExpr2' ----");
		p.s = new Scanner("BOTH var1 AN var2");
		try {
			p.isGenExpr(p.s.lookupToken());
			Assert.assertTrue(p.isError());
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
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("WIN");
		Assert.assertTrue(p.isBool(p.s.lookupToken()));
		Assert.assertTrue(!p.isError());
	}
	
	@Test
	public void testBool2() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("FAIL");
		Assert.assertTrue(p.isBool(p.s.lookupToken()));
		Assert.assertTrue(!p.isError());
	}
	
	@Test
	public void testNotBool() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("\"test\"");
		Assert.assertFalse(p.isBool(p.s.lookupToken()));
	}
	
	@Test
	public void testBool4() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("1");
		Assert.assertFalse(p.isBool(p.s.lookupToken()));
	}
	
	@Test
	public void testBool5() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("bla1");
		Assert.assertFalse(p.isBool(p.s.lookupToken()));
	}
	
	@Test
	public void testInfExpr1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testInfExpr1' ----");
		p.s = new Scanner("ALL OF var1 AN var2 AN var3 MKAY");
		try {
			Assert.assertTrue(p.isInfExpr(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testInfExpr2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testInfExpr2' ----");
		p.s = new Scanner("ANY OF var1 AN var2 AN var3 MKAY");
		try {
			Assert.assertTrue(p.isInfExpr(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotInfExpr2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- expecting error notice 'testNotInfExpr2' ----");
		p.s = new Scanner("ANY OF var1 AN var2 AN var3");
		try {
			p.isInfExpr(p.s.lookupToken());
			Assert.assertTrue(p.isError());
			System.out.println("-----------------------------------------");
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotInfExpr3() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- expecting error notice 'testNotInfExpr2' ----");
		p.s = new Scanner("ANY OF var1 AN var2 AN var3 \n" +
				"AN var4 MMKAY \n");
		try {
			p.isInfExpr(p.s.lookupToken());
			Assert.assertTrue(p.isError());
			System.out.println("-----------------------------------------");
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBiExpr1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBiExpr1' ----");
		p.s = new Scanner("BOTH OF var1 AN var2");
		try {
			Assert.assertTrue(p.isBiExpr(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBiExpr2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBiExpr2' ----");
		p.s = new Scanner("EITHER OF var1 AN var2");
		try {
			Assert.assertTrue(p.isBiExpr(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBiExpr3() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBiExpr3' ----");
		p.s = new Scanner("BOTH SAEM var1 AN var2");
		try {
			Assert.assertTrue(p.isBiExpr(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBiExpr4() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBiExpr4' ----");
		p.s = new Scanner("DIFFRINT var1 AN var2");
		try {
			Assert.assertTrue(p.isBiExpr(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotBiExpr() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNotBiExpr' ----");
		p.s = new Scanner("SUM OF var1 AN var2");
		try {
			Assert.assertFalse(p.isBiExpr(p.s.lookupToken()));
//			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFlowControl1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFlowControl1' ----");
//		p.s = new Scanner("BOTH OF WIN AN WIN " +
		p.s = new Scanner("" +
				"ORLY? " +
				"YA RLY " +
				"    PRODUKT OF var2 AN var1 " +
				"OIC ");
		try {
			Assert.assertTrue(p.isFlowControl(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFlowControl2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFlowControl2' ----");
//		p.s = new Scanner("BOTH OF WIN AN WIN \n" +
		p.s = new Scanner("" +
				"ORLY? \n" +
				"YA RLY \n" +
				"    IM IN YR loop TIL BOTH SAEM var2 AN 100 \n" +
				"        PRODUKT OF var2 AN var1 \n" +
				"    IM OUTTA YR loop \n" +
				"OIC ");
		try {
			Assert.assertTrue(p.isFlowControl(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testLoop1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testLoop1' ----");
		p.s = new Scanner("IM IN YR loop TIL BOTH SAEM var2 AN 100 \n" +
				"    PRODUKT OF var2 AN var1 \n" +
				"    BOTH SAEM var2 AN 30 \n" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var2 AN var3 \n" +
				"        OIC \n" +
				"IM OUTTA YR loop \n");
		try {
			Assert.assertTrue(p.isLoop(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testLoop2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testLoop2' ----");
		p.s = new Scanner("IM IN YR loop YR arg1 WILE BOTH SAEM var2 AN 100 \n" +
				"    PRODUKT OF var2 AN var1 \n" +
				"    BOTH SAEM var2 AN 30 \n" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var2 AN var3 \n" +
				"        OIC \n" +
				"IM OUTTA YR loop ");
		try {
			Assert.assertTrue(p.isLoop(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIf1' ----");
//		p.s = new Scanner("BOTH SAEM var2 AN 30 \n" +
		p.s = new Scanner("" +
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
		try {
			Assert.assertTrue(p.isIf(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIf2' ----");
//		p.s = new Scanner("BOTH SAEM var2 AN 30 \n" +
		p.s = new Scanner("" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var2 AN var3 \n" +
				"        NO WAI \n" +
				"            PRODUKT OF var5 AN var6 \n" +
				"    OIC ");
		try {
			Assert.assertTrue(p.isIf(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf3() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIf3' ----");
//		p.s = new Scanner("BOTH SAEM var2 AN 30 \n" +
		p.s = new Scanner("" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var2 AN var3 \n" +
				"        MEBBE BOTH SAEM var2 AN 40 \n" +
				"            SUM OF var2 AN var4 \n" +
				"        MEBBE ALL OF WIN AN WIN AN WIN MKAY \n" +
				"            QUOSHUNT OF var3 AN var4 \n" +
				"    OIC ");
		try {
			Assert.assertTrue(p.isIf(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf4() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIf4' ----");
//		p.s = new Scanner("BOTH SAEM var2 AN 30 \n" +
		p.s = new Scanner("" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var2 AN var3 \n" +
				"        MEBBE BOTH OF var2 AN WIN \n" +
				"            SUM OF var2 AN var4 \n" +
				"        MEBBE ALL OF WIN AN WIN AN WIN MKAY \n" +
				"            QUOSHUNT OF var3 AN var4 \n" +
				"    OIC ");
		try {
			Assert.assertTrue(p.isIf(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotIf() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- expecting error notice 'testNotIf' ----");
//		p.s = new Scanner("BOTH SAEM var2 AN 30 \n" +
		p.s = new Scanner("" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var2 AN var3 \n" +
				"        MEBBE SUM OF var2 AN var4 \n" +
				"    OIC ");
		try {
			p.isIf(p.s.lookupToken());
			Assert.assertTrue(p.isError());
			System.out.println("-----------------------------------------");
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testOperation1' ----");
		p.s = new Scanner("BOTH OF var2 AN WIN ");
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testOperation2' ----");
		p.s = new Scanner("SUM OF var2 AN 30 ");
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation3() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testOperation3' ----");
		p.s = new Scanner("DIFF OF var2 AN 30 ");
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation4() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testOperation4' ----");
		p.s = new Scanner("QUOSHUNT OF SUM OF DIFF OF var1 AN var2 AN 30 AN 40");
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation5() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testOperation5' ----");
		p.s = new Scanner("QUOSHUNT OF 40 AN SUM OF DIFF OF var1 AN var2 AN 30");
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testStringOp() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testStringOp' ----");
		p.s = new Scanner("\"test\"");
		Assert.assertTrue(p.isStringOp(p.s.lookupToken()));
		Assert.assertTrue(!p.isError());
	}
	
	@Test
	public void testNumOp1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp1' ----");
		p.s = new Scanner("DIFF OF 30 AN DIFF OF 40 AN 50");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp2' ----");
		p.s = new Scanner("30");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp3() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp3' ----");
		p.s = new Scanner("BIGGR OF 30 AN 40");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp4() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp4' ----");
		p.s = new Scanner("SMALLR OF 30 AN 40");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNum() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNum' ----");
		p.s = new Scanner("30");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertTrue(!p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testVarAssign() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testVarAssign' ----");
		p.s = new Scanner("var1 R SUM OF 1 AN 2");
		try {
			Assert.assertTrue(p.isVarAssign(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testType() {
		Parser p = new Parser();
		p.parseTest = true;
		p.s = new Scanner("NUMBR");
		System.out.println("---- 'testType' ----");
		Assert.assertTrue(p.isType(p.s.lookupToken()));
		Assert.assertTrue(!p.isError());
	}
	
	@Test
	public void testVarDecl() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testVarDecl' ----");
		p.s = new Scanner("var1 IS NOW A NUMBR");
		try {
			Assert.assertTrue(p.isVarDecl(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIdentifier1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIdentifier1' ----");
		p.s = new Scanner("var1");
		Assert.assertTrue(p.isIdentifier(p.s.lookupToken()));
		Assert.assertTrue(!p.isError());
	}
	
	@Test
	public void testIdentifier2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIdentifier2' ----");
		p.s = new Scanner("var_1");
		Assert.assertTrue(p.isIdentifier(p.s.lookupToken()));
		Assert.assertTrue(!p.isError());
	}
	
	@Test
	public void testNotIdentifier() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- expecting error notice 'testNotIdentifier' ----");
		p.s = new Scanner("1var");
		Assert.assertFalse(p.isIdentifier(p.s.lookupToken()));
//		Assert.assertTrue(p.isError());
		System.out.println("-----------------------------------------");
	}
	
	@Test
	public void testFunction1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFunction1' ----");
		p.s = new Scanner("" +
				"HOW DUZ I func_name \n" +
				"    BOTH SAEM var1 AN var2 \n" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var1 AN var2 \n" +
				"    OIC \n" +
				"IF YOU SAY SO");
		try {
			Assert.assertTrue(p.isFunction(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFunction2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFunction2' ----");
		p.s = new Scanner("" +
				"HOW DUZ I func_name YR NUMBR arg1 AN YR CHARZ arg2 AN YR TROOF arg3 \n" +
				"    BOTH SAEM var1 AN var2 \n" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var1 AN var2 \n" +
				"    OIC \n" +
				"IF YOU SAY SO");
		try {
			Assert.assertTrue(p.isFunction(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFunction3() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFunction3' ----");
		p.s = new Scanner("" +
				"HOW DUZ I func_name YR arg1 AN YR arg2 AN YR arg3 \n" +
				"    BOTH SAEM var1 AN var2 \n" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var1 AN var2 \n" +
				"    OIC \n" +
				"FOUND YR var1 \n" +
				"IF YOU SAY SO");
		try {
			Assert.assertTrue(p.isFunction(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFunction4() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFunction4' ----");
		p.s = new Scanner("" +
				"HOW DUZ I func_name YR arg1 AN YR arg2 AN YR arg3 \n" +
				"    SUM OF var1 AN 20 \n" +
				"    GTFO \n" +
				"    BOTH SAEM var1 AN var2 \n" +
				"    ORLY? \n" +
				"        YA RLY \n" +
				"            SUM OF var1 AN var2 \n" +
				"    OIC \n" +
				"IF YOU SAY SO");
		try {
			Assert.assertTrue(p.isFunction(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
}
