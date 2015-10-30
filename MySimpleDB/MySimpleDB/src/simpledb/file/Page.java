package simpledb.file;

import simpledb.server.SimpleDB;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The contents of a disk block in memory.
 * A page is treated as an array of BLOCK_SIZE bytes.
 * There are methods to get/set values into this array,
 * and to read/write the contents of this array to a disk block.
 * 
 * For an example of how to use Page and 
 * {@link Block} objects, 
 * consider the following code fragment.  
 * The first portion increments the integer at offset 792 of block 6 of file junk.  
 * The second portion stores the string "hello" at offset 20 of a page, 
 * and then appends it to a new block of the file.  
 * It then reads that block into another page 
 * and extracts the value "hello" into variable s.
 * <pre>
 * Page p1 = new Page();
 * Block blk = new Block("junk", 6);
 * p1.read(blk);
 * int n = p1.getInt(792);
 * p1.setInt(792, n+1);
 * p1.write(blk);
 *
 * Page p2 = new Page();
 * p2.setString(20, "hello");
 * blk = p2.append("junk");
 * Page p3 = new Page();
 * p3.read(blk);
 * String s = p3.getString(20);
 * </pre>
 * @author Edward Sciore
 */
public class Page {
   /**
    * The number of bytes in a block.
    * This value is set unreasonably low, so that it is easier
    * to create and test databases having a lot of blocks.
    * A more realistic value would be 4K.
    */
   public static final int BLOCK_SIZE = 400;
   
   /**
    * The size of an integer in bytes.
    * This value is almost certainly 4, but it is
    * a good idea to encode this value as a constant. 
    */
   public static final int INT_SIZE = Integer.SIZE / Byte.SIZE;
   
   /**
    * The maximum size, in bytes, of a string of length n.
    * A string is represented as the encoding of its characters,
    * preceded by an integer denoting the number of bytes in this encoding.
    * If the JVM uses the US-ASCII encoding, then each char
    * is stored in one byte, so a string of n characters
    * has a size of 4+n bytes.
    * @param n the size of the string
    * @return the maximum number of bytes required to store a string of size n
    */
   public static final int STR_SIZE(int n) {
      float bytesPerChar = Charset.defaultCharset().newEncoder().maxBytesPerChar();
      return INT_SIZE + (n * (int)bytesPerChar);
   }
   
   private ByteBuffer contents = ByteBuffer.allocateDirect(BLOCK_SIZE);
   private FileMgr filemgr = SimpleDB.fileMgr();
   
   /**
    * Creates a new page.  Although the constructor takes no arguments,
    * it depends on a {@link FileMgr} object that it gets from the
    * method {@link simpledb.server.SimpleDB#fileMgr()}.
    * That object is created during system initialization.
    * Thus this constructor cannot be called until either
    * {@link simpledb.server.SimpleDB#init(String)} or
    * {@link simpledb.server.SimpleDB#initFileMgr(String)} or
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * {@link simpledb.server.SimpleDB#initFileLogAndBufferMgr(String)}
    * is called first.
    */
   
   
   /**
    * 
    * 
    * Akond, April 19, 2015
    * 
    * 
    * **/
   /*
    * 
    * The format using which date will be stored. 
    * 
    * */
  String theFormat="yyyyMMdd";
  SimpleDateFormat dateFormatObj = new SimpleDateFormat(theFormat);
   
   public Page() {}
   
   /**
    * Populates the page with the contents of the specified disk block. 
    * @param blk a reference to a disk block
    */
   public synchronized void read(Block blk) {
      filemgr.read(blk, contents);
   }
   
   /**
    * Writes the contents of the page to the specified disk block.
    * @param blk a reference to a disk block
    */
   public synchronized void write(Block blk) {
      filemgr.write(blk, contents);
   }
   
   /**
    * Appends the contents of the page to the specified file.
    * @param filename the name of the file
    * @return the reference to the newly-created disk block
    */
   public synchronized Block append(String filename) {
      return filemgr.append(filename, contents);
   }
   
   /**
    * Returns the integer value at a specified offset of the page.
    * If an integer was not stored at that location, 
    * the behavior of the method is unpredictable.
    * @param offset the byte offset within the page
    * @return the integer value at that offset
    */
   public synchronized int getInt(int offset) {
      contents.position(offset);
      return contents.getInt();
   }
   
   /**
    * Writes an integer to the specified offset on the page.
    * @param offset the byte offset within the page
    * @param val the integer to be written to the page
    */
   public synchronized void setInt(int offset, int val) {
      contents.position(offset);
      contents.putInt(val);
   }
   
   /**
    * Returns the string value at the specified offset of the page.
    * If a string was not stored at that location,
    * the behavior of the method is unpredictable.
    * @param offset the byte offset within the page
    * @return the string value at that offset
    */
   public synchronized String getString(int offset) {
      contents.position(offset);
      int len = contents.getInt();
      byte[] byteval = new byte[len];
      contents.get(byteval);
      return new String(byteval);
   }
   
   /**
    * Writes a string to the specified offset on the page.
    * @param offset the byte offset within the page
    * @param val the string to be written to the page
    */
   public synchronized void setString(int offset, String val) {
      contents.position(offset);
      byte[] byteval = val.getBytes();
      contents.putInt(byteval.length);
      contents.put(byteval);
   }
   
   /*
    * 
    * 
    * 
    * Akond 9:30 PM, April 18, 2015 
    * 
    * 
    * */
   
   
   /*
    * 
    * Date handling 
    * 
    * */   
   
