
package dhbw.flight;

import java.sql.*;
import java.util.Scanner;



public class CmdFind implements CmdIf
{
	@Override
	public void printHelp() {
		System.out.println("  find <search-string> - Find airlines and airports");
	}

	@Override
	public void execute(Scanner sc) {
		String search = sc.next();
		System.out.println("Looking for: " + search);
		
		search = '%'+search+'%';
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbUtils.getMysqlDataSource().getConnection();
			
			String sqlQueryAirports = "SELECT name FROM airports WHERE name LIKE ?";
			prepStmt = conn.prepareStatement(sqlQueryAirports);
			
			System.out.println("searching in airports...");
			prepStmt.setString(1, search);
			rs = prepStmt.executeQuery();
			System.out.println("Founded airports:");
			DbUtils.printResultSet(rs);
			prepStmt.close(); prepStmt = null;
			rs.close(); rs = null;
			
			System.out.println();
			
			String sqlQueryAirlines = "SELECT name FROM airlines WHERE name LIKE ?";
			prepStmt = conn.prepareStatement(sqlQueryAirlines);
			
			System.out.println("searching in airlines...");
			prepStmt.setString(1, search);
			rs = prepStmt.executeQuery();
			System.out.println("Founded airlines:");
			DbUtils.printResultSet(rs);
		
		} catch(SQLException e) {
			System.out.println("SQL-Exception: " + e.getMessage());
			System.out.println("SQL State: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			//e.printStackTrace();
		} finally {
			try {
				if(rs != null) { rs.close(); } rs = null;
				if(prepStmt != null) { prepStmt.close(); } prepStmt = null;
				if(conn != null) { conn.close(); } conn = null;
			} catch(SQLException e) { }
		}
	}
}
