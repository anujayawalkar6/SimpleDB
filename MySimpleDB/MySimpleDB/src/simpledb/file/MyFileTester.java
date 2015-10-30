package simpledb.file;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import simpledb.server.SimpleDB;

public class MyFileTester {

	public static void main(String[] args) 
	{
		 String dirToCreateDB="akondDB"; 
		 SimpleDB.init(dirToCreateDB);
		 
		 int intOffset=99; 		 
		 int strOffset=20;
		 int dateOffset=1;
		 int byteAOffset =2 ;
		 int boolOffset = 3 ;
		 int shortOffset =  4 ;
		 
		 
		 Page tempPage1 = new Page();
		 Page tempPage2 = new Page();		 
		 
		 Block tempBlock = new Block("MyTempBlock", 6);		 
		 
		 /*
		  * 
		  * Testing String 
		  * 
		  * 
		  * 
		  * */
		 tempPage1.setString(strOffset, "Akond");
		 tempBlock = tempPage1.append("Rahman");

		 tempPage2.read(tempBlock);
		 String stringToRet = tempPage2.getString(strOffset);
		 
		 System.out.println("String to return ... " +  stringToRet );
		 
		 /*
		  * 
		  * Testing Int 
		  * 
		  * 
		  * */
		 
		 tempPage1.setInt(intOffset, 99);
		 int n = tempPage1.getInt(intOffset);
		 System.out.println("Value of n=" + n);
		 tempPage1.setInt( n+1, 792);
		 tempPage1.write(tempBlock);
		 int newN = tempPage1.getInt(n+1);
		 System.out.println("Value @ 'n+1'=" + newN);		 
		 
		 
		 /*
		  * 
		  * 
		  * Testing Date 
		  * 
		  * */
		 
		 String dateStr="19870819";
		 String theFormat="yyyyMMdd";
	     DateFormat   formatter = new SimpleDateFormat(theFormat);
	     Date testDateObj = new Date();
		 try 
		 {
			testDateObj = (Date) formatter.parse(dateStr);
		 } 
		 catch (ParseException e) 
		 {

			e.printStackTrace();
		 }

		 tempPage1.setDate(dateOffset, testDateObj);
		 System.out.println("The date was: " +	tempPage1.getDate(dateOffset));
		 
		 
		 /*
		  * 
		  * 
		  * Testing Byte Array 
		  * 
		  * 
		  * */
		 
		 //byte[] byteArrayExample = new byte[]{3,2,5,4,1};	
		 byte [] byteArrayExample = "Akond".getBytes();
		 String strToRet="" ;

		 tempPage1.setBytArray(byteAOffset, byteArrayExample);
		 strToRet = new String(tempPage1.getByteArray(byteAOffset));		 
		 System.out.println("Byte array cotents as string=" + strToRet );
		 
		 /*
		  * 
		  * testing boolean 
		  * 
		  * 
		  * */
		 boolean boolToInsert = true ;
		 System.out.println("bools=" +  boolToInsert);
		 tempPage1.setBoolean(boolOffset, boolToInsert);
		 System.out.println("boolean returned = " +	tempPage1.getBoolean(boolOffset));
		 
		 /*
		  * 
		  * testing short data type 
		  * 
		  * */
		 
		 short shortValToRet = 21474 ; 
		 tempPage1.setShort(shortOffset, shortValToRet);
		 System.out.println("short value to return = " + tempPage1.getShort(shortOffset));
		 

	}

}
