package catpiler.utils;

import catpiler.backend.codegeneration.CodeGenerator;
import catpiler.backend.codegeneration.MemoryManager;
import catpiler.frontend.parser.symboltable.SymboltableEntry;
import catpiler.frontend.scanner.keywords.CHARZ;

public class StringLib {
	
	private static CodeGenerator codeGenerator;
	
	private static MemoryManager memoryManager;
	
	public static void init(CodeGenerator codeGenerator, MemoryManager memoryManager) {
		StringLib.codeGenerator = codeGenerator;
		StringLib.memoryManager = memoryManager;
	}
	
	/**
	 * storeString() loads string into heap memory, if it is necessary
	 * @param string
	 */
	public static void storeString(SymboltableEntry string) {
		if(string.getCategory().equals("const")) {
			int size = string.getAttribute().length();
			if(string.getHeap() == null) {
				int heap = memoryManager.allocateMemory(size+1);
				string.setHeap("hp" + new Integer(heap).toString());
				int blocks;
				if(size % memoryManager.BLOCKSIZE == 0) {
					blocks = (int) (size / memoryManager.BLOCKSIZE);
				} else {
					blocks = (int) (size / memoryManager.BLOCKSIZE) + 1;
				}
				string.setHeapsize(blocks);
			}
			if(string.isNeedRefresh()) {
				String tmpAddress = codeGenerator.getNextFreeTemporary();
				codeGenerator.put("la", tmpAddress, string.getHeap());
				String tmpChar = codeGenerator.getNextFreeTemporary();
				for(int i=0; i<size; i++) {
					int c = string.getAttribute().charAt(i);
					codeGenerator.put("addi", tmpChar, "$r0", new Integer(c).toString());
					codeGenerator.put("sb", tmpAddress, tmpChar);
					codeGenerator.put("addi", tmpAddress, tmpAddress, "1");
				}
				codeGenerator.put("sb", tmpAddress, "$r0");
				string.setNeedRefresh(false);
			}
		}
	}
	
	public static void strCmp(SymboltableEntry string1, SymboltableEntry string2) {
		if(!string1.getType().equals(CHARZ.tokenId) || 
				!string2.getType().equals(CHARZ.tokenId)) {
			ErrorReporter.markError("String comparison needs two strings as arguments");
		}
		
		codeGenerator.put("strcmp_:");
		String reg_str1 = codeGenerator.moveFromArgs(string1.getReg());
		string1.setReg(reg_str1);
		String reg_str2 = codeGenerator.moveFromArgs(string2.getReg());
		string2.setReg(reg_str2);
		String oneByteStr1 = codeGenerator.loadByte(reg_str1);
		String oneByteStr2 = codeGenerator.loadByte(reg_str2);
		codeGenerator.put("begz", oneByteStr1, "9");
		codeGenerator.put("begz", oneByteStr2, "10");
		String tmp = codeGenerator.getNextFreeTemporary();
		codeGenerator.put("slt", tmp, oneByteStr1, oneByteStr2);
		codeGenerator.put("bnez", tmp, "8");
		codeGenerator.put("slt", tmp, oneByteStr2, oneByteStr1);
		codeGenerator.put("bnez", tmp, "6");
		codeGenerator.put("addi", oneByteStr1, oneByteStr1, "1");
		codeGenerator.put("addi", oneByteStr2, oneByteStr2, "1");
		codeGenerator.put("j", "-10");
		codeGenerator.put("begz", oneByteStr2, "4");
		codeGenerator.put("jr", "$ra");
		codeGenerator.put("addi", "$v0", "$r0", "1");
		codeGenerator.put("jr", "$ra");
		codeGenerator.put("addi", "$v0", "$r0", "0");
		codeGenerator.put("jr", "$ra");
		
//		codeGenerator.put("lb", arg1, arg2, arg3)
//			loop:
//			lb $t3($t1)  #load a byte from each string - lädt das erste byte von dem string hinter adresse aus t1 in t3
//			lb $t4($t2)  # lädt das erste byte von dem string hinter adresse aus t2 in t4
//			beqz $t3,checkt2 #str1 end - 0byte von t3 entdeckt? -> checkt2
//			beqz $t4,missmatch # str2 0byte, str1 aber nicht -> v0 = 1
//			slt $t5,$t3,$t4  #compare two bytes - t3 < t4? wenn ja: t5 = 1, wenn nein: t5 = 0
//			bnez $t5,missmatch # wenn t5 != 0 -> v0 = 1
//			slt $t5,$t4,$t3  #compare two bytes - t4 < t3? wenn ja: t5 = 1, wenn nein: t5 = 0
//			bnez $t5,missmatch # wenn t5 != 0 -> v0 = 1
//			addi $t1,$t1,1  #t1 points to the next byte of str1
//			addi $t2,$t2,1
//			j loop
	}

}
