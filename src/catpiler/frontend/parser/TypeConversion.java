package catpiler.frontend.parser;

import catpiler.backend.codegeneration.CodeGenerator;
import catpiler.backend.codegeneration.MemoryManager;
import catpiler.frontend.parser.symboltable.Symboltable;
import catpiler.frontend.parser.symboltable.SymboltableEntry;
import catpiler.frontend.scanner.keywords.FAIL;
import catpiler.frontend.scanner.keywords.Keyword;
import catpiler.frontend.scanner.keywords.TROOF;
import catpiler.frontend.scanner.keywords.WIN;
import catpiler.utils.ErrorReporter;

public class TypeConversion {
	
	private static CodeGenerator codeGenerator;
	
	private static MemoryManager memoryManager;
	
	private static Symboltable symboltable;
	
	public static void init(CodeGenerator codeGenerator, Symboltable symboltable, MemoryManager memoryManager) {
		TypeConversion.codeGenerator = codeGenerator;
		TypeConversion.symboltable = symboltable;
		TypeConversion.memoryManager = memoryManager;
	}

	public static void int2bool(Keyword keyword ) {
		SymboltableEntry ste = symboltable.get(keyword.getAttribute());
		if(ste != null) {
			if(ste.getCategory().equals("const")) {
				if(new Integer(ste.getAttribute()) > 0) {
					ste.setAttribute(WIN.tokenId);
				} else {
					ste.setAttribute(FAIL.tokenId);
				}
			} else if(ste.getCategory().equals("var") || 
					ste.getCategory().equals("reg")) {
				String reg;
				if(ste.getCategory().equals("var")) {
					reg = codeGenerator.loadWord(ste.getAddress());
					ste.setReg(reg);
					ste.setCategory("reg");
				} else {
					reg = ste.getReg();
				}
				// set register if 0 < reg
				codeGenerator.put("SLT", reg, "$r0", reg);
			}
			ste.setType(TROOF.tokenId);
		}
	}
	
	public static void int2char(Keyword keyword ) {
		SymboltableEntry ste = symboltable.get(keyword.getAttribute());
		if(ste != null) {
			if(ste.getCategory().equals("const")) {
				Integer v = new Integer(ste.getAttribute());
				if(v < -127) {
					ErrorReporter.markWarning(
							"Type conversion int -> char: Integer exceeds 1 byte");
					ste.setAttribute("128");
				} else if(v > 128) {
					ErrorReporter.markWarning(
							"Type conversion int -> char: Integer exceeds 1 byte");
					ste.setAttribute("-127");
				}
			} else if(ste.getCategory().equals("var") || 
					ste.getCategory().equals("reg")) {
				String reg;
				if(ste.getCategory().equals("var")) {
					reg = codeGenerator.loadByte("3(" + ste.getAddress() + ")");
					ste.setReg(reg);
					ste.setCategory("reg");
				} else {
					reg = ste.getReg();
					String tmp = codeGenerator.getNextFreeTemporary();
					codeGenerator.put("ADDI", tmp, "$r0", "24");
					// extract last 8 bit:
					codeGenerator.put("SLL", reg, tmp);
					codeGenerator.put("SRL", reg, tmp);
				}
			}
			ste.setType(TROOF.tokenId);
		}
	}

	public static void int2string(Keyword keyword, int size ) {
		
	}
	
	public static void int2intarray(Keyword keyword, int size ) {
		
	}
	
	public static void bool2int(Keyword keyword ) {
		
	}
	
	public static void boolarray2null(Keyword keyword ) {
		SymboltableEntry ste = symboltable.get(keyword.getAttribute());
		if(ste != null) {
			java.lang.String heapstart = ste.getHeap();
			// free memory
			memoryManager.freeMemory(heapstart, ste.getHeapsize());
		}
	}
	
	public static void boolarray2intarray(Keyword keyword, int size ) {
		
	}
	
	public static void boolarray2boolarray(Keyword keyword, int size ) {
		
	}
	
	public static void char2int(Keyword keyword ) {
		
	}
	
	public static void char2string(Keyword keyword, int size ) {
		
	}

