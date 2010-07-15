package catpiler.frontend.test;

import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import catpiler.backend.codegeneration.CodeGenerator;
import catpiler.backend.linker.Linker;
import catpiler.frontend.exception.ParseException;
import catpiler.frontend.parser.Parser;
import catpiler.frontend.scanner.Scanner;
import catpiler.utils.ErrorReporter;

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
	public void testIf9() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testIf9' ----");
		p.s = new Scanner("HAI \n" +
				"I HAS A var1 \n" +
				"I HAS A var2 \n" +
				"I HAS A var3 \n" +
				"I HAS A var4 \n" +
				"I HAS A var5 \n" +
				"var5 R 4\n" +
				"BOTH OF var1 AN var2\n" +
				"ORLY? \n" +
				"YA RLY\n" +
				"    var5 R SUM OF var3 AN 3\n" +
				"MEBBE var2\n" +
				"    SUM OF var3 AN var4\n" +
				"MEBBE var1\n" +
				"    var5 R SUM OF 1 AN var4\n" +
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
				"    var1 R WIN\n" +
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
	public void testFunction1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFunction1' ----");
		p.s = new Scanner("" +
				"HAI\n" +
				"I HAS A var1\n" +
				"I HAS A var2\n" +
				"IM IN YR loop TIL BOTH OF var1 AN WIN \n" +
				"    var2 R SUM OF 1 AN var2 \n" +
				"    var1 R WIN\n" +
				"IM OUTTA YR loop \n" +
				"KTHXBYE\n" +
				"\n" +
				"HOW DUZ I addArguments YR NUMBR arg1 AN YR NUMBR arg2\n" +
				"    FOUND YR SUM OF arg1 AN arg2 \n" +
				"IF YOU SAY SO");
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
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
	public void testFunction2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFunction2' ----");
		p.s = new Scanner("" +
				"HAI\n" +
				"I HAS A var1\n" +
				"I HAS A var2\n" +
				"I HAS A var3\n" +
				"IM IN YR loop TIL BOTH OF var1 AN WIN \n" +
				"    var2 R SUM OF 1 AN var2 \n" +
				"    var1 R WIN\n" +
				"IM OUTTA YR loop \n" +
				"CAN U addArguments var2 var3 ?\n" +
				"KTHXBYE\n" +
				"\n" +
				"HOW DUZ I addArguments YR NUMBR arg1 AN YR NUMBR arg2\n" +
				"    FOUND YR SUM OF arg1 AN arg2 \n" +
				"IF YOU SAY SO");
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
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
	public void testFunctionSmall() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFunctionSmall' ----");
		p.s = new Scanner("" +
				"HAI\n" +
				"I HAS A var1\n" +
				"var1 R 3\n" +
				"CAN U func var1 ?\n" +
				"KTHXBYE\n" +
				"\n" +
				"HOW DUZ I func YR NUMBR arg1\n" +
				"    I HAS A count\n" +
				"    count R 0\n" +
				"    IM IN YR printNtimes TIL BOTH SAEM count AN arg1\n" +
				"        count R SUM OF count AN 1\n" +
				"        VISIBLE count \n" +
				"    IM OUTTA YR printNtimes\n" +
				"IF YOU SAY SO");
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
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
	public void testPrint1() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testPrint1' ----");
		Scanner s = new Scanner("VISIBLE \"test bla tralala\"");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isPrint(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPrint2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testPrint2' ----");
		Scanner s = new Scanner("" +
				"HAI\n" +
				"    I HAS A var1\n" +
				"    I HAS A var2\n" +
				"    I HAS A var3\n" +
				"    DIFFRINT var1 AN var2\n" +
				"    ORLY?\n" +
				"    YA RLY\n" +
				"        var3 R 2" +
				"    NO WAI\n" +
				"        var3 R 5\n" +
				"    VISIBLE var3\n" +
				"    OIC\n" +
				"KTHXBYE");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPrint3() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testPrint3' ----");
		Scanner s = new Scanner("" +
				"HAI\n" +
				"    I HAS A var1\n" +
				"    var1 IS NOW A CHARZ 5\n" +
				"    var1 R \"abcde\"" +
				"    I HAS A var2\n" +
				"    var2 IS NOW A CHARZ 5\n" +
				"    I HAS A var3\n" +
				"    DIFFRINT var1 AN var2\n" +
				"    ORLY?\n" +
				"    YA RLY\n" +
				"        var3 R 2" +
				"    NO WAI\n" +
				"        var3 R 5\n" +
				"        VISIBLE var3\n" +
				"    OIC\n" +
				"KTHXBYE");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetline() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testGetline' ----");
		Scanner s = new Scanner("" +
				"HAI\n" +
				"    I HAS A var1\n" +
				"    var1 IS NOW A CHARZ 5\n" +
				"    GIMMEH var1\n" +
				"    BOTH SAEM var1 AN \"test\"\n" +
				"    ORLY?\n" +
				"        YA RLY\n" +
				"            VISIBLE \"you typed test\"\n" +
				"        NO WAI\n" +
				"            VISIBLE \"you didn't type test\"\n" +
				"    OIC\n" +
				"    VISIBLE var1\n" +
				"KTHXBYE");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testVarLoading() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testVarLoading' ----");
		Scanner s = new Scanner("" +
				"HAI\n" +
				"    I HAS A fn1\n" +
				"    I HAS A fn2\n" +
				"    fn1 IS NOW A NUMBR\n" +
				"    fn2 IS NOW A NUMBR\n" +
				"    fn1 R 0\n" +
				"    fn2 R 1\n" +
				"    IM IN YR testVarLoading TIL BOTH SAEM fn1 AN fn2\n" +
				"        I HAS A fn3\n" +
				"        fn3 R SUM OF fn1 AN fn2\n" +
				"        fn1 R fn2\n" +
				"        fn2 R fn3\n" +
				"    IM OUTTA YR testVarLoading\n" +
				"KTHXBYE");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFibonacci() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFibonacci' ----");
		Scanner s = new Scanner("" +
				"HAI\n" +
				"    I HAS A arg\n" +
				"    arg IS NOW A NUMBR\n" +
				"    GIMMEH arg\n" +
				"    VISIBLE \"calculating \"\n" +
				"        VISIBLE arg\n" +
				"        VISIBLE \". fibonacci number: \"\n" +
				"    CAN U fibonacci arg ?\n" +
				"KTHXBYE\n" +
				"HOW DUZ I fibonacci YR NUMBR arg\n" +
				"    I HAS A count\n" +
				"    I HAS A fn1\n" +
				"    I HAS A fn2\n" +
				"    count IS NOW A NUMBR\n" +
				"    fn1 IS NOW A NUMBR\n" +
				"    fn2 IS NOW A NUMBR\n" +
				"    fn1 R 0\n" +
				"    fn2 R 1\n" +
				"    count R 0\n" +
//				"    VISIBLE \" \"" +
//				"    VISIBLE fn1" +
				"    VISIBLE \" \"" +
				"    VISIBLE fn2" +
				"    IM IN YR calulateFib TIL BOTH SAEM count AN arg\n" +
				"        count R SUM OF count AN 1\n" +
				"        I HAS A fn3\n" +
				"        fn3 R SUM OF fn1 AN fn2\n" +
				"        fn1 R fn2\n" +
				"        fn2 R fn3\n" +
//				"        VISIBLE \" \"" +
				"        VISIBLE fn3\n" +
				"    IM OUTTA YR calulateFib\n" +
				"IF YOU SAY SO");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFibonacciFile() {
		Parser p = new Parser();
		Parser.loadSourcecode("src/catpiler/frontend/test/codeExamples/testcode.lol");
//		try {
//			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
	}
	
	@Test
	public void testFunctionReturnValueFile() {
		Parser p = new Parser();
		Parser.loadSourcecode("src/catpiler/frontend/test/codeExamples/testReturnValue.lol");
			Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testFunctionReturnValueFile2() {
		Parser p = new Parser();
		Parser.loadSourcecode("src/catpiler/frontend/test/codeExamples/testReturnValue2.lol");
			Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testRecFunction1() {
		Parser p = new Parser();
		Parser.loadSourcecode("src/catpiler/frontend/test/codeExamples/recursiveFunctionTest.lol");
			Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testRecFunction2() {
		Parser p = new Parser();
		Parser.loadSourcecode("src/catpiler/frontend/test/codeExamples/recFaculty.lol");
			Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testStruct1() {
		Parser p = new Parser();
		Parser.loadSourcecode("src/catpiler/frontend/test/codeExamples/stuffTest.lol");
			Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testStruct2() {
		Parser p = new Parser();
		Parser.loadSourcecode("src/catpiler/frontend/test/codeExamples/stuffTest2.lol");
			Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testStruct3() {
		Parser p = new Parser();
		Parser.loadSourcecode("src/catpiler/frontend/test/codeExamples/stuffTest3.lol");
			Assert.assertTrue(!ErrorReporter.isError());
	}

	@Test
	public void testEvaluation1() {
		Parser p = new Parser();
		Parser.loadSourcecode("src/catpiler/frontend/test/codeExamples/evaluation1.lol");
			Assert.assertTrue(!ErrorReporter.isError());
	}

	@Test
	public void testEvaluation2() {
		Parser p = new Parser();
		Parser.loadSourcecode("src/catpiler/frontend/test/codeExamples/evaluation2.lol");
			Assert.assertTrue(!ErrorReporter.isError());
	}
	
	@Test
	public void testEvaluation3() {
		Parser p = new Parser();
		Parser.loadSourcecode("src/catpiler/frontend/test/codeExamples/evaluation3.lol");
			Assert.assertTrue(!ErrorReporter.isError());
	}

	@Test
	public void testModules1() {
		Parser p = new Parser();
		Parser.loadSourcecode("src/catpiler/frontend/test/codeExamples/helloWorld.lol");
		Assert.assertTrue(!ErrorReporter.isError());
		Set<String> sources = new HashSet<String>();
		sources.add("src/catpiler/frontend/test/codeExamples/helloWorld.cat");
		sources.add("src/catpiler/frontend/test/codeExamples/strlib.cat");
		Linker.destination = "/home/sstroka/Desktop/main.asm";
		Linker.linkSources(sources);
	}
	
	@Test
	public void testFibonacci2() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testFibonacci2' ----");
		Scanner s = new Scanner("" +
				"HAI\n" +
				"    I HAS A arg\n" +
				"    arg IS NOW A NUMBR\n" +
				"    GIMMEH arg\n" +
				"    VISIBLE \"calculating \"\n" +
				"        VISIBLE arg\n" +
				"        VISIBLE \". fibonacci number: \"\n" +
				"    I HAS A count\n" +
				"    I HAS A fn1\n" +
				"    I HAS A fn2\n" +
				"    count IS NOW A NUMBR\n" +
				"    fn1 IS NOW A NUMBR\n" +
				"    fn2 IS NOW A NUMBR\n" +
				"    fn1 R 0\n" +
				"    fn2 R 1\n" +
				"    count R 0\n" +
//				"    VISIBLE \" \"" +
//				"    VISIBLE fn1" +
				"    VISIBLE \" \"" +
				"    VISIBLE fn2" +
				"    IM IN YR calulateFib TIL BOTH SAEM count AN arg\n" +
				"        count R SUM OF count AN 1\n" +
				"        I HAS A fn3\n" +
				"        fn3 R SUM OF fn1 AN fn2\n" +
				"        fn1 R fn2\n" +
				"        fn2 R fn3\n" +
				"        VISIBLE fn3\n" +
				"    IM OUTTA YR calulateFib\n" +
				"KTHXBYE\n");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isProgram(p.s.lookupToken()));
			Assert.assertTrue(!ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testNumOpFail() {
		Parser p = new Parser();
		p.parseTest = true;
		System.out.println("---- 'testNumOpFail' ----");
		Scanner s = new Scanner("SUM OF QUOSHUNT OF 40 AN 0 AN 30");
		p.s = s;
		ErrorReporter.setS(s);
		try {
			Assert.assertTrue(p.isNumOp(p.s.lookupToken()));
			Assert.assertTrue(ErrorReporter.isError());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testCleanUpTemporaries() {
		CodeGenerator codeGenerator = new CodeGenerator(null);
		codeGenerator.bitmap = new int[] { 
				// r0:
				1, 
				// assembler temporary - reserved by assembler
				1, 
				// values v0 and v1
				0, 0, 
				// arguments
				0, 0, 0, 0, 
				// temporaries
				0, 0, 0, 1, 1, 0, 1, 0, 
				// saved values
				0, 0, 0, 0, 0, 0, 0, 0,
				// temporaries
				0, 1,
				// reserved for use by interrupt/trap handler
				1, 1,
				// global pointer
				1,
				// stack pointer
				1,
				// frame pointer
				1,
				// return address
				1
			};
		codeGenerator.cleanUpTemporaries();
		Assert.assertEquals(1, codeGenerator.bitmap[1]);
		Assert.assertEquals(1, codeGenerator.bitmap[1]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[1]);
		Assert.assertEquals(1, codeGenerator.bitmap[1]);
		Assert.assertEquals(1, codeGenerator.bitmap[1]);
		Assert.assertEquals(1, codeGenerator.bitmap[1]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[0]);
		Assert.assertEquals(1, codeGenerator.bitmap[1]);
		Assert.assertEquals(1, codeGenerator.bitmap[1]);
		Assert.assertEquals(1, codeGenerator.bitmap[1]);
		Assert.assertEquals(1, codeGenerator.bitmap[1]);
		Assert.assertEquals(1, codeGenerator.bitmap[1]);
		Assert.assertEquals(1, codeGenerator.bitmap[1]);
		
		Assert.assertEquals(4, codeGenerator.nft);
//		for(int i=0; i<32; i++) {
//			System.out.print(codeGenerator.bitmap[i] + " ");
//		}
	}
}
