import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

public class CLI {

	Connection con; 
	
	public CLI () {
		con = ConnectionManager.connectionManager.getConnection();
	}
	
	/*--------------------------------------------------------------
	 * show restaurants query
	 *------------------------------------------------------------- */
	
	/*read restaurant.txt and insert to database*/
	public void readRestaurantFile() throws Exception{
		ResturantEntityManager resturantEntityManager = new ResturantEntityManager();
		resturantEntityManager.readRestaurantFile(con);
	}
	
	/*Show names of restaurants in a specify region*/
	public void showResturant(int region) throws Exception {
		ResturantEntityManager resturantEntityManager = new ResturantEntityManager();
		Map<Resturant, String> returants = resturantEntityManager.showResturant(this.con, region);
		System.out.println("Resturant Name"+"  shipping Cost"+ "   Food Category");
		for(Map.Entry entry : returants.entrySet()) {
			Resturant r = (Resturant)entry.getKey();
			System.out.println(r.name+"               "+r.shippingCost+"           "+entry.getValue());
		}
		//con.close();
	}
	
	/*Show names of restaurants in a specific region with specific category*/
	public void showResturantHasSpecificCategory(int region, String category) throws Exception {
		ResturantEntityManager resturantEntityManager = new ResturantEntityManager();
		Set<Resturant> resturants = resturantEntityManager.showResturantHasSpecificCategory(this.con, region, category);
		System.out.println("Resturant Name"+"  shipping Cost");
		for(Resturant r : resturants) {
			System.out.println(r.name+"               "+r.shippingCost+"           ");
		}
		//con.close();
	}
	
	/*--------------------------------------------------------------
	 * show foods query
	 *------------------------------------------------------------- */
	
	/*Show foods of a restaurant*/
	public void showfoods(String restaurantName) throws Exception {
		ResturantEntityManager resturantEntityManager = new ResturantEntityManager();
		List<Food> foods = resturantEntityManager.showfoods(con, restaurantName);
		System.out.println("Food Name"+"            Prcie" + "             Category");
		for(Food f : foods) {
			System.out.println(f.name+"        "+f.price+"           "+ "    "+f.category);
		}		
	}
	
	/*Show foods of a restaurant with a Specific Category*/
	public void showfoodHasSpecificCategory(String restaurantName, String category) throws Exception {
		ResturantEntityManager resturantEntityManager = new ResturantEntityManager();
		List<Food> foods = resturantEntityManager.showfoodHasSpecificCategory(con, restaurantName, category);
		System.out.println("Food Name"+"            Prcie" + "             Category");
		for(Food f : foods) {
			System.out.println(f.name+"        "+f.price+"           "+ "    "+f.category);
		}
	}
	
	/*Select a food from a restaurant*/
	public Food Selectfoods(String restaurantName, String foodName) throws Exception {
		ResturantEntityManager resturantEntityManager = new ResturantEntityManager();
		List<Food> foods = resturantEntityManager.showfoods(con, restaurantName);
		for(Food f : foods) {
			if(f.name.equalsIgnoreCase(foodName))
				return f;
		}
		return null;
	}
	
	/*--------------------------------------------------------------
	 * show customers query
	 *------------------------------------------------------------- */
	
	/*show all restaurant's customers*/
	public void showCustomers() throws Exception{
		CustomerEntityManager customerEntityManager = new CustomerEntityManager();
		List<Customer> customers = customerEntityManager.showCustomer(con);
		for(Customer c: customers) {
			System.out.println(c.name+"  "+c.phoneNumber+"  "+c.postalCode);
		}
	}
	
	/*show customer's information that exist already*/
	public Customer customerExist(long phoneNumber) throws Exception {
		CustomerEntityManager customerEntityManager = new CustomerEntityManager();
		if(customerEntityManager.customerExist(con, phoneNumber) != null)
			return customerEntityManager.customerExist(con, phoneNumber);
		else {
			return null;
		}		
	}
	
	/*Insert a customer into customers table MySQL*/
	public void customerInsert(long phoneNumber, String name, long postalCode) throws Exception{
		CustomerEntityManager customerEntityManager = new CustomerEntityManager();
		customerEntityManager.customerInsert(con, phoneNumber, name, postalCode);
		System.out.println("new customer aded successfull.");
	}
	
	public void customerEdit(long phoneNumber, String name, long postalCode) throws SQLException {
		CustomerEntityManager customerManager = new CustomerEntityManager();
		customerManager.customerEdit(con, phoneNumber, name, postalCode);
	}
	
	/*--------------------------------------------------------------
	 * show orders query
	 *------------------------------------------------------------- */
	
	/*Insert a order into orders table MySQL*/
	public int orderInsert(long phoneNumber) throws Exception{
		OrderEntityManager orderEntityManager = new OrderEntityManager();
		return orderEntityManager.orderInsert(con, phoneNumber);
	}
	
	/*Insert order detains into orders_details table MySQL*/
	public void orderDetailsInsert(int orderNumber, int foodNumber, int quantity) throws Exception{
		OrderEntityManager orderEntityManager = new OrderEntityManager();
		orderEntityManager.orderDetailsInsert(con, orderNumber, foodNumber, quantity);
	}
}
