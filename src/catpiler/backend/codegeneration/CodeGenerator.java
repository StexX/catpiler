package catpiler.backend.codegeneration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import catpiler.frontend.parser.Parser;
import catpiler.utils.ErrorReporter;

public class CodeGenerator {

	private static int CODESIZE = 100000;
	//we do not allow more than 1000 lines of data declarations
	private static int DATASIZE = 1000;
	/*
	 * comments for metadata are stored in the following format:
	 * #module_label:timestamp
	 * #
	 */
	private static int COMMENTSIZE = 5;
	private File file;
	private FileWriter fileWriter;
	
	private boolean codeDebug = true;
	private boolean dataDebug = false;
	
	private String data[] = new String[DATASIZE];
	private int dc = 0;
	private String code[] = new String[CODESIZE];
	private int pc = 0;

	private String moduleTimeStamp;
	private Set<String> functionDefs;
	private Set<String> functionCalls;
	
	// first 2 registers. r0 = 0, r1 = reserved
	private int r[];
	// v0: expression eval, v1: function results
	private int v[];
	// arguments
	private int a[];
	// callee saves
	private int s[];
	// temporary: caller saves
	private int t[];
	// pointer to global area
	private int gp;
	// frame pointer
	private int fp;
	// stack pointer
	private int sp;
	// return address
	private int ra;
	// register bitmap
	public int bitmap[];
	// next free temporary register
	public int nft;
	// next free argument register
	private int nfa;
	// next free saver register
	private int nfs;
	
	public int branchAndJumpCount;
	
