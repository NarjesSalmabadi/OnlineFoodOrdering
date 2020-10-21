import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrderEntityManager {
	Logger logger = LoggerFactory.getLogger(OrderEntityManager.class);
	
	/*Insert a order into orders table MySQL*/
	public int orderInsert(Connection con, long phoneNumber) throws Exception{
		Statement stmt=con.createStatement(); 
		int orderNumber=0;
		ResultSet rs = stmt.executeQuery("SELECT max(OrderNumber) FROM orders");
		if(rs.next())
			orderNumber = rs.getInt(1)+1;
		else
			orderNumber=1;
		String sql = "insert into orders (OrderNumber, Customers_PhoneNumber, OrderDate) values (?, ?, ?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, orderNumber);
		pstmt.setLong(2, phoneNumber);
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		pstmt.setDate(3, sqlDate);
		pstmt.executeUpdate();
		pstmt.close();
		logger.trace("new order added to DB: orderNumber:{} phoneNumber:{} orderDate:{}",orderNumber,phoneNumber,sqlDate);
		return orderNumber;
	}
	
	/*Insert order detains into orders_details table MySQL*/
	public int orderDetailsInsert(Connection con, int orderNumber, int foodNumber, int quantity) throws Exception{
		Statement stmt=con.createStatement(); 
		int ordernum = stmt.executeUpdate("INSERT INTO ordersdetails VALUES ("+foodNumber+","+ orderNumber+","+quantity+");");
		stmt.close();
		logger.trace("order's foods added to DB: orderNumber:{} foodNumber:{}",orderNumber,foodNumber);
		return ordernum;
	}

}
