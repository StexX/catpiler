package catpiler.frontend.test;

import static org.junit.Assert.fail;

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
			p.isModule(p.s.lookupToken());
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
			p.isModule(p.s.lookupToken());
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
			p.isModule(p.s.lookupToken());
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
				"THATSIT" +
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
				"var3 R \"some string\"" +
				"IF YOU SAY SO ");
		try {
			p.isModule(p.s.lookupToken());
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
			p.isMain(p.s.lookupToken());
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
			p.isStatement(p.s.lookupToken());
		} catch (SyntaxException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
}