	public static void intarray2null(Keyword keyword ) {
		SymboltableEntry ste = symboltable.get(keyword.getAttribute());
		if(ste != null) {
			java.lang.String heapstart = ste.getHeap();
			// free memory
			memoryManager.freeMemory(heapstart, ste.getHeapsize());
		}
	}
	
	public static void intarray2string(Keyword keyword, int size ) {
		
	}
	
	public static void intarray2intarray(Keyword keyword, int size ) {
		
	}
	
	public static void intarray2boolarray(Keyword keyword, int size ) {
		
	}
	
	public static void stirng2null(Keyword keyword ) {
		SymboltableEntry ste = symboltable.get(keyword.getAttribute());
		if(ste != null) {
			java.lang.String heapstart = ste.getHeap();
			// free memory
			memoryManager.freeMemory(heapstart, ste.getHeapsize());
		}
	}
	
	public static void string2string(Keyword keyword, int size ) {
		
	}
	
	public static void string2int(Keyword keyword ) {
		
	}
	
	public static void null2bool(Keyword keyword ) {
		
	}
	
	public static void null2boolarray(Keyword keyword, int size ) {
		SymboltableEntry ste = symboltable.get(keyword.getAttribute());
		if(ste != null) {
			if(ste.getCategory().equals("const")) {
				
				int start = memoryManager.allocateMemory(size);
		//		codeGenerator.decreaseSp(wordAligned);
				ste.setHeap("hp" + new Integer(start).toString());
				int blockAmount = 0;
				if(size % memoryManager.BLOCKSIZE != 0) {
					blockAmount = ((int) (size / memoryManager.BLOCKSIZE)) +1;
				} else {
					blockAmount = (int)(size / memoryManager.BLOCKSIZE);
				}
				ste.setHeapsize(blockAmount);
				ste.setCategory("heap");
			} else if(ste.getCategory().equals("var") || 
					ste.getCategory().equals("reg")) {
				String reg;
				if(ste.getCategory().equals("var")) {
					reg = codeGenerator.loadWord(ste.getAddress());
					ste.setCategory("reg");
					ste.setReg("reg");
				} else {
					reg = ste.getReg();
				}
				
				int start = memoryManager.allocateMemory(size);
		//		codeGenerator.decreaseSp(wordAligned);
				ste.setHeap("hp" + new Integer(start).toString());
				int blockAmount = 0;
				if(size % memoryManager.BLOCKSIZE != 0) {
					blockAmount = ((int) (size / memoryManager.BLOCKSIZE)) +1;
				} else {
					blockAmount = (int)(size / memoryManager.BLOCKSIZE);
				}
				ste.setHeapsize(blockAmount);
				ste.setCategory("heap");
				
				// loads address of global variable hpx into register
				codeGenerator.put("la", reg, ste.getHeap());
				// storing?!
			}
		}
	}
	
	public static void null2char(Keyword keyword ) {
		
	}

	public static void null2string(Keyword keyword, int size ) {
		SymboltableEntry ste = symboltable.get(keyword.getAttribute());
		if(ste != null) {
			if(ste.getCategory().equals("const")) {
				
				int start = memoryManager.allocateMemory(size+1);
		//		codeGenerator.decreaseSp(wordAligned);
				ste.setHeap("hp" + new Integer(start).toString());
				int blockAmount = 0;
				if(size % memoryManager.BLOCKSIZE != 0) {
					blockAmount = ((int) (size / memoryManager.BLOCKSIZE)) +1;
				} else {
					blockAmount = (int)(size / memoryManager.BLOCKSIZE);
				}
				ste.setHeapsize(blockAmount);
				ste.setCategory("heap");
			} else if(ste.getCategory().equals("var") || 
					ste.getCategory().equals("reg")) {
				String reg;
				if(ste.getCategory().equals("var")) {
					reg = codeGenerator.loadWord(ste.getAddress());
					ste.setCategory("reg");
					ste.setReg("reg");
				} else {
					reg = ste.getReg();
				}
				
				int start = memoryManager.allocateMemory(size+1);
		//		codeGenerator.decreaseSp(wordAligned);
				ste.setHeap("hp" + new Integer(start).toString());
				int blockAmount = 0;
				if(size % memoryManager.BLOCKSIZE != 0) {
					blockAmount = ((int) (size / memoryManager.BLOCKSIZE)) +1;
				} else {
					blockAmount = (int)(size / memoryManager.BLOCKSIZE);
				}
				ste.setHeapsize(blockAmount);
				ste.setCategory("heap");
				
				// loads address of global variable hpx into register
				codeGenerator.put("la", reg, ste.getHeap());
				// storing?!
			}
		}
	}
	
