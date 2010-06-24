package catpiler.frontend.test;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import catpiler.frontend.exception.ParseException;
import catpiler.frontend.parser.Parser;
import catpiler.frontend.scanner.Scanner;

public class CodeGeneratorTest {

	@Test
	public void testNumOp1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp1' ----");
		p.s = new Scanner("DIFF OF 30 AN DIFF OF 50 AN 40");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("20", p.currentSymboltableEntry.getAttribute());
			System.out.println("Result of num operation is: " + p.currentSymboltableEntry.getAttribute());
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
		p.s = new Scanner("DIFF OF 30 AN DIFF OF 40 AN 50");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("40", p.currentSymboltableEntry.getAttribute());
			System.out.println("Result of num operation is: " + p.currentSymboltableEntry.getAttribute());
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
		p.s = new Scanner("PRODUKT OF 30 AN DIFF OF 40 AN 50");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("-300", p.currentSymboltableEntry.getAttribute());
			System.out.println("Result of num operation is: " + p.currentSymboltableEntry.getAttribute());
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
		p.s = new Scanner("SUM OF DIFF OF 40 AN 50 AN 30");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			System.out.println("Result of num operation is: " + p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("20", p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp5() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp5' ----");
		p.s = new Scanner("SUM OF QUOSHUNT OF 100 AN 10 AN 30");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			System.out.println("Result of num operation is: " + p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("40", p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp6() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp6' ----");
		p.s = new Scanner("SUM OF QUOSHUNT OF PRODUKT OF 5 AN 10 AN 5 AN 30");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			System.out.println("Result of num operation is: " + p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("40", p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp7() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp7' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R 4 \n" +
				"var2 R 5 \n" +
				"PRODUKT OF var1 AN var2\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			System.out.println("Result of num operation is: " + p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("20", p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp8() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp8' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R 4 \n" +
				"var2 R 5 \n" +
				"BIGGR OF var1 AN var2\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			System.out.println("Result of num operation is: " + p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("5", p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp9() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp9' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R 4 \n" +
				"PRODUKT OF var1 AN var2\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getReg());
			Assert.assertEquals("reg", p.currentSymboltableEntry.getCategory());
			System.out.println("Result of num operation is stored in address: " + p.currentSymboltableEntry.getReg());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp10() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp10' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R 4 \n" +
				"BIGGR OF var1 AN var2\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getReg());
			Assert.assertEquals("reg", p.currentSymboltableEntry.getCategory());
			System.out.println("Result of num operation is stored in address: " + p.currentSymboltableEntry.getReg());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp11() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp11' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var2 R 4 \n" +
				"BIGGR OF var1 AN var2\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getReg());
			Assert.assertEquals("reg", p.currentSymboltableEntry.getCategory());
			System.out.println("Result of num operation is stored in address: " + p.currentSymboltableEntry.getReg());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp12() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp12' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"BIGGR OF var1 AN var2\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getReg());
			Assert.assertEquals("reg", p.currentSymboltableEntry.getCategory());
			System.out.println("Result of num operation is stored in address: " + p.currentSymboltableEntry.getReg());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOp13() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOp13' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"PRODUKT OF var1 AN var2\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getReg());
			Assert.assertEquals("reg", p.currentSymboltableEntry.getCategory());
			System.out.println("Result of num operation is stored in address: " + p.currentSymboltableEntry.getReg());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBoolOp1' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R WIN \n" +
				"var2 R FAIL \n" +
				"EITHER OF var1 AN var2\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("const", p.currentSymboltableEntry.getCategory());
			System.out.println("Result of bool operation is: " + p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("1", p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBoolOp2' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R FAIL \n" +
				"var2 R FAIL \n" +
				"EITHER OF var1 AN var2\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("const", p.currentSymboltableEntry.getCategory());
			System.out.println("Result of bool operation is: " + p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("0", p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOpFail1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBoolOpFail1' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R FAIL \n" +
				"var2 R FAIL \n" +
				"EITHER OF var1 AN var2\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("const", p.currentSymboltableEntry.getCategory());
			System.out.println("Result of bool operation is: " + p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("0", p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp3() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBoolOp3' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R WIN \n" +
				"var2 R WIN \n" +
				"EITHER OF var1 AN var2\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("const", p.currentSymboltableEntry.getCategory());
			System.out.println("Result of bool operation is: " + p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("1", p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp4() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBoolOp4' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R WIN \n" +
				"var2 R WIN \n" +
				"BOTH OF var1 AN var2\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("const", p.currentSymboltableEntry.getCategory());
			System.out.println("Result of bool operation is: " + p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("1", p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testBoolOp5() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testBoolOp5' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R FAIL \n" +
				"var2 R WIN \n" +
				"BOTH OF var1 AN var2\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			Assert.assertNotNull(p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("const", p.currentSymboltableEntry.getCategory());
			System.out.println("Result of bool operation is: " + p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("0", p.currentSymboltableEntry.getAttribute());
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
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R FAIL \n" +
				"var2 R WIN \n" +
				"BOTH OF var1 AN var2\n" +
				"ORLY? \n" +
				"YA RLY\n" +
				"    SUM OF 2 AN 3\n" +
				"OIC\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			System.out.println("CurrentSymbolTableEntry: " + 
					p.currentSymboltableEntry.getName() + " : " + 
					p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("0", p.currentSymboltableEntry.getAttribute());
			Assert.assertEquals("const", p.currentSymboltableEntry.getCategory());
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
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R FAIL \n" +
				"var2 R WIN \n" +
				"EITHER OF var1 AN var2\n" +
				"ORLY? \n" +
				"YA RLY\n" +
				"    SUM OF 2 AN 3\n" +
				"OIC\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			System.out.println("CurrentSymbolTableEntry: " + 
					p.currentSymboltableEntry.getName() + " : " + 
					p.currentSymboltableEntry.getAttribute());
//			Assert.assertEquals("0", p.currentSymboltableEntry.getAttribute());
//			Assert.assertEquals("const", p.currentSymboltableEntry.getCategory());
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
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R FAIL \n" +
				"var2 R WIN \n" +
				"BOTH OF var1 AN var2\n" +
				"ORLY? \n" +
				"YA RLY\n" +
				"    SUM OF 2 AN 3\n" +
				"MEBBE var2\n" +
				"    SUM OF 1 AN 1\n" +
				"OIC\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			System.out.println("CurrentSymbolTableEntry: " + 
					p.currentSymboltableEntry.getName() + " : " + 
					p.currentSymboltableEntry.getAttribute());
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
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R FAIL \n" +
				"var2 R WIN \n" +
				"BOTH OF var1 AN var2\n" +
				"ORLY? \n" +
				"YA RLY\n" +
				"    SUM OF 2 AN 3\n" +
				"MEBBE var1\n" +
				"    SUM OF 10 AN 10\n" +
				"MEBBE var2\n" +
				"    SUM OF 1 AN 1\n" +
				"OIC\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			System.out.println("CurrentSymbolTableEntry: " + 
					p.currentSymboltableEntry.getName() + " : " + 
					p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf5() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIf5' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R FAIL \n" +
				"var2 R WIN \n" +
				"BOTH OF var1 AN var2\n" +
				"ORLY? \n" +
				"YA RLY\n" +
				"    SUM OF 2 AN 3\n" +
				"MEBBE var2\n" +
				"    SUM OF 10 AN 10\n" +
				"MEBBE var1\n" +
				"    SUM OF 1 AN 1\n" +
				"OIC\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			System.out.println("CurrentSymbolTableEntry: " + 
					p.currentSymboltableEntry.getName() + " : " + 
					p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf6() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIf6' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"var1 R FAIL \n" +
				"var2 R FAIL \n" +
				"BOTH OF var1 AN var2\n" +
				"ORLY? \n" +
				"YA RLY\n" +
				"    SUM OF 2 AN 3\n" +
				"MEBBE var2\n" +
				"    SUM OF 10 AN 10\n" +
				"MEBBE var1\n" +
				"    SUM OF 1 AN 1\n" +
				"NO WAI\n" +
				"    SUM OF 3 AN 3\n" +
				"OIC\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			System.out.println("CurrentSymbolTableEntry: " + 
					p.currentSymboltableEntry.getName() + " : " + 
					p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf7() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIf7' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"BOTH OF var1 AN var2\n" +
				"ORLY? \n" +
				"YA RLY\n" +
				"    SUM OF 2 AN 3\n" +
				"MEBBE var2\n" +
				"    SUM OF 10 AN 10\n" +
				"MEBBE var1\n" +
				"    SUM OF 1 AN 1\n" +
				"NO WAI\n" +
				"    SUM OF 3 AN 3\n" +
				"OIC\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			System.out.println("CurrentSymbolTableEntry: " + 
					p.currentSymboltableEntry.getName() + " : " + 
					p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testIf8() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIf8' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"I HAS A var3 \n" +
				"I HAS A var4 \n" +
				"BOTH OF var1 AN var2\n" +
				"ORLY? \n" +
				"YA RLY\n" +
				"    SUM OF var3 AN 3\n" +
				"MEBBE var2\n" +
				"    SUM OF var3 AN var4\n" +
				"MEBBE var1\n" +
				"    SUM OF 1 AN var4\n" +
				"NO WAI\n" +
				"    PRODUKT OF var3 AN var4\n" +
				"OIC\n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			System.out.println("CurrentSymbolTableEntry: " + 
					p.currentSymboltableEntry.getName() + " : " + 
					p.currentSymboltableEntry.getAttribute());
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
		p.s = new Scanner("" +
				"HAI\n" +
				"I HAS A var1\n" +
				"IM IN YR loop TIL BOTH SAEM var1 AN 100 \n" +
				"    var1 R SUM OF 1 AN var1 \n" +
				"IM OUTTA YR loop \n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			System.out.println("CurrentSymbolTableEntry: " + 
					p.currentSymboltableEntry.getName() + " : " + 
					p.currentSymboltableEntry.getAttribute());
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
		p.s = new Scanner("" +
				"HAI\n" +
				"I HAS A var1\n" +
				"I HAS A var2\n" +
				"IM IN YR loop TIL BOTH OF var1 AN WIN \n" +
				"    var2 R SUM OF 1 AN var2 \n" +
				"IM OUTTA YR loop \n" +
				"KTHXBYE");
		try {
			Assert.assertTrue(p.isMain(p.s.lookupToken()));
			Assert.assertNotNull(p.currentSymboltableEntry);
			System.out.println("CurrentSymbolTableEntry: " + 
					p.currentSymboltableEntry.getName() + " : " + 
					p.currentSymboltableEntry.getAttribute());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testNumOpFail() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOpFail' ----");
		p.s = new Scanner("SUM OF QUOSHUNT OF 40 AN 0 AN 30");
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertTrue(p.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
}
