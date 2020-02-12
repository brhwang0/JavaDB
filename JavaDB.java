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
		inputHost.addAncestorListener(new RequestFocusListener());
		JTextField inputPort = new JTextField();
		JTextField inputUser = new JTextField();
		JTextField inputPass = new JPasswordField();
		Object[] message = {
			"Host IP: ", inputHost,
			"Port: ", inputPort,
			"Username:", inputUser,
			"Password:", inputPass
		};
		
		int option = JOptionPane.showConfirmDialog(null, message, "Connection Setup", JOptionPane.OK_CANCEL_OPTION);
		
		System.out.println(option);
		
		if (option == JOptionPane.OK_OPTION) {
			hostName = "jdbc:mysql://" + inputHost.getText() + ":" + inputPort.getText() + "/";
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
			
			// Displaying and choosing database
			ResultSet rs = stmt.executeQuery("show databases;");
			while (rs.next()) {
				System.out.println("Database: " + rs.getString("Database"));
			}
			System.out.print("Please choose a database to use: ");
			String dbname = s.nextLine();
			stmt.executeQuery("use " + dbname);
			
			// Displaying and choosing table
			rs = stmt.executeQuery("show tables;");
			while (rs.next()) {
				System.out.println("Tables: " + rs.getString("Tables_in_" + dbname));
			}
			System.out.print("Please choose the table you'd like to view: ");
			String tablename = s.nextLine();
			rs = stmt.executeQuery("select * from " + tablename);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();
			
			// Print table
			for (int i= 1; i < numColumns + 1; i++) {
				System.out.print(rsmd.getColumnLabel(i) + " ");
			}
			System.out.println();
			while (rs.next()) {
				for (int i = 1; i < numColumns + 1; i++) {
					System.out.print(rs.getString(i) + " ");
				}
				System.out.println();
				
			}
			
			con.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
	}
}