	public CodeGenerator(String outputName) {
		if(outputName != null) {
			file = new File(outputName);
			try {
				fileWriter = new FileWriter(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		nft = 0;
		nfa = 0;
		nfs = 0;
		fp = sp = 0;
		t = new int[10];
		a = new int[4];
		v = new int[2];
		s = new int[6];
		// the bitmap illustrates which registers can be used and which cannot be used
		bitmap = new int[] { 
				// r0:
				1, 
				// assembler temporary - reserved by assembler
				1, 
				// values v0 and v1
				0, 0, 
				// arguments
				0, 0, 0, 0, 
				// temporaries
				0, 0, 0, 0, 0, 0, 0, 0, 
				// saved values
				0, 0, 0, 0, 0, 0, 0, 0,
				// temporaries
				0, 0,
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
		data = new String[DATASIZE];
		code = new String[CODESIZE];
		branchAndJumpCount = 0;
		functionCalls = new HashSet<String>();
		functionDefs = new HashSet<String>();
	}
	
	public String getModuleTimestamp() {
		return moduleTimeStamp;
	}

	public void setModuleTimestamp(String moduleTimeStamp) {
		this.moduleTimeStamp = moduleTimeStamp;
	}

	public Set<String> getFunctionDefs() {
		return functionDefs;
	}

	public void setFunctionDefs(Set<String> functionDefs) {
		this.functionDefs = functionDefs;
	}



	public Set<String> getFunctionCalls() {
		return functionCalls;
	}



	public void setFunctionCalls(Set<String> functionCalls) {
		this.functionCalls = functionCalls;
	}



	public void put(String op, String arg1, String arg2, String arg3) {
		if(!ErrorReporter.isError()) {
			String line = op + " " + arg1 + " " + arg2 + " " + arg3 + "\n";
			
			if(pc >= CODESIZE) {
				// TODO: ERROR HANDLING. THIS MUST NEVER HAPPEN!!!!!
			}
			code[pc] = line;
			if(codeDebug)
				System.out.println("code[" + pc + "] = " + line);
			pc++;
		}
	}
	
	public void put(String op, String arg1, String arg2) {
		if(!ErrorReporter.isError()) {
			String line = op + " " + arg1 + " " + arg2 + "\n";
			
			if(pc >= CODESIZE) {
				// TODO: ERROR HANDLING. THIS MUST NEVER HAPPEN!!!!!
			}
			code[pc] = line;
			if(codeDebug)
				System.out.println("code[" + pc + "] = " + line);
			pc++;
		}
	}
	
	public void put(String op, String arg1) {
		if(!ErrorReporter.isError()) {
			String line = op + " " + arg1 + "\n";
			
			if(pc >= CODESIZE) {
				// TODO: ERROR HANDLING. THIS MUST NEVER HAPPEN!!!!!
			}
			code[pc] = line;
			if(codeDebug)
				System.out.println("code[" + pc + "] = " + line);
			pc++;
		}
	}
	
	public void put(String op) {
		if(!ErrorReporter.isError()) {
			
			if(pc >= CODESIZE) {
				// TODO: ERROR HANDLING. THIS MUST NEVER HAPPEN!!!!!
			}
			code[pc] = op + "\n";
			if(codeDebug)
				System.out.println("code[" + pc + "] = " + op + "\n");
			pc++;
		}
	}
	
	public void storeData(String name, String type, String size) {
		if(!ErrorReporter.isError()) {
			
			if(dc >= DATASIZE) {
				// TODO: ERROR HANDLING. THIS MUST NEVER HAPPEN!!!!!
			}
			data[dc] = name + ": " + type + " " + size + "\n";
			if(dataDebug)
				System.out.println("data[" + dc + "] = " + name + " " + type + " " + size + "\n");
			dc++;
		}
	}
	
	public void storeData(String line) {
		if(!ErrorReporter.isError()) {
			
			if(dc >= DATASIZE) {
				// TODO: ERROR HANDLING. THIS MUST NEVER HAPPEN!!!!!
			}
			data[dc] = line + "\n";
			if(dataDebug)
				System.out.println("data[" + dc + "] = " + line + "\n");
			dc++;
		}
	}
	
	public String loadImmediately(String arg) {
		if(!ErrorReporter.isError()) {
			if(nft >= t.length) {
				cleanUpTemporaries();
				if(nft >= t.length) {
					// TODO: check whether there is enough 
					// space. If not: push temps on stack
				}
			}
//			t[nft] = arg;
			String reg = "$t" + nft;
			put("li", reg, arg);
			nft++;
			return reg;
		}
		return null;
	}
	
	public void releaseRegister(String reg) {
		if(reg != null && reg.startsWith("$t")) {
			Integer reg_number = new Integer(reg.substring(2));
			if(reg_number <= 7) {
				bitmap[reg_number + 8] = 0;
			} else if(reg_number == 8) {
				bitmap[24] = 0;
			} else if(reg_number == 9) {
				bitmap[25] = 0;
			}
			System.out.println("> Releasing register " + reg);
		}
	}
	
	public String loadWord(String address) {
		if(!ErrorReporter.isError()) {
			if(nft >= t.length) {
				cleanUpTemporaries();
				if(nft >= t.length) {
					// TODO: check whether there is enough 
					// space. If not: push temps on stack
				}
			}
			String reg = "$t" + nft;
			String stackAddr = address + "($fp)";
			put("lw", reg, stackAddr);
			if(nft <= 15) {
				bitmap[nft + 8] = 1;
			} else {
				if(nft == 16)
					bitmap[24] = 1;
				else
					bitmap[25] = 1;
			}
			nft++;
			return reg;
		}
		return null;
	}
	
	public String loadAddress(String heapLabel) {
		if(!ErrorReporter.isError()) {
			if(nft >= t.length) {
				cleanUpTemporaries();
				if(nft >= t.length) {
					// TODO: check whether there is enough 
					// space. If not: push temps on stack
				}
			}
			String reg = "$t" + nft;
			put("la", reg, heapLabel);
			if(nft <= 15) {
				bitmap[nft + 8] = 1;
			} else {
				if(nft == 16)
					bitmap[24] = 1;
				else
					bitmap[25] = 1;
			}
			nft++;
			return reg;
		}
		return null;
	}
	
	public String loadByte(int address) {
		if(!ErrorReporter.isError()) {
			if(nft >= t.length) {
				cleanUpTemporaries();
				if(nft >= t.length) {
					// TODO: check whether there is enough 
					// space. If not: push temps on stack
				}
			}
			t[nft] = address;
			String reg = "$t" + nft;
			String stackAddr = new Integer(address).toString() + "($fp)";
			put("lb", reg, stackAddr);
			if(nft <= 15) {
				bitmap[nft + 8] = 1;
			} else {
				if(nft == 16)
					bitmap[24] = 1;
				else
					bitmap[25] = 1;
			}
			nft++;
			return reg;
		}
		return null;
	}
	
	public String loadByte(String from_register) {
		if(!ErrorReporter.isError()) {
			if(nft >= t.length) {
				cleanUpTemporaries();
				if(nft >= t.length) {
					// TODO: check whether there is enough 
					// space. If not: push temps on stack
				}
			}
//			t[nft] = address;
			String target = "$t" + nft;
//			String stackAddr = new Integer(address).toString() + "($fp)";
//			put("lb", reg, stackAddr);
			put("lb", target, "(" + from_register + ")");
			if(nft <= 15) {
				bitmap[nft + 8] = 1;
			} else {
				if(nft == 16)
					bitmap[24] = 1;
				else
					bitmap[25] = 1;
			}
			nft++;
			return target;
		}
		return null;
	}
	
	public int[] cleanUpTemporaries() {
		int newTmp1[] = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
		// index for newTmp1
		int j = 0;
		// $t0 - $t7:
		for(int i=0; i<8; i++) {
			if(bitmap[8+i] == 1) {
				newTmp1[j] = 1;
				// move tmp register
				// maybe move tmp int array elements (if this array is needed anywhere)
				if(i != j)
					put("move", "$t"+j, "$t"+i );
				j++;
			}
		}
		// $t8:
		if(bitmap[24] == 1 && j < newTmp1.length) {
			newTmp1[j] = 1;
			if(j != 8)
				put("move", "$t"+j, "$t8" );
			j++;
			bitmap[24] = 0;
		}
		// $t9:
		if(bitmap[25] == 1 && j < newTmp1.length) {
			newTmp1[j] = 1;
			if(j != 9)
				put("move", "$t"+j, "$t9" );
			j++;
			bitmap[25] = 0;
		}
		
		int freebitcount = 0;
		for(int i=0; i<8; i++) {
			if(newTmp1[i] == 1) {
				bitmap[8+i] = 1;
			} else {
				freebitcount++;
				bitmap[8+i] = 0;
			}
		}
		nft = 8 - freebitcount;
		if(bitmap[24] == 1) {
			nft++;
		}
		if(bitmap[25] == 1) {
			nft++;
		}
		
		return bitmap;
	}
	
	public String loadArgs(int address) {
		if(!ErrorReporter.isError()) {
			if(nfa >= a.length) {
				// TODO: Push other register on the stack.
			}
			a[nfa] = address;
			String reg = "$a" + nfa;
			put("lw", reg, new Integer(address).toString());
			bitmap[nfa + 4] = 1;
			nfa++;
			return reg;
		}
		return null;
	}
	
	public void clearArgs() {
		nfa = 0;
	}
	
	public String move2Args(String reg) {
		if(!ErrorReporter.isError()) {
			if(nfa >= a.length) {
				// TODO: Push other register on the stack.
			}
			String args_reg = "$a" + nfa;
			put("move", args_reg, reg);
			nfa++;
			return args_reg;
		}
		return null;
	}
	
	public String moveFromArgs(String args_reg) {
		if(!ErrorReporter.isError()) {
			if(nft >= t.length) {
				cleanUpTemporaries();
				if(nft >= t.length) {
					// TODO: check whether there is enough 
					// space. If not: push temps on stack
				}
			}
			String reg = "$t" + nft;
			put("move", reg, args_reg);
			nft++;
			return reg;
		}
		return null;
	}
	
	public void move2Return(String reg) {
		put("move", "$v1", reg);
	}
	
	public void clearSavers() {
		nfs = 0;
	}
	
	public String saveReturnAddress() {
		put("move", "$s7", "$ra");
		return "$s7";
	}
	
	public void restoreReturnAddress() {
		put("move", "$ra", "$s7");
	}
	
	public String move2Savers(String reg) {
		if(reg.startsWith("$s")) {
			return reg;
		}
		if(!ErrorReporter.isError()) {
			if(nfs >= s.length) {
				// TODO: Push other register on the stack.
			}
			String ret_reg = "$s" + nfs;
			put("move", ret_reg, reg);
			nfs++;
			return ret_reg;
		}
		return null;
	}
	
	public void pushOnStack(int size) {
		String register = loadImmediately("0");
		// decrease stack pointer by 'size' bytes
		put("subu", "$sp", "$sp", new Integer(size).toString());
		// push loaded register onto stack
		put("sw", register, "($sp)");
	}

	public int getPc() {
		return pc;
	}
	
	public int getSp() {
		return sp;
	}
	
//	public void setSp(int sp) {
//		this.sp = sp;
//	}
	
	public void decreaseSp(int offset) {
		put("subu", "$sp", "$sp", new Integer(offset).toString());
		sp = sp - offset;
	}
	
	public int getFp() {
		return fp;
	}

	public String getNextFreeTemporary() {
		if(nft >= t.length) {
			cleanUpTemporaries();
			if(nft >= t.length) {
				// TODO: check whether there is enough 
				// space. If not: push temps on stack
				System.err.println("Not enough registers available");
				return null;
			}
		}
		String nextTemp = "$t" + new Integer(nft).toString();
		if(nft <= 15) {
			bitmap[nft + 8] = 1;
		} else {
			if(nft == 16)
				bitmap[24] = 1;
			else
				bitmap[25] = 1;
		}
		nft++;
		return nextTemp;
	}
	
	public void setFPisSP() {
		put("add", "$fp", "$zero", "$sp");
		this.fp = sp;
	}
	
	public void setSPisFP() {
		put("add", "$sp", "$zero", "$fp");
		this.sp = fp;
	}

	public void fixUp(int pc, int offset) {
		String line2fix = code[pc];
		String msg = "fixed line " + "[" + pc + "] : " + line2fix + ": ";
		code[pc] = line2fix.replace("offset", new Integer(offset).toString());
		msg = msg + code[pc];
		System.out.println(msg);
	}
	
	public java.lang.String getBranchLabel(String modulename) {
		java.lang.String brLabel1 = modulename + "br" + new Integer(branchAndJumpCount).toString();
		branchAndJumpCount++;
		return brLabel1;
	}
	
	public void finalizeCG() {
		if(fileWriter != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("#");
			sb.append(moduleTimeStamp);
			sb.append("\n");
			sb.append("#");
			for(String s : functionCalls) {
				sb.append(s);
				sb.append(",");
			}
			sb.append("\n");
			sb.append("#");
			for(String s : functionDefs) {
				sb.append(s);
				sb.append(",");
			}
			sb.append("\n");
			try {
				
				sb.append(".data");
				sb.append("\n");
				for(int i=0; i<dc; i++) {
					sb.append(data[i]);
				}
				sb.append(".text");
				sb.append("\n");
				for(int i=0; i<pc; i++) {
					sb.append(code[i]);
				}
				fileWriter.write(sb.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// TODO: error
		}
	}
}
