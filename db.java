import java.sql.*;  

public class db {  
	public static void main(String args[]){  
		try {  
			Class.forName("com.mysql.cj.jdbc.Driver");  
			Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.37:3306/pokemon","Brandon","123");
			Statement stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from pokedex");  
			while(rs.next()) {
				System.out.println(rs.getString(1) + "  " + rs.getString(2));
			}
			con.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}  
	}  
}  