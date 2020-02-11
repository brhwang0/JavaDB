import java.util.*;
import java.sql.*;

public class JavaDB {
	
	public static void main(String[] args) {
		
		Scanner s = new Scanner(System.in);
		System.out.print("Please enter the host URL or IP address: ");
		String host = s.nextLine();
		System.out.print("Please enter username: ");
		String username = s.nextLine();
		System.out.print("Please enter password: ");
		String password = s.nextLine();
		String hostName = "jdbc:mysql://" + host + ":3306/";
		
		try {
			Connection con = DriverManager.getConnection(hostName, username, password); 
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("show databases;");
			/*
			while (rs.next()) {
				System.out.println(rs.getString(1) +"  " + rs.getString(2));
			}
			*/
			// Displaying and choosing databases
			while (rs.next()) {
				System.out.println("Database: " + rs.getString("Database"));
			}
			System.out.print("Please choose a database: ");
			String dbname = s.nextLine();
			stmt.executeQuery("use " + dbname);
			
			// Displaying and choosing tables
			rs = stmt.executeQuery("show tables;");
			while (rs.next()) {
				System.out.println("Tables: " + rs.getString("Tables_in_" + dbname));
			}
			System.out.print("Please choose a table: ");
			String tablename = s.nextLine();
			rs = stmt.executeQuery("select * from " + tablename);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			
			// Print table
			while (rs.next()) {
			}
			
			con.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
	}
}