	public static void null2intarray(Keyword keyword, int size ) {
		SymboltableEntry ste = symboltable.get(keyword.getAttribute());
		if(ste != null) {
			if(ste.getCategory().equals("const")) {
				
				int start = memoryManager.allocateMemory(size);
		//		codeGenerator.decreaseSp(wordAligned);
				ste.setHeap("hp" + new Integer(start).toString());
				int blockAmount = 0;
				if(size % memoryManager.BLOCKSIZE != 0) {
					blockAmount = ((int) (size / memoryManager.BLOCKSIZE)) +1;
				} else {
					blockAmount = (int)(size / memoryManager.BLOCKSIZE);
				}
				ste.setHeapsize(blockAmount);
				ste.setCategory("heap");
			} else if(ste.getCategory().equals("var") || 
					ste.getCategory().equals("reg")) {
				String reg;
				if(ste.getCategory().equals("var")) {
					reg = codeGenerator.loadWord(ste.getAddress());
					ste.setCategory("reg");
					ste.setReg("reg");
				} else {
					reg = ste.getReg();
				}
				
				int start = memoryManager.allocateMemory(size);
		//		codeGenerator.decreaseSp(wordAligned);
				ste.setHeap("hp" + new Integer(start).toString());
				int blockAmount = 0;
				if(size % memoryManager.BLOCKSIZE != 0) {
					blockAmount = ((int) (size / memoryManager.BLOCKSIZE)) +1;
				} else {
					blockAmount = (int)(size / memoryManager.BLOCKSIZE);
				}
				ste.setHeapsize(blockAmount);
				ste.setCategory("heap");
				
				// loads address of global variable hpx into register
				codeGenerator.put("la", reg, ste.getHeap());
				// storing?!
			}
		}
	}
	
	public static void null2int(Keyword keyword ) {
		
	}
	
	public static void null2record(Keyword keyword, int size) {
		SymboltableEntry ste = symboltable.get(keyword.getAttribute());
		if(ste != null) {
			if(ste.getCategory().equals("const")) {
				int start = memoryManager.allocateMemory(size);
		//		codeGenerator.decreaseSp(wordAligned);
				ste.setHeap("hp" + new Integer(start).toString());
				int blockAmount = 0;
				if(size % memoryManager.BLOCKSIZE != 0) {
					blockAmount = ((int) (size / memoryManager.BLOCKSIZE)) +1;
				} else {
					blockAmount = (int)(size / memoryManager.BLOCKSIZE);
				}
				ste.setHeapsize(blockAmount);
				ste.setCategory("heap");
			} else if(ste.getCategory().equals("var") || 
					ste.getCategory().equals("reg")) {
				String reg;
				if(ste.getCategory().equals("var")) {
					reg = codeGenerator.loadWord(ste.getAddress());
					ste.setCategory("reg");
					ste.setReg("reg");
				} else {
					reg = ste.getReg();
				}
				
				int start = memoryManager.allocateMemory(size);
		//		codeGenerator.decreaseSp(wordAligned);
				ste.setHeap("hp" + new Integer(start).toString());
				int blockAmount = 0;
				if(size % memoryManager.BLOCKSIZE != 0) {
					blockAmount = ((int) (size / memoryManager.BLOCKSIZE)) +1;
				} else {
					blockAmount = (int)(size / memoryManager.BLOCKSIZE);
				}
				ste.setHeapsize(blockAmount);
				ste.setCategory("heap");
				
				// loads address of global variable hpx into register
				codeGenerator.put("la", reg, ste.getHeap());
				// storing?!
			}
		}
	}
	
	public static void null2null() {
		// do nothing
	}
	
}
