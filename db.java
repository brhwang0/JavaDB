import java.util.*;
import java.sql.*;

public class JavaDB {
	
	public static void main(String[] args) {
		
		Scanner s = new Scanner(System.in);
		System.out.print("Please enter the host URL or IP address: ");
		String host = s.nextLine();
		System.out.print("Please enter the database name: ");
		String dbname = s.nextLine();
		System.out.print("Please enter username: ");
		String username = s.nextLine();
		System.out.print("Please enter password: ");
		String password = s.nextLine();
		String connection = "jdbc:mysql://" + host + ":3306/" + dbname;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(connection, username, password);
			//Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.37:3306/pokemon","HH","123");   
			Statement stmt = con.createStatement();  
			ResultSet rs = stmt.executeQuery("select * from pokedex");
			while (rs.next()) {
				System.out.println(rs.getString(1) +"  " + rs.getString(2));
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
	}
}