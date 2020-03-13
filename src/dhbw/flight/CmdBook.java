package dhbw.flight;

import java.sql.*;
import java.util.Scanner;

public class CmdBook implements CmdIf
{
	@Override
	public void printHelp() {
		System.out.println("  book <name> <airline> <number> <date>");
	}

	@Override
	public void execute(Scanner sc) {
		
		String name = sc.next();
		String airline = sc.next();
		int number = Integer.parseInt(sc.next());
		String date = sc.next();
		
		System.out.println("Booking " + name + " on " + airline + " " + number + " " + date);
		
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbUtils.getMysqlDataSource().getConnection();
			conn.setAutoCommit(false);
			
			String queryIdFlight = "SELECT F.idFlight, F.capacity, COUNT(*) FROM flights F, passengers P " +
					"WHERE F.idFlight = P.idFlight " +
					"AND F.airline = ? AND F.number = ? AND F.date = ? " +
					"FOR UPDATE";
			prepStmt = conn.prepareStatement(queryIdFlight);
			prepStmt.setString(1, airline);
			prepStmt.setInt(2, number);
			prepStmt.setString(3, date);
			rs = prepStmt.executeQuery();
			
			
			rs.next();
			
			int idFlight = rs.getInt(1);
			int capacity = rs.getInt(2);
			int bookedSeats = rs.getInt(3);
			
			if(bookedSeats > capacity) {
				System.out.println("Flight is overbooked");
				return;
			} else if(bookedSeats == capacity) {
				System.out.println("Flight is full");
				return;
			}
			rs.close(); rs = null;
			prepStmt.close(); prepStmt = null;
			
			String bookPassenger = "INSERT INTO passengers SET name = ?, idFlight = ?";
			
			prepStmt = conn.prepareStatement(bookPassenger);
			prepStmt.setString(1, name);
			prepStmt.setInt(2, idFlight);
			prepStmt.executeUpdate();
			
			conn.commit();
			
			System.out.println("booked");
			
		} catch(SQLException e) {
			System.out.println("SQL-Exception: " + e.getMessage());
			System.out.println("SQL State: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			//e.printStackTrace();
			if(conn != null) {
				try { conn.rollback(); } catch(SQLException e2) { }
			}
		} finally {
			try {
				if(rs != null) { rs.close(); } rs = null;
				if(prepStmt != null) { prepStmt.close(); } prepStmt = null;
				if(conn != null) {
					conn.setAutoCommit(true);
					conn.close();
				}
				conn = null;
			} catch(SQLException e) { }
		}
	}

}
