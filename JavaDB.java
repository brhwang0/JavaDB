import java.util.*;
import java.sql.*;
import javax.swing.*;

public class JavaDB {
	
	static String connectionURL;
	static String hostName;
	static String username;
	static String password;
	
	public static void setup() {
		
		String connectionURL;
		
		JTextField inputHost = new JTextField();
		JTextField inputUser = new JTextField();
		JTextField inputPass = new JPasswordField();
		Object[] message = {
			"Host IP: ", inputHost,
			"Username:", inputUser,
			"Password:", inputPass
		};

		int option = JOptionPane.showConfirmDialog(null, message, "Connection Setup", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			hostName = "jdbc:mysql://" + inputHost.getText() + ":3306/";
			username = inputUser.getText(); password = inputPass.getText();
		}
		else {
			System.out.println("Connection setup cancelled.");
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		setup();
		Scanner s = new Scanner(System.in);
		
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