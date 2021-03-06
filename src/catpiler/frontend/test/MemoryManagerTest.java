package catpiler.frontend.test;

import junit.framework.Assert;

import org.junit.Test;

import catpiler.backend.codegeneration.CodeGenerator;
import catpiler.backend.codegeneration.MemoryManager;

public class MemoryManagerTest {

	@Test
	public void testInitHeap() {
		CodeGenerator codeGenerator = new CodeGenerator(null);
		MemoryManager memoryManager = new MemoryManager(codeGenerator);
		
		memoryManager.init();
	}
	
	@Test
	public void testAllocateMemory() {
		CodeGenerator codeGenerator = new CodeGenerator(null);
		MemoryManager memoryManager = new MemoryManager(codeGenerator);
		
		memoryManager.init();
		int memstart1 = memoryManager.allocateMemory(127);
		Assert.assertEquals(0, memstart1);
		int memblocks = 0;
		if(127 % memoryManager.BLOCKSIZE == 0) {
			memblocks = 127 / memoryManager.BLOCKSIZE;
		} else {
			memblocks = (127 / memoryManager.BLOCKSIZE) + 1;
		}
		int memstart2 = memoryManager.allocateMemory(257);
		Assert.assertEquals(memblocks, memstart2);
		if(257 % memoryManager.BLOCKSIZE == 0) {
			memblocks = memblocks + (257 / memoryManager.BLOCKSIZE);
		} else {
			memblocks = memblocks + (257 / memoryManager.BLOCKSIZE + 1);
		}
		int memstart3 = memoryManager.allocateMemory(50);
		Assert.assertEquals(memblocks, memstart3);
		if(50 % memoryManager.BLOCKSIZE == 0) {
			memblocks = memblocks + (50 / memoryManager.BLOCKSIZE);
		} else {
			memblocks = memblocks + (50 / memoryManager.BLOCKSIZE + 1);
		}
		memoryManager.decreaseReferenceCount(1, 2);
		int memstart4 = memoryManager.allocateMemory(50);
		Assert.assertEquals(1, memstart4);
	}
}
