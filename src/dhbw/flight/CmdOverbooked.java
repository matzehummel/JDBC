
package dhbw.flight;

import java.sql.*;
import java.util.Scanner;


public class CmdOverbooked implements CmdIf
{
	@Override
	public void printHelp() {
		System.out.println("  overbooked - Print all overbooked flights");
	}

	@Override
	public void execute(Scanner sc) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbUtils.getMysqlDataSource().getConnection();
			stmt = conn.createStatement();
			
			String sqlQueryOverbooked = "SELECT F.airline, F.number, F.date, COUNT(*) AS cnt, F.capacity AS cap" +
					" FROM passengers AS P, flights AS F" +
					" WHERE P.idFlight = F.idFlight" +
					" GROUP BY P.idFlight" +
					" HAVING cnt > cap";
			rs = stmt.executeQuery(sqlQueryOverbooked);
			DbUtils.printResultSet(rs);
		} catch(SQLException e) {
			System.out.println("SQL-Exception: " + e.getMessage());
			System.out.println("SQL State: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			//e.printStackTrace();
		} finally {
			try {
				if(rs != null) { rs.close(); } rs = null;
				if(stmt != null) { stmt.close(); } stmt = null;
				if(conn != null) { conn.close(); } conn = null;
			} catch(SQLException e) { }
		}
	}
}
