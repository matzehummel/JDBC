package dhbw.flight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CmdClear implements CmdIf {
	
	@Override
	public void printHelp() {
		System.out.println("  clear <airline> <number> <date>");
	}

	@Override
	public void execute(Scanner sc) {
		
		String airline = sc.next();
		int number = Integer.parseInt(sc.next());
		String date = sc.next();
		
		System.out.println("Clearing " + airline + " " + number + " " + date);
		
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbUtils.getMysqlDataSource().getConnection();
			
			String queryIdFlight = "SELECT F.idFlight FROM flights F, passengers P " +
					"WHERE F.idFlight = P.idFlight " +
					"AND F.airline = ? AND F.number = ? AND F.date = ? ";
			prepStmt = conn.prepareStatement(queryIdFlight);
			prepStmt.setString(1, airline);
			prepStmt.setInt(2, number);
			prepStmt.setString(3, date);
			rs = prepStmt.executeQuery();
			
			rs.next();
			int idFlight = rs.getInt(1);
			
			rs.close(); rs = null;
			prepStmt.close(); prepStmt = null;
			
			String deleteBookingsFromFlight = "DELETE FROM passengers WHERE idFlight = ?";
			prepStmt = conn.prepareStatement(deleteBookingsFromFlight);
			prepStmt.setInt(1, idFlight);
			prepStmt.executeUpdate();
			prepStmt.close(); prepStmt = null;
			System.out.println("Flight cleared");
			
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
