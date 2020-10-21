import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	private Connection conection;
	
	public static ConnectionManager connectionManager = new ConnectionManager();
	
	//constructor
	private ConnectionManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conection = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinefoodordering","root","narjes8370");    
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}  
		
	}
	
	public Connection getConnection() {
		return conection;
	}

}
