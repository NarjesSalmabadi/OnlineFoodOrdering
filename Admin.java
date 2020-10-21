import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Admin {
	
	public static void main(String[] args) {
		
		Connection con = ConnectionManager.connectionManager.getConnection();
		try {
			
			List<Customer> customers = userReview (con);
			final Integer[] months = {1,2,3,4,5,6,7,8,9,10,11,12};
			for (Integer month : months) {
				customers.stream()
				.filter(c -> c.registrationDate.getMonth()+1 == month).filter(c -> c.totalPurchase<100000)
				.forEach(c->System.out.println((c.registrationDate.getMonth()+1)+" totalPurchase<100,000 "+c.name+" "+c.phoneNumber));
				
				customers.stream()
				.filter(c -> c.registrationDate.getMonth()+1 == month)
				.filter(c -> c.totalPurchase>=100000 && c.totalPurchase<500000)
				.forEach(c->System.out.println((c.registrationDate.getMonth()+1)+" 100,000<=totalPurchase<500,000 "+c.name+" "+c.phoneNumber));
				
				customers.stream()
				.filter(c -> c.registrationDate.getMonth()+1 == month)
				.filter(c -> c.totalPurchase>=500000)
				.forEach(c->System.out.println((c.registrationDate.getMonth()+1)+" totalPurchase>=500,000 "+c.name+" "+c.phoneNumber));	
			}
			
			List<AlternativeRest> resturants = resturantsReview(con);
			for(AlternativeRest a: resturants)
				System.out.println(a.name+" "+a.yearlyShippingCost+" "+ a.region);
			final Integer[] region = {1,2,3,4,5};
			for(Integer i: region) {
				resturants.stream()
				.filter(r -> r.region==i)
				.filter(r -> r.yearlyShippingCost<15000)
				.forEach(r->System.out.println(r.region+" yearlyShippingCost<15,000 "+r.name+" "+r.bestSellingFood));
				
				resturants.stream()
				.filter(r -> r.region==i)
				.filter(r -> r.yearlyShippingCost>=15000 && r.yearlyShippingCost<30000)
				.forEach(r->System.out.println(r.region+" 15,000<=yearlyShippingCost<30,000 "+r.name+" "+r.bestSellingFood));
				
				resturants.stream()
				.filter(r -> r.region==i)
				.filter(r -> r.yearlyShippingCost>=30000)
				.forEach(r->System.out.println(r.region+" yearlyShippingCost>=30,000 "+r.name+" "+r.bestSellingFood));
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static List<Customer> userReview (Connection con) throws Exception{
		Statement stmt = con.createStatement(); 
		ResultSet rs = stmt.executeQuery("select RegistrationDate,Name,PhoneNumber, sum(Quantity*FoodPrice) as totalSales from customers \r\n" + 
				"	join orders on customers.PhoneNumber = orders.Customers_PhoneNumber\r\n" + 
				"		join ordersdetails on orders.OrderNumber = ordersdetails.Orders_OrderNumber\r\n" + 
				"			join resturantsfoods on ordersdetails.ResturantsFoods_FoodNumber = resturantsfoods.FoodNumber\r\n" + 
				"				 group by PhoneNumber;");  
		List<Customer> customers = new ArrayList<>();
		
		while(rs.next()) {
			//Customer customer = new Customer();
			//customer.setName(rs.getString(2));
			//customers.add(customer);
			customers.add(new Customer(rs.getString(2),rs.getLong(3), rs.getDate(1),rs.getInt(4)));
		}
		stmt.close();
		rs.close();
		return customers;		
	}
	
	public static List<AlternativeRest> resturantsReview(Connection con) throws SQLException {
		List<AlternativeRest> resturants = new ArrayList<>();
		Statement stmt =con.createStatement();
		ResultSet rs=stmt.executeQuery("Select FoodName, resturants.Name,max(Quantity) ,resturants.Region,count(distinct Orders_OrderNumber)*resturants.ShippingCost "
										+ "from resturants,resturantsfoods,ordersdetails where resturants.Name=resturantsfoods.resturants_Name "
										+ "and resturantsfoods.FoodNumber=ordersdetails.ResturantsFoods_FoodNumber group by resturants.Name;");
		while(rs.next()) {
			resturants.add(new AlternativeRest(rs.getString(2),rs.getInt(5),rs.getInt(4),rs.getString(1)));	
		}
		stmt.close();
		rs.close();
		return resturants;
	}	
		
}

class AlternativeRest {
	
	String name;
	int yearlyShippingCost;
	int region;
	String bestSellingFood;
	
	public AlternativeRest(String name, int yearlyShippingCost, int region, String bestSellingFood) {
		this.name = name;
		this.yearlyShippingCost = yearlyShippingCost;
		this.region = region;
		this.bestSellingFood = bestSellingFood;
	}	
}
