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
//		if(string.getCategory().equals("const")) {
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
				string.setCategory("heap");
			}
//			if(string.isNeedRefresh()) {
				String tmpAddress = codeGenerator.getNextFreeTemporary();
				codeGenerator.put("la", tmpAddress, string.getHeap());
				codeGenerator.put("lw", tmpAddress, "("+tmpAddress+")");
				String tmpChar = codeGenerator.getNextFreeTemporary();
				for(int i=0; i<size; i++) {
					int c = string.getAttribute().charAt(i);
					codeGenerator.put("addi", tmpChar, "$zero", new Integer(c).toString());
					codeGenerator.put("sb", tmpChar, "("+tmpAddress+")");
					codeGenerator.put("addi", tmpAddress, tmpAddress, "1");
				}
				codeGenerator.put("sb", "$zero", "("+tmpAddress+")");
				codeGenerator.releaseRegister(tmpAddress);
				codeGenerator.releaseRegister(tmpChar);
//				string.setNeedRefresh(false);
//			}
//		}
	}
	
	public static void strCmp(SymboltableEntry string1, SymboltableEntry string2) {
		if(!string1.getType().equals(CHARZ.tokenId) || 
				!string2.getType().equals(CHARZ.tokenId)) {
			ErrorReporter.markError("String comparison needs two strings as arguments");
		}
		
//		codeGenerator.put("strcmp_:");
//		String reg_str1 = codeGenerator.moveFromArgs(string1.getReg());
//		string1.setReg(reg_str1);
//		String reg_str2 = codeGenerator.moveFromArgs(string2.getReg());
//		string2.setReg(reg_str2);
		codeGenerator.put("strcmploop_:");
		String oneByteStr1 = codeGenerator.loadByte(string1.getReg());
		String oneByteStr2 = codeGenerator.loadByte(string2.getReg());
		// getbranchlabel
		String bl1 = codeGenerator.getBranchLabel();
		String bl2 = codeGenerator.getBranchLabel();
		String bl3 = codeGenerator.getBranchLabel();
		String bl4 = codeGenerator.getBranchLabel();
		codeGenerator.put("beq", oneByteStr1, "$zero", bl1);
		codeGenerator.put("beq", oneByteStr2, "$zero", bl2);
		String tmp = codeGenerator.getNextFreeTemporary();
		codeGenerator.put("slt", tmp, oneByteStr1, oneByteStr2);
		codeGenerator.put("bnez", tmp, bl2);
		codeGenerator.put("slt", tmp, oneByteStr2, oneByteStr1);
		codeGenerator.put("bne", tmp, "$zero", bl2);
		codeGenerator.put("addi", string1.getReg(), string1.getReg(), "1");
		codeGenerator.put("addi", string2.getReg(), string2.getReg(), "1");
		codeGenerator.put("j", "strcmploop_");
		codeGenerator.put(bl1+":");
		codeGenerator.put("beq", oneByteStr2, "$zero", bl3);
		codeGenerator.put("j", bl2);
		codeGenerator.put(bl2+":");
		codeGenerator.put("addi", "$v1", "$zero", "0");
		codeGenerator.put("j", bl4);
		codeGenerator.put(bl3+":");
		codeGenerator.put("addi", "$v1", "$zero", "1");
		codeGenerator.put(bl4+":");
//		codeGenerator.put("jr", "$ra");
		
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
