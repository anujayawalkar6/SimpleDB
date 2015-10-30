package simpledb.buffer;

import java.util.HashMap;
import java.util.Map;

import simpledb.file.*;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
class BasicBufferMgr 
{
   private Buffer[] bufferpool;
   private int numAvailable;
   
   /*
    * Change #
    * Mapping created .... 
    * This mapping will be used to allocate, un-allocate, and find an existing mapping between blocks and buffer 
    * 
    * */
   Map<Block, Buffer> bufferPoolMap = new HashMap<Block, Buffer>() ;   
   


   
   /**
    * Creates a buffer manager having the specified number 
    * of buffer slots.
    * This constructor depends on both the {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} objects 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * Those objects are created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    * @param numbuffs the number of buffer slots to allocate
    */
   BasicBufferMgr(int numbuffs) {
      bufferpool = new Buffer[numbuffs];
      numAvailable = numbuffs;
      for (int i=0; i<numbuffs; i++)
      {
          bufferpool[i] = new Buffer();    	  
      }
      
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   synchronized void flushAll(int txnum) {
      for (Buffer buff : bufferpool)
         if (buff.isModifiedBy(txnum))
         buff.flush();
   }
   
   /**
    * Pins a buffer to the specified block. 
    * If there is already a buffer assigned to that block
    * then that buffer is used;  
    * otherwise, an unpinned buffer from the pool is chosen.
    * Returns a null value if there are no available buffers.
    * @param blk a reference to a disk block
    * @return the pinned buffer
    */
   synchronized Buffer pin(Block blk) 
   {
      Buffer buff = findExistingBuffer(blk);
      if (buff == null) 
      {

         buff = chooseUnpinnedBuffer();
         if (buff == null)
         {
             return null;        	 
         }

         buff.assignToBlock(blk);
         /*
          * Change #
          * creating the mapping here 
          * 
          * */
         assignBuffToblock(blk, buff);


      }
      else 
      {
    	  /*
    	   * Change #
    	   * Just keeping track of when the buffer is obtained from the mapping via console message  
    	   * 
    	   * */
    	 // System.out.println("a buffer was obtained from the mapping");
    	  
      }
      if (!buff.isPinned())
      {
          numAvailable--;    	  
      }

      buff.pin();

      return buff;
   }
   
   /**
    * Allocates a new block in the specified file, and
    * pins a buffer to it. 
    * Returns null (without allocating the block) if 
    * there are no available buffers.
    * @param filename the name of the file
    * @param fmtr a pageformatter object, used to format the new block
    * @return the pinned buffer
    */
   synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
      Buffer buff = chooseUnpinnedBuffer();
      if (buff == null)
         return null;
      buff.assignToNew(filename, fmtr);
      /*
       * Change #
       * assigning the appropriate  block to a buffer 
       * 
       * 
       * */
      assignBuffToblock(buff.block(), buff);
      numAvailable--;
      buff.pin();
      return buff;
   }
   
   /**
    * Unpins the specified buffer.
    * @param buff the buffer to be unpinned
    */
   synchronized void unpin(Buffer buff) 
   {
	  Block blockObj =  buff.block(); 
      buff.unpin();
      if (!buff.isPinned())
      {
    	  /*
    	   * Change #
    	   * First checking if there is an exiting mapping 
    	   * */
    	 if(containsMapping(blockObj))
    	  {
    		  /*
    		   * Change #
    		   * if there is a mapping then un-allocate the buffer 
    		   * 
    		   * */
    		  unassignBlockToBuff( buff);
    	 }
          numAvailable++;    	  
      }

   }
   
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   int available() {
      return numAvailable;
   }
   
   private Buffer findExistingBuffer(Block blk) 
   {
	   /*
	    * Change #
	    * The variable to return the buffer 
	    * */
	   Buffer buffToret = null ;
	   /*
	    * Change #
	    * Sequential accessing  will not work any more 
	    * 
	    * */
	   
	  /*
      for (Buffer buff : bufferpool) 
      {
    	  
         Block b = buff.block();
         if (b != null && b.equals(blk))
         {
             return buff;        	 
         }
      }
      return null; 
	   */
	   /*
	    * Change #
	    * Looking up the map the find an existing buffer 
	    * 
	    * */
         if(containsMapping(blk))
         {
        	 buffToret =  getMapping(blk);
         }
    	  
         
         return buffToret ;
   }
   
   private Buffer chooseUnpinnedBuffer() {
      for (Buffer buff : bufferpool)
         if (!buff.isPinned())
         return buff;
      return null;
   }
   
   public boolean containsMapping(Block blk)
   {
	   return bufferPoolMap.containsKey(blk);
	   
   }
   
   public Buffer getMapping (Block blk)
   {
	   //System.out.println("returning the block ..");
	   return bufferPoolMap.get(blk);
   } 
   /*
    * Change #
    * We created this method to assign a buffer to a block from the map created 
    * 
    * */
   private void assignBuffToblock( Block blockParam, Buffer bufferParam) 
   {
	   
	   bufferPoolMap.put( blockParam, bufferParam) ;
   }
   
   /*
    * Change #
    * We created this method to un-map a block for a buffer 
    * 
    * if the object is a buffer , then we get the corresponding block, and then remove the block 
    * 
    * */
   
   
   private void unassignBlockToBuff(Buffer bufferParam )
   {

	   
	   bufferPoolMap.remove(bufferParam.block());
   }
   
   
}
