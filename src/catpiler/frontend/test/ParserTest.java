package catpiler.frontend.test;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import catpiler.frontend.exception.ParseException;
import catpiler.frontend.exception.SyntaxException;
import catpiler.frontend.parser.Parser;
import catpiler.frontend.scanner.Scanner;

public class ParserTest {

	@Test
	public void testStruct() {
		Parser p = new Parser();
		p.s = new Scanner("" +
				"STUFF " +
				"I HAS A var1 " +
				"I HAS A var2 " +
				"var1 IS NOW A NUMBR " +
				"var2 IS NOW A NUMBR " +
				"var1 R 3 " +
				"var2 R 2 " +
				"THATSIT");

		try {
			Assert.assertTrue(p.isStruct(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testModule() {
		Parser p = new Parser();
		p.s = new Scanner("" +
				"CAN HAS fileId ");
		try {
			Assert.assertTrue(p.isModule(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testVarInit() {
		Parser p = new Parser();
		p.s = new Scanner("" +
				"I HAS A var ");
		try {
			Assert.assertTrue(p.isVarInit(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testProgram() {
		Parser p = new Parser();
		p.s = new Scanner("" +
				"CAN HAS fileId " +
				"STUFF " +
				"I HAS A var1 " +
				"I HAS A var2 " +
				"var1 IS NOW A NUMBR " +
				"var2 IS NOW A NUMBR " +
				"var1 R 3 " +
				"var2 R 2 " +
				"THATSIT " +
				"HAI " +
				"I HAS A var1 " +
				"I HAS A var2 " +
				"var1 IS NOW A NUMBR " +
				"var2 IS NOW A NUMBR " +
				"var1 R 3 " +
				"var2 R 2 " +
				"SUM OF var1 AN var2 " +
				"PRODUKT OF var2 AN var1 " +
				"KTHXBYE " +
				"HOW DUZ I label " +
				"I HAS A var3 " +
				"var3 R \"some string\" " +
				"IF YOU SAY SO ");
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testMain() {
		Parser p = new Parser();
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
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testStatement() {
		Parser p = new Parser();
		p.s = new Scanner("" +
				"EITHER OF var3 AN var4 " +
				"ORLY? " +
				"YA RLY " +
				"    PRODUKT OF var2 AN var1 " +
				"OIC ");
		try {
			Assert.assertTrue(p.isStatement(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFuncCall1() {
		Parser p = new Parser();
		p.s = new Scanner("function1");
		try {
			Assert.assertTrue(p.isFuncCall(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFuncCall2() {
		Parser p = new Parser();
		p.s = new Scanner("function1 var1 2 \"str\"");
		try {
			Assert.assertTrue(p.isFuncCall(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFuncCall3() {
		Parser p = new Parser();
		p.s = new Scanner("function1 var1 SUM OF 2 AN 3");
		try {
			Assert.assertTrue(p.isFuncCall(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testExpr1() {
		Parser p = new Parser();
		p.s = new Scanner("ANY OF WIN AN FAIL AN FAIL MKAY");
		try {
			Assert.assertTrue(p.isExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testExpr2() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH SAEM 2 AN var2");
		try {
			Assert.assertTrue(p.isExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testExpr3() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH OF var1 AN var2");
		try {
			Assert.assertTrue(p.isExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp1() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH OF var1 AN var2");
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp2() {
		Parser p = new Parser();
		p.s = new Scanner("EITHER OF WIN AN var2");
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp3() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH OF FAIL AN var2");
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp4() {
		Parser p = new Parser();
		p.s = new Scanner("WIN");
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp5() {
		Parser p = new Parser();
		p.s = new Scanner("FAIL");
		try {
			Assert.assertTrue(p.isBoolOp(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGenExpr1() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH SAEM var1 AN var2");
		try {
			Assert.assertTrue(p.isGenExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGenExpr2() {
		Parser p = new Parser();
		p.s = new Scanner("DIFFRINT var1 AN var2");
		try {
			Assert.assertTrue(p.isGenExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGenExpr3() {
		Parser p = new Parser();
		// means: var1 <= 30
		p.s = new Scanner("BOTH SAEM 30 AN BIGGR OF 30 AN var1");
		try {
			Assert.assertTrue(p.isGenExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotGenExpr1() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH OF var1 AN var2");
		try {
			Assert.assertFalse(p.isGenExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotGenExpr2() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH var1 AN var2");
		try {
			p.isGenExpr(p.s.lookupToken());
			fail();
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			//expected
		}
	}
	
	@Test
	public void testBool1() {
		Parser p = new Parser();
		p.s = new Scanner("WIN");
		try {
			Assert.assertTrue(p.isBool(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBool2() {
		Parser p = new Parser();
		p.s = new Scanner("FAIL");
		try {
			Assert.assertTrue(p.isBool(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBool3() {
		Parser p = new Parser();
		p.s = new Scanner("\"test\"");
		try {
			Assert.assertFalse(p.isBool(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBool4() {
		Parser p = new Parser();
		p.s = new Scanner("1");
		try {
			Assert.assertFalse(p.isBool(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBool5() {
		Parser p = new Parser();
		p.s = new Scanner("bla1");
		try {
			Assert.assertFalse(p.isBool(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testInfExpr1() {
		Parser p = new Parser();
		p.s = new Scanner("ALL OF var1 AN var2 AN var3 MKAY");
		try {
			Assert.assertTrue(p.isInfExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testInfExpr2() {
		Parser p = new Parser();
		p.s = new Scanner("ANY OF var1 AN var2 AN var3 MKAY");
		try {
			Assert.assertTrue(p.isInfExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotInfExpr2() {
		Parser p = new Parser();
		p.s = new Scanner("ANY OF var1 AN var2 AN var3");
		try {
			p.isInfExpr(p.s.lookupToken());
			fail();
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			// expected
		}
	}
	
	@Test
	public void testBiExpr1() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH OF var1 AN var2");
		try {
			Assert.assertTrue(p.isBiExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBiExpr2() {
		Parser p = new Parser();
		p.s = new Scanner("EITHER OF var1 AN var2");
		try {
			Assert.assertTrue(p.isBiExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBiExpr3() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH SAEM var1 AN var2");
		try {
			Assert.assertTrue(p.isBiExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBiExpr4() {
		Parser p = new Parser();
		p.s = new Scanner("DIFFRINT var1 AN var2");
		try {
			Assert.assertTrue(p.isBiExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotBiExpr() {
		Parser p = new Parser();
		p.s = new Scanner("SUM OF var1 AN var2");
		try {
			Assert.assertFalse(p.isBiExpr(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFlowControl1() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH OF WIN AN WIN " +
				"ORLY? " +
				"YA RLY " +
				"    PRODUKT OF var2 AN var1 " +
				"OIC ");
		try {
			Assert.assertTrue(p.isFlowControl(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testFlowControl2() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH OF WIN AN WIN " +
				"ORLY? " +
				"YA RLY " +
				"    IM IN YR loop TIL BOTH SAEM var2 AN 100 " +
				"        PRODUKT OF var2 AN var1 " +
				"    IM OUTTA YR loop " +
				"OIC ");
		try {
			Assert.assertTrue(p.isFlowControl(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testLoop1() {
		Parser p = new Parser();
		p.s = new Scanner("IM IN YR loop TIL BOTH SAEM var2 AN 100 " +
				"    PRODUKT OF var2 AN var1 " +
				"    BOTH SAEM var2 AN 30 " +
				"    ORLY? " +
				"        YA RLY " +
				"            SUM OF var2 AN var3 " +
				"        OIC " +
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
	
	@Test
	public void testLoop2() {
		Parser p = new Parser();
		p.s = new Scanner("IM IN YR loop YR arg1 WILE BOTH SAEM var2 AN 100 " +
				"    PRODUKT OF var2 AN var1 " +
				"    BOTH SAEM var2 AN 30 " +
				"    ORLY? " +
				"        YA RLY " +
				"            SUM OF var2 AN var3 " +
				"        OIC " +
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
	
	@Test
	public void testIf1() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH SAEM var2 AN 30 " +
				"    ORLY? " +
				"        YA RLY " +
				"            SUM OF var2 AN var3 " +
				"        MEBBE BOTH SAEM var2 AN 40 " +
				"            SUM OF var2 AN var4 " +
				"        MEBBE ALL OF WIN AN WIN AN WIN MKAY " +
				"            QUOSHUNT OF var3 AN var4 " +
				"        NO WAI " +
				"            PRODUKT OF var5 AN var6 " +
				"    OIC ");
		try {
			Assert.assertTrue(p.isIf(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf2() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH SAEM var2 AN 30 " +
				"    ORLY? " +
				"        YA RLY " +
				"            SUM OF var2 AN var3 " +
				"        NO WAI " +
				"            PRODUKT OF var5 AN var6 " +
				"    OIC ");
		try {
			Assert.assertTrue(p.isIf(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf3() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH SAEM var2 AN 30 " +
				"    ORLY? " +
				"        YA RLY " +
				"            SUM OF var2 AN var3 " +
				"        MEBBE BOTH SAEM var2 AN 40 " +
				"            SUM OF var2 AN var4 " +
				"        MEBBE ALL OF WIN AN WIN AN WIN MKAY " +
				"            QUOSHUNT OF var3 AN var4 " +
				"    OIC ");
		try {
			Assert.assertTrue(p.isIf(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf4() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH SAEM var2 AN 30 " +
				"    ORLY? " +
				"        YA RLY " +
				"            SUM OF var2 AN var3 " +
				"        MEBBE BOTH OF var2 AN WIN " +
				"            SUM OF var2 AN var4 " +
				"        MEBBE ALL OF WIN AN WIN AN WIN MKAY " +
				"            QUOSHUNT OF var3 AN var4 " +
				"    OIC ");
		try {
			Assert.assertTrue(p.isIf(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNotIf() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH SAEM var2 AN 30 " +
				"    ORLY? " +
				"        YA RLY " +
				"            SUM OF var2 AN var3 " +
				"        MEBBE SUM OF var2 AN var4 " +
				"    OIC ");
		try {
			p.isIf(p.s.lookupToken());
			fail();
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			// expected
		}
	}
	
	@Test
	public void testOperation1() {
		Parser p = new Parser();
		p.s = new Scanner("BOTH OF var2 AN WIN ");
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation2() {
		Parser p = new Parser();
		p.s = new Scanner("SUM OF var2 AN 30 ");
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation3() {
		Parser p = new Parser();
		p.s = new Scanner("DIFF OF var2 AN 30 ");
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation4() {
		Parser p = new Parser();
		p.s = new Scanner("QUOSHUNT OF SUM OF DIFF OF var1 AN var2 AN 30 AN 40");
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testOperation5() {
		Parser p = new Parser();
		p.s = new Scanner("QUOSHUNT OF 40 AN SUM OF DIFF OF var1 AN var2 AN 30");
		try {
			Assert.assertTrue(p.isOperation(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testStringOp() {
		Parser p = new Parser();
		p.s = new Scanner("\"test\"");
		try {
			Assert.assertTrue(p.isStringOp(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp1() {
		Parser p = new Parser();
		p.s = new Scanner("DIFF OF 30 AN DIFF OF 40 AN 50");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp2() {
		Parser p = new Parser();
		p.s = new Scanner("30");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp3() {
		Parser p = new Parser();
		p.s = new Scanner("BIGGR OF 30 AN 40");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp4() {
		Parser p = new Parser();
		p.s = new Scanner("SMALLR OF 30 AN 40");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNum() {
		Parser p = new Parser();
		p.s = new Scanner("30");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testVarAssign() {
		Parser p = new Parser();
		p.s = new Scanner("var1 R SUM OF 1 AN 2");
		try {
			Assert.assertTrue(p.isVarAssign(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testType() {
		Parser p = new Parser();
		p.s = new Scanner("NUMBR");
		try {
			Assert.assertTrue(p.isType(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testVarDecl() {
		Parser p = new Parser();
		p.s = new Scanner("var1 IS NOW A NUMBR");
		try {
			Assert.assertTrue(p.isVarDecl(p.s.lookupToken()));
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
}
