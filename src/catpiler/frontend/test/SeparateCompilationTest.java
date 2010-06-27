package catpiler.frontend.test;

import org.junit.Test;

import catpiler.frontend.parser.Parser;

public class SeparateCompilationTest {

	@Test
	public void testLoadFile() {
		Parser.loadSourcecode("src/catpiler/frontend/test/codeExamples/helloWorld.lol");
	}
	
}
