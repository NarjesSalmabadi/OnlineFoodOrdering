import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerEntityManager {
	Logger logger = LoggerFactory.getLogger(CustomerEntityManager.class);
	
	/*show all restaurant's customers*/
	public List<Customer> showCustomer(Connection con) throws Exception{
		Statement stmt=con.createStatement(); 
		ResultSet rs = stmt.executeQuery("SELECT * FROM onlinefoodordering.customers");
		List<Customer> customers = new ArrayList<>();
		while(rs.next()) {
			customers.add(new Customer(rs.getString(2),rs.getInt(1),rs.getInt(3)));
		}
		stmt.close();
		return customers;
	}

	/*If a customer exist already return customer*/
	public Customer customerExist(Connection con, long phoneNumber) throws Exception{
		Statement stmt=con.createStatement(); 
		ResultSet rs = stmt.executeQuery("SELECT * FROM onlinefoodordering.customers where PhoneNumber="+phoneNumber);
		if(rs.next()) {
			Customer customer = new Customer(rs.getString(2),rs.getLong(1),rs.getLong(3));
			stmt.close();
			return customer;
		}
		else {
			stmt.close();
			return null;
		}		
	}
	
	/*Insert a customer into customers table MySQL*/
	public int customerInsert(Connection con, long phoneNumber, String name, long postalCode) throws Exception{
		String sql = "insert into customers (PhoneNumber, Name, PostalCode, RegistrationDate) values (?, ?, ?, ?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1, phoneNumber);
		pstmt.setString(2, name);
		pstmt.setLong(3, postalCode);
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		pstmt.setDate(4, sqlDate);
		int customernum = pstmt.executeUpdate();
		pstmt.close();
		logger.trace("new customer added to DB: phoneNumber:{} name:{} postalCode",phoneNumber,name,sqlDate);
		return customernum;
	}
	
	/*Edit customer's information in table MySQL*/
	public void customerEdit(Connection con, long phoneNumber, String name, long postalCode) throws SQLException {
		Statement stmt=con.createStatement(); 
		stmt.executeUpdate("UPDATE customers SET Name = \""+name+"\", PostalCode = "+postalCode+" where PhoneNumber="+phoneNumber+";");
		logger.trace("Information of customer {} is editted. new name:{} new postalCode:{}",phoneNumber,name,postalCode);
		stmt.close();
		
	}
}
