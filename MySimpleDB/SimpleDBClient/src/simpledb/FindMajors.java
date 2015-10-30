package simpledb;
import java.sql.*;
import simpledb.remote.SimpleDriver;

public class FindMajors {
    public static void main(String[] args) {
		//String major = args[0];
    	String major = "drama";
    	float diff = 0; 
    	System.out.println("Here are the " + major + " majors");
		System.out.println("Name\tGradYear");

		Connection conn = null;
		ResultSet rs = null; 
		Statement stmt = null; 
		try {
			// Step 1: connect to database server
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);

			String qry = "select DName "
			           + "from DEPT "
			           //+ "where did = majorid "
			           //+ "and dname = '" + major + "'"
			           ;				 			
			stmt = conn.createStatement();
			long start = System.currentTimeMillis() ;

			for(int i =0; i < 1000; i++)
			{
				// Step 2: execute the query
				 
				 rs = stmt.executeQuery(qry);	
				 
			}
			long end = System.currentTimeMillis() ;

			diff = end -start ;
			System.out.println("Difference is "+ diff/1000 + " seconds");
					// Step 3: loop through the result set
					
					rs.close();
					//stmt.close();
			



		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("exception " + e.getMessage() );
		}
		finally {
			// Step 4: close the connection
			try {
				if (conn != null)
					conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
