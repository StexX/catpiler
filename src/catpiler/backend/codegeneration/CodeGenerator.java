package catpiler.backend.codegeneration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import catpiler.frontend.parser.Parser;

public class CodeGenerator {

	private static int CODESIZE = 100000;
	private File file;
	private FileOutputStream fileOut;
	
	private String code[] = new String[CODESIZE];
	private int pc = 0;
	
	private Parser parser;
	
	// first 2 registers. r0 = 0, r1 = reserved
	public int r[];
	// v0: expression eval, v1: function results
	public int v[];
	// arguments
	public int a[];
	// callee saves
	public int s[];
	// temporary: caller saves
	public int t[];
	// pointer to global area
	public int gp;
	// frame pointer
	public int fp;
	// stack pointer
	public int sp;
	// return address
	public int ra;
	// next free register
	public int nfr;
	
	public CodeGenerator(Parser parser) {
		file = new File("tmp.s");
		try {
			fileOut = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		nfr = 0;
		t = new int[10];
		this.parser = parser;
	}
	
	public void put(String op, String arg1, String arg2, String arg3) {
		if(!parser.isError()) {
			String line = op + " " + arg1 + " " + arg2 + " " + arg3 + "\n";
			
			if(pc >= CODESIZE) {
				// TODO: ERROR HANDLING. THIS MUST NEVER HAPPEN!!!!!
			}
			code[pc] = line;
			System.out.println("code[" + pc + "] = " + line);
			pc++;
		}
	}
	
	public void put(String op, String arg1, String arg2) {
		if(!parser.isError()) {
			String line = op + " " + arg1 + " " + arg2 + "\n";
			
			if(pc >= CODESIZE) {
				// TODO: ERROR HANDLING. THIS MUST NEVER HAPPEN!!!!!
			}
			code[pc] = line;
			System.out.println("code[" + pc + "] = " + line);
			pc++;
		}
	}
	
	public void put(String op, String arg1) {
		if(!parser.isError()) {
			String line = op + " " + arg1 + "\n";
			
			if(pc >= CODESIZE) {
				// TODO: ERROR HANDLING. THIS MUST NEVER HAPPEN!!!!!
			}
			code[pc] = line;
			System.out.println("code[" + pc + "] = " + line);
			pc++;
		}
	}
	
	public String loadImmediately(int arg) {
		if(!parser.isError()) {
			if(nfr > t.length) {
				// TODO: Push other register on the stack.
			}
			t[nfr] = arg;
			String reg = "$t" + nfr;
			put("li", reg, new Integer(arg).toString());
			nfr++;
			return reg;
		}
		return null;
	}
	
	public String load(int address) {
		if(!parser.isError()) {
			if(nfr > t.length) {
				// TODO: Push other register on the stack.
			}
			t[nfr] = address;
			String reg = "$t" + nfr;
			put("lw", reg, new Integer(address).toString());
			nfr++;
			return reg;
		}
		return null;
	}
	
	public void pushOnStack(int size) {
		String register = loadImmediately(0);
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
	
	public void setSp(int offset) {
		String target = new Integer(offset).toString() + "($sp)";
		put("subu", "$sp", target);
		sp = sp + offset;
	}
	
	public void fixUp(int pc, int offset) {
		String line2fix = code[pc];
		String msg = "fixed line " + "[" + pc + "] : " + line2fix + ": ";
		code[pc] = line2fix.replace("offset", new Integer(offset).toString());
		msg = msg + code[pc];
		System.out.println(msg);
	}
}
