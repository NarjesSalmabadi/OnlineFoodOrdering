import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResturantEntityManager {
	Logger logger = LoggerFactory.getLogger(ResturantEntityManager.class);
	
	/*Show names of restaurants in a specify region*/
	public Map<Resturant, String> showResturant(Connection con, int region) throws Exception{
		Statement stmt=con.createStatement(); 
		ResultSet rs=stmt.executeQuery("SELECT DISTINCT resturants.Name,resturants.ShippingCost,resturantsfoods.Category FROM "
										+ "resturants join resturantsfoods on resturants.Name=resturantsfoods.resturants_Name "
										+ "where resturants.Region="+region+";");  
		Map<Resturant,String> resturants = new HashMap<>();
		while(rs.next()) {
			resturants.put(new Resturant(rs.getString(1),rs.getInt(2),region), rs.getString(3));
		}
		stmt.close();
		return resturants;		
	}

	/*Show names of restaurants in a specific region with specific category*/
	public Set<Resturant> showResturantHasSpecificCategory(Connection con, int region, String category) throws Exception{
		Statement stmt=con.createStatement(); 
		ResultSet rs=stmt.executeQuery("SELECT DISTINCT resturants.Name,resturants.ShippingCost FROM "
				+ "resturants join resturantsfoods on resturants.Name=resturantsfoods.resturants_Name "
				+ "where resturants.Region="+region+" AND resturantsfoods.Category =\""+category+"\""); 
		Set<Resturant> resturants = new HashSet<>();
		while(rs.next()) {
			resturants.add(new Resturant(rs.getString(1),rs.getInt(2),region));
		}
		stmt.close();
		return resturants;		
	}
	
	/*Show foods of a restaurant*/
	public List<Food> showfoods(Connection con, String restaurantName) throws Exception{
		Statement stmt=con.createStatement(); 
		ResultSet rs=stmt.executeQuery("SELECT * FROM resturantsfoods WHERE resturantsfoods.resturants_Name=\""+restaurantName+"\";");  
		List<Food> foods = new ArrayList<>();
		while(rs.next()) {
			foods.add(new Food(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4)));
		}
		stmt.close();
		return foods;		
	}
	
	/*Show foods of a restaurant with a Specific Category*/
	public List<Food> showfoodHasSpecificCategory(Connection con, String restaurantName, String category) throws Exception{
		Statement stmt=con.createStatement(); 
		ResultSet rs=stmt.executeQuery("SELECT * FROM resturantsfoods WHERE resturants_Name=\""+restaurantName+"\" AND Category=\""+category+"\";");  
		List<Food> foods = new ArrayList<>();
		while(rs.next()) {
			foods.add(new Food(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4)));
		}
		stmt.close();
		return foods;		
	}
	
	/*Read restaurant.txt and insert to database*/
	public void readRestaurantFile(Connection con) throws SQLException, IOException {
		try {
		Statement stmt = con.createStatement();
		int rs;
		int rs2;
		File file_to_read = new File("restaurant.txt");
		BufferedReader bf = new BufferedReader(new FileReader(file_to_read));
		String nameOfRest="",countOfFood="";
		String region="";
		String shippingCost="";
		//////////////////
		String nameOfFood="";
		String price="";
		String category="";
		int countOfRestId=1;
		int countOfFoods=1;

		String line;	

		int numberOfRest = bf.read();
		while ((line = bf.readLine())!=null){	
			for(int i=0;i<numberOfRest && (line = bf.readLine())!=null;i++) {
	            String tmp[] = line.split(", ");
	            nameOfRest = tmp[0];
	            countOfFood = tmp[1];
	            region = tmp[2];
	            shippingCost = tmp[3];
	            rs=stmt.executeUpdate("INSERT INTO resturants (Name,ShippingCost,Region) values (\""+nameOfRest+"\", "+Integer.parseInt(shippingCost)+", "+Integer.parseInt(region)+")");
	            logger.trace("new resturant added to DB: resturantName:{} region:{} shippingCost:{}",nameOfRest,region,shippingCost);
	            for(int j=0;j<Integer.parseInt(countOfFood) && (line = bf.readLine())!=null;j++) {	
	            	String tmpFood[]=line.split(", ");
	            	nameOfFood=tmpFood[0];
	            	price=tmpFood[1];
	            	category=tmpFood[2];
	            	rs2=stmt.executeUpdate("insert into resturantsfoods (FoodNumber,FoodName,FoodPrice,Category,resturants_Name) values ("+countOfFoods+",\""+nameOfFood+"\","+Integer.parseInt(price)+",\""+category+"\",\""+nameOfRest+"\")");
	            	logger.trace("new food added to DB: resturantName:{} foodName:{} price:{}",nameOfRest,nameOfFood,price);
	            	countOfFoods++;
	            	
	            }
	            countOfRestId++;	            
			}           
        }
        bf.close();       
        stmt.close();
    }	
    catch (IOException e)
    {
        e.printStackTrace();
    }
        
	}
}
