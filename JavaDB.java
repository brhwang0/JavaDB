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
		if (option == JOptionPane.OK_OPTION) {
			hostName = "jdbc:mysql://" + inputHost.getText() + ":" + inputPort.getText() + "/";
			username = inputUser.getText(); password = inputPass.getText();
		}
		else {
			System.out.println("Connection setup cancelled.");
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		//setup();
		
		Scanner s = new Scanner(System.in);
		try {
			//Connection con = DriverManager.getConnection(hostName, username, password);
			Connection con = DriverManager.getConnection("jdbc:mysql://10.255.172.2:3306/", "HH", "1234");
			Statement stmt = con.createStatement();
			String dbname, tablename;
			
			while (true) {
				// Displaying and choosing database
				ResultSet rs = stmt.executeQuery("show databases;");
				System.out.println("\n*** Database List ***\n");
				while (rs.next()) {
					System.out.println(rs.getString("Database"));
				}
				
				System.out.print("\nPlease choose a database to use: ");
				while (true) {
					try {
						
						dbname = s.nextLine();
						stmt.executeQuery("use " + dbname);
						System.out.println();
						break;
					}
					catch (SQLException e) {
						System.out.print("\nPlease enter a valid database name: ");
					}
					catch (NoSuchElementException e) {
						System.out.println("\n\nProgram terminated.");
						System.exit(1);
					}
				}
				
				// Displaying and choosing table
				rs = stmt.executeQuery("show tables;");
				System.out.println("\n*** Tables in Database \"" + dbname + "\" ***\n");
				while (rs.next()) {
					System.out.println(rs.getString("Tables_in_" + dbname));
				}
				System.out.print("\nPlease choose the table you'd like to view: ");
				while (true) {
					try {
						tablename = s.nextLine();
						rs = stmt.executeQuery("select * from " + tablename);
						System.out.println();
						break;
					}
					catch (SQLException e) {
						System.out.print("\nPlease enter a valid table name: ");
					}
					catch (NoSuchElementException e) {
						System.out.println("\n\nProgram terminated.");
						System.exit(1);
					}
				}
				// Printing table
				ResultSetMetaData rsmd = rs.getMetaData();
				int numColumns = rsmd.getColumnCount();
				
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
				
				// Ask user if they want to view another table
				System.out.println();
				System.out.print("Would you like to view another table? Please enter Y/N: ");
				while (true) {
					try {
						String option = s.nextLine();
						char c = Character.toLowerCase(option.charAt(0));
						
						if (option.length() != 1) {
							System.out.print("Invalid input. Please enter Y or N: ");
						}
						else if (c == 'n') {
							con.close();
							System.exit(0);
						}
						else if (c == 'y') {
							break;
						}
						else {
							System.out.println("Invalid input. Please enter Y or N: ");
						}
					}
					catch (StringIndexOutOfBoundsException e) {
						System.out.print("Invalid input. Please enter Y or N: ");
					}
					catch (NoSuchElementException e) {
						System.out.println("\n\nProgram terminated.");
						System.exit(1);
					}
				}
			}
		}
		
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		catch (Exception e) {
			System.out.println(e);
		}
		
	}
}