
package dhbw.flight;

import java.sql.*;
import javax.sql.DataSource;
import com.mysql.cj.jdbc.MysqlDataSource;


public class DbUtils
{
	static String serverName = "dbapi.frank-may.de";
	static String user = "Matthias";
	static String password = "raspberry04";
	
	public static DataSource getMysqlDataSource() {
		MysqlDataSource ds = new MysqlDataSource();
	    ds.setServerName(serverName);
	    ds.setDatabaseName(user);
	    ds.setUser(user);
	    ds.setPassword(password);
		return ds;
	}
	
	public static void printResultSet(ResultSet resultSet)
		throws SQLException
	{
		ResultSetMetaData meta = resultSet.getMetaData();
		int nCols = meta.getColumnCount();
		
		while(resultSet.next()) {
			System.out.print("> ");
			for(int i = 1; i <= nCols; i++) {
				if(i > 1) { System.out.print(", "); }
				System.out.print(resultSet.getString(i));
			}
			System.out.println();
		}
	}
}
