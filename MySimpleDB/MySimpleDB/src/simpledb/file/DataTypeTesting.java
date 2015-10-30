package simpledb.file;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class DataTypeTesting 
{



	
	
	
	@Test
	public void testDate() 
	{
		String dateStr="19870819";
		String theFormat="yyyyMMdd";
	    DateFormat   formatter = new SimpleDateFormat(theFormat);
	    Date testDateObj = new Date() ;
	    Page testPage = new Page();
	    int offset =100 ;
		try 
		{
			testDateObj = (Date) formatter.parse(dateStr);
		} 
		catch (ParseException e) 
		{

			e.printStackTrace();
		}	
		testPage.setDate(offset, testDateObj);
		assertEquals("Date must be equal", testDateObj , testPage.getDate(offset) );
	}
	
	

}
