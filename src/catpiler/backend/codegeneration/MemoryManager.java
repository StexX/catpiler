package catpiler.backend.codegeneration;

import catpiler.utils.ErrorReporter;

public class MemoryManager {

	public final int HEAPBLOCKS = 100;
	public final int BLOCKSIZE = 256;
	private String[] addr;
	private int[] refc;
	
	private CodeGenerator codeGenerator;
	
	public MemoryManager(CodeGenerator codeGenerator) {
		this.codeGenerator = codeGenerator;
		init();
	}
	
	/* this method creates the very first code that
	 * is going to be executed. We therefore assume, 
	 * that no register is taken */
	public void init() {
		addr = new String[HEAPBLOCKS];
		refc = new int[HEAPBLOCKS];
		
		for(int i=0; i<HEAPBLOCKS; i++) {
			codeGenerator.put("ADDI", "$v0", "$r0", "9");
			codeGenerator.put("ADDI", "$a0", "$r0", new Integer(BLOCKSIZE).toString());
			codeGenerator.put("syscall");
			
			String label = "hp" + new Integer(i).toString();
			codeGenerator.storeData(label, ".space", "4");
			codeGenerator.put("la", "$t0", label);
			// indirect addressing
			codeGenerator.put("sw", "$v0", "$t0");
			
			addr[i] = label;
			refc[i] = 0;
		}
	}
	
	/**
	 * Memory allocation: worst case complexity is O(n)
	 * @param size
	 * @return returns the number of block that points 
	 * 			to the beginning of the allocated memory.
	 * 			The memory can be accessed by loading the address
	 * 			of the label "hp<sub>i</sub>", where i = the return value
	 */
	public int allocateMemory(int size) {
		// figure out how many blocks we need
		int blockAmount = 0;
		if(size % BLOCKSIZE != 0) {
			blockAmount = ((int) (size / BLOCKSIZE)) +1;
		} else {
			blockAmount = (int)( size / BLOCKSIZE);
		}
		int i=0;
		while(i < HEAPBLOCKS) {
			int j=0;
			for(j=0; j<blockAmount; j++) {
				if(refc[i+j] != 0) {
					break;
				}
			}
			if(j >= blockAmount) {
				break;
			} else {
				i = i+j+1;
			}
		}
		for(int j=i; j<(blockAmount+i); j++) {
			refc[j] = 1;
		}
		
		System.out.print("Reference map: ");
		for(int k=0; k<HEAPBLOCKS; k++) {
			System.out.print(new Integer(refc[k]).toString() + " ");
		}
		System.out.print("\n");
		
		if(i < HEAPBLOCKS)
			return i;
		else
			return -1;
	}
	
	public void increaseReferenceCount(int start, int size) {
		for(int i=start; i<(start+size); i++) {
			refc[i] = refc[i] + 1;
		}
		
		System.out.print("Reference map: ");
		for(int k=0; k<HEAPBLOCKS; k++) {
			System.out.print(new Integer(refc[k]).toString() + " ");
		}
		System.out.print("\n");
	}
	
	public void freeMemory(String start, int size) {
		int start_int = new Integer(start.substring(2));
		freeMemory(start_int, size);
	}
	
	public void freeMemory(int start, int size) {
		for(int i=start; i<(start+size); i++) {
			refc[i] = 0;
		}
	}
	
	public void decreaseReferenceCount(String start, int size) {
		int start_int = new Integer(start.substring(2));
		decreaseReferenceCount(start_int, size);
	}
	
	public void decreaseReferenceCount(int start, int size) {
		for(int i=start; i<(start+size); i++) {
			if(refc[i] == 0) {
				// TODO: error in reference holding
//				System.err.println("Found an error in memory reference management - memory was already de-allocated");
				ErrorReporter.markError("Memory Reference Management problem: Memory was already de-allocated");
			}
			refc[i] = refc[i] - 1;
		}
		
		System.out.print("Reference map: ");
		for(int k=0; k<HEAPBLOCKS; k++) {
			System.out.print(new Integer(refc[k]).toString() + " ");
		}
		System.out.print("\n");
	}
}
