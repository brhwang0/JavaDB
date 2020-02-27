import java.util.*;
import java.sql.*;
import javax.swing.*;

public class JavaDB {
	
	static String connectionURL;
	static String username;
	static String password;
	static Scanner s = new Scanner(System.in);
	
	public static void setup() {
		
		JTextField inputHost = new JTextField();
		inputHost.addAncestorListener(new RequestFocusListener());
		JTextField inputPort = new JTextField();
		JTextField inputUser = new JTextField();
		JTextField inputPass = new JPasswordField();
		JCheckBox useSSL = new JCheckBox("Connect using SSL");
		Object[] message = {
			"Host IP: ", inputHost,
			"Port: ", inputPort,
			"Username:", inputUser,
			"Password:", inputPass,
			useSSL
		};
		
		int option = JOptionPane.showConfirmDialog(null, message, "Connection Setup", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			connectionURL = "jdbc:mysql://" + inputHost.getText() + ":" + inputPort.getText() + "/";
			username = inputUser.getText(); password = inputPass.getText();
			if (useSSL.isSelected()) {
				connectionURL += "?useSSL=true&verifyServerCertificate=false";
			}
			else {
				connectionURL += "?useSSL=false&verifyServerCertificate=false";
			}
		}
		else {
			System.out.println("Connection setup cancelled.");
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		setup();
		
		try {
			// Establishing connection to server
			Connection con = DriverManager.getConnection(connectionURL, username, password);
			Statement stmt = con.createStatement();
			String dbname = "", tablename = "";
			
			// Creating list of available databases
			ArrayList<String> dbList = new ArrayList<String>();
			ResultSet rs = stmt.executeQuery("show databases;");
			while (rs.next()) {
				dbList.add(rs.getString("Database"));
			}
			
			// Creating radio button for each database
			JRadioButton dbArr[] = new JRadioButton[dbList.size()];
			ButtonGroup dbGroup = new ButtonGroup();
			for (int i = 0; i < dbArr.length; i++) {
				dbArr[i] = new JRadioButton(dbList.get(i));
				dbGroup.add(dbArr[i]);
			}
			
			// Populate database options
			Object[] databases = {
				"Please choose a database to use: ", dbArr
			};
			dbArr[0].setSelected(true);
			
			while (true) {
				// Display DB Selection dialog
				int dbPane = JOptionPane.showConfirmDialog(null, databases, "Database List", JOptionPane.OK_CANCEL_OPTION);
				
				// Check user selection
				if (dbPane == JOptionPane.OK_OPTION) {
					// Get selected database
					for (int i = 0; i < dbArr.length; i++) {
						if (dbArr[i].isSelected()) {
							dbname = dbList.get(i);
						}
					}
					
					stmt.executeQuery("use " + dbname);
					
					// Creating list of available tables
					rs = stmt.executeQuery("show tables;");
					ArrayList<String> tableList = new ArrayList<String>();
					while (rs.next()) {
						tableList.add(rs.getString("Tables_in_" + dbname));
					}
					
					// Creating radio button for each table
					JRadioButton tableArr[] = new JRadioButton[tableList.size()];
					ButtonGroup tableGroup = new ButtonGroup();
					for (int i = 0; i < tableArr.length; i++) {
						tableArr[i] = new JRadioButton(tableList.get(i));
						tableGroup.add(tableArr[i]);
					}
					
					// Populate table options
					Object[] tables = {
						"Please choose a table to view: ", tableArr
					};
					tableArr[0].setSelected(true);
					
					// Display Table Selection dialog
					int tablePane = JOptionPane.showConfirmDialog(null, tables, "Table List", JOptionPane.OK_CANCEL_OPTION);
					
					// Check user selection
					if (tablePane == JOptionPane.OK_OPTION) {
						// Get selected table and display table data
						for (int i = 0; i < tableArr.length; i++) {
							if (tableArr[i].isSelected()) {
								tablename = tableList.get(i);
							}
						}
						rs = stmt.executeQuery("select * from " + tablename);
					}
					else {
						System.out.println("Program terminated.");
						System.exit(0);
					}
				}
				
				else {
					System.out.println("Program terminated.");
					System.exit(0);
				}
				
				// Printing table to console
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