   /*
   public synchronized void setDate(int offset, Date val) 
   {
	      contents.position(offset);
	      String dateStr = "" ;
	      dateStr = val.getStringPart();
	      byte[] byteval = dateStr.getBytes();
	      contents.putInt(byteval.length);
	      contents.put(byteval);
   }  
   
   public synchronized Date getDate(int offset) 
   {

	      contents.position(offset);
	      int len = contents.getInt();
	      byte[] byteval = new byte[len];
	      contents.get(byteval);
	      String dateStringToSend = new String(byteval);
	   	  Date returnDateParam = new Date(dateStringToSend); 
	   	  return returnDateParam ;
	}
	
	*/ 
   
   /*
    * 
    * Setting date here: byte value is converted to string and then to date  
    * 
    * */
   public synchronized void setDate(int offset, Date val) 
   {
	      contents.position(offset);
	      String dateStr = convertDateToStr(val) ;
	      byte[] byteval = dateStr.getBytes();
	      contents.putInt(byteval.length);
	      contents.put(byteval);
   } 
   
   /*
    * 
    * Getting date here: byte value is converted to string and then to date 
    * */
   public synchronized Date getDate(int offset) 
   {
	      contents.position(offset);
	      int len = contents.getInt();
	      byte[] byteval = new byte[len];
	      contents.get(byteval);
	      String dateStringToSend = new String(byteval);
	   	  Date returnDateParam = convertStringToDate(dateStringToSend); 
	   	  return returnDateParam ;
	}   
   
   
   /*
    * 
    * Setting byte array  here 
    * 
    * */   
  
   public synchronized void setBytArray(int offset, byte[] byteArrayParam) 
   {
	      contents.position(offset);
	      contents.putInt(byteArrayParam.length);
	      contents.put(byteArrayParam);
   }     
   
   /*
    * 
    * Getting byte array  here 
    * 
    * */ 
   public synchronized byte[] getByteArray(int offset) 
   {

	      contents.position(offset);
	      int len = contents.getInt();
	      byte[] byteval = new byte[len];
	      contents.get(byteval);
	   	  return byteval ;
	}
   
   /*
    *  Setting Boolean 
    * The string 'true' or 'false' is stored here  
    * 
    * */
   
   public synchronized void setBoolean(int offset, boolean boolParam) 
   {
	      String strToStore="";
	      contents.position(offset);
	      if(boolParam)
	      {
	    	  strToStore = "true" ;
	      }
	      else
	      {
	    	  strToStore = "false" ;
	      }
	      byte[] byteval = strToStore.getBytes();
	      contents.putInt(byteval.length);
	      contents.put(byteval);
   }     
   /*
    *  Getting Boolean 
    * The string 'true' or 'false' is retrieved  here and the the corresponsing boolean vale is returned  
    * 
    * */
   public synchronized boolean getBoolean(int offset) 
   {
	      boolean boolToret; 
	      contents.position(offset);
	      int len = contents.getInt();
	      byte[] byteval = new byte[len];
	      contents.get(byteval);
	      String strToUse= new String(byteval);
	      if(strToUse.equals("true"))
	      {
	    	  boolToret = true ;
	      }
	      else
	      {
	    	  boolToret = false; 
	      }
	      
	      return boolToret ;

	 }
   
     /*
      * 
      * Short data type handling 
      * Setting short data type. Short value is converted to byte array and stored 
      * 
      * */
   
   public synchronized void setShort(int offset, short shortParam) 
   {
	      contents.position(offset);

	      byte[] byteval = convertShrotValToByteArray(shortParam);
	      contents.putInt(byteval.length);
	      contents.put(byteval);
   }    
   /*
    * 
    * Short data type handling 
    * Getting short data type. Short value is converted from  byte array that was stored previously 
    * 
    * */
   public synchronized short getShort(int offset) 
   {
	      Short shortValToret  ;
	      contents.position(offset);
	      int len = contents.getInt();
	      byte[] byteval = new byte[len];
	      contents.get(byteval);
	      shortValToret = convertByteArrayToShort(byteval); 
	      return shortValToret ;
	}   
   
   
     /*
      * 
      * Short to byte array converter 
      * 
      * */
   
     private byte[] convertShrotValToByteArray(short value) {
	    
    	int byteLen = 2 ;  
	    byte[] bytesToret = new byte[byteLen];
	    ByteBuffer bufferInUse = ByteBuffer.allocate(bytesToret.length);
	    bufferInUse.putShort(value);
	    return bufferInUse.array();
	}
     
     /*
      * 
      * Convert byte array to short 
      * 
      * */

     private  short convertByteArrayToShort(byte[] array) 
     {
    	    ByteBuffer bufferToRet = ByteBuffer.wrap(array);
    	    return bufferToRet.getShort();
     }

     /*
      * 
      * Convert Date to String 
      * 
      * */
     
     private String convertDateToStr(Date dateParam)
     {

    	 String strToret ="" ;
    	 strToret = dateFormatObj.format(dateParam);
    	 return strToret ;
     } 
     
     /*
      * 
      * String to date converter 
      * */
     
     private Date convertStringToDate(String strParam)
     {
    	  Date dateObjToRet = new Date();
    	  
		  try 
		  {
			dateObjToRet = dateFormatObj.parse(strParam);

		  } 
		  catch (ParseException e) 
		  {
			  System.err.println("Error log " + e.getMessage());
			  e.printStackTrace();
		  }
	      return dateObjToRet ;

     }     
     
}
