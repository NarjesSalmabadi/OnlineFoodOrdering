import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner read = new Scanner(System.in);
		CLI cli = new CLI();
		
		try {
			//cli.readRestaurantFile();
			//user must enter phone number and region
			System.out.println("Enter your phone number without the zero:");
			long phoneNumber = read.nextLong();
			Customer costomer = new Customer(phoneNumber);
			System.out.println("Enter your region:");
			int region = read.nextInt();
			
			String category,restaurantName,foodName;
			Integer quantity;
			FileWriter fw;
			BufferedWriter bf;
			int totalPrice=0;
			boolean a,b;
			
			while(true) {
			System.out.println("1. Show all restaurants in your region");
			System.out.println("2. Show restaurants in your region with a spicify category");
			System.out.println("3. Exit");
			int n = read.nextInt();
			
			switch(n){
			case 1://Show restaurants in a specify region then order a food
				cli.showResturant(region);
				//user must choose a restaurant
				System.out.println("Enter your favorite restaurant:");
				restaurantName = read.next();
				//show foods of selected restaurant
				cli.showfoods(restaurantName);
				//user must choose a food and its count
				a = true;
				while(a) {
					System.out.println("1. Select a food");
					System.out.println("2. Show Shopping Cart");
					System.out.println("3. Finalize the order");
					System.out.println("4. Rturn to main menu");
					int operation = read.nextInt();
					
					switch(operation) {
					case 1: //customer want to select a food
						read.nextLine();
						System.out.println("Enter your favorite food:");
						foodName = read.nextLine();
						System.out.println("Enter qauntity of order:");
						quantity = read.nextInt();
						costomer.ShoppingCart.put(cli.Selectfoods(restaurantName, foodName),quantity);
						break;
					case 2: //The customer wants to see shopping cart
						totalPrice =0;
						if (costomer.ShoppingCart.size()!=0) {
							for (Map.Entry<Food, Integer> entry : costomer.ShoppingCart.entrySet()) {
								Food f = (Food) entry.getKey();
								System.out.println("[Name: "+f.name+"  Price: "+f.price+"  Category: "+f.category+"  Count: "+entry.getValue()+"]"+"\n");
								totalPrice += f.price*entry.getValue();
							}
							System.out.println(totalPrice);
						}
						else {
							System.out.println("your Shopping Cart is empty!");
						}
						b = true;
						while(b) {
						System.out.println("1. delete a food from Shopping Cart");
						System.out.println("2. return to previous menu");
						int n1 = read.nextInt();
						switch(n1) {
						case 1: //delete a food from Shopping Cart
							read.nextLine();
							System.out.println("Enter the food's name that you want to delede:");
							String foodName1 = read.nextLine();
							for (Map.Entry<Food, Integer> entry : costomer.ShoppingCart.entrySet()) {
								Food f = (Food) entry.getKey();
								if(f.name.equals(foodName1)) {
									costomer.ShoppingCart.remove(f);
									break;
								}		
							}
							break;
						case 2: //return to previous menu
							b=false;
							break;
							}
						}
						
						break;
					case 3: //The customer wants to finalize the order
						if(cli.customerExist(phoneNumber) != null) { //if customer exist already
							Map<Food, Integer> map = costomer.ShoppingCart;
							costomer = cli.customerExist(phoneNumber);
							costomer.ShoppingCart = map;
							System.out.println(costomer.name+"  "+costomer.phoneNumber+"  "+costomer.postalCode);
							System.out.println("1. Edit profile information");
							System.out.println("2. Confirm profile information");
							int operation1 = read.nextInt();
							switch(operation1) {
							case 1:
								System.out.println("Enter new username");
								costomer.name = read.next();
								System.out.println("Enter new postal code");
								costomer.postalCode = read.nextLong();
								cli.customerEdit(phoneNumber, costomer.name, costomer.postalCode);
								System.out.println("Your information edit successfully");
								break;
							case 2:
								break;
							}
							
						}
						else {  //if customer doesn't exist already
							System.out.println("Customer with this phone number doesn't exist already");
							System.out.println("Enter your name");
							String name = read.next();
							System.out.println("Enter your postalCode");
							long postalCode = read.nextLong();
							costomer.name=name;
							costomer.postalCode=postalCode;
							cli.customerInsert(phoneNumber, name, postalCode);
						}
						//insert order to database
						int orderNumber = cli.orderInsert(phoneNumber);
						//write customer's bills in a text file and insert order details to database
						totalPrice =0;
						fw = new FileWriter("C:\\Users\\mashadservice.ir\\Desktop\\"+costomer.name+".txt");
						bf = new BufferedWriter(fw);
						bf.write("Time of order: "+new Timestamp(System.currentTimeMillis())+"\n");
						bf.write("Foods"+"\n");
						for (Map.Entry<Food, Integer> entry : costomer.ShoppingCart.entrySet()) {
							Food f = (Food) entry.getKey();
							bf.write("[Name: "+f.name+"  Price: "+f.price+"  Category: "+f.category+"  Count: "+entry.getValue()+"]"+"\n");
							cli.orderDetailsInsert(orderNumber, f.id, entry.getValue());
							totalPrice += f.price*entry.getValue();
						}
						bf.write("Total Price= "+totalPrice+"\n");
						bf.flush();
						bf.close();
						System.out.println("Your order has been successfully registered");
						break;
					case 4: //customer want to exit						
						if(costomer.ShoppingCart.size()!=0) {
							System.out.println("Your cart will be empty. Are you sure? yes/no");
							String s = read.next();
							if(s.equalsIgnoreCase("yes")) {
								costomer.ShoppingCart.clear();
								a=false;
							}
						}else {a=false;}
						break;
					}
				}
				break;
			case 2://Show restaurants in a specify region and with a Specific Category then order a food
				System.out.println("Enter your favorite category:");
				category = read.next();
				cli.showResturantHasSpecificCategory(region, category);
				//user must choose a restaurant
				System.out.println("Enter your favorite restaurant:");
				restaurantName = read.next();
				cli.showfoodHasSpecificCategory(restaurantName, category);
				//user must choose a food and its count
				a = true;
				while(a) {
					System.out.println("1. Select a food");
					System.out.println("2. Show Shopping Cart");
					System.out.println("3. Finalize the order");
					System.out.println("4. Rturn to main menu");
					int operation = read.nextInt();
					
					switch(operation) {
					case 1: //customer want to select a food
						read.nextLine();
						System.out.println("Enter your favorite food:");
						foodName = read.nextLine();
						System.out.println("Enter qauntity of order:");
						quantity = read.nextInt();
						costomer.ShoppingCart.put(cli.Selectfoods(restaurantName, foodName),quantity);
						break;
					case 2: //The customer wants to see shopping cart
						totalPrice =0;
						if (costomer.ShoppingCart.size()!=0) {
							for (Map.Entry<Food, Integer> entry : costomer.ShoppingCart.entrySet()) {
								Food f = (Food) entry.getKey();
								System.out.println("[Name: "+f.name+"  Price: "+f.price+"  Category: "+f.category+"  Count: "+entry.getValue()+"]"+"\n");
								totalPrice += f.price*entry.getValue();
							}
							System.out.println(totalPrice);
						}
						else {
							System.out.println("your Shopping Cart is empty!");
						}
						b = true;
						while(b) {
						System.out.println("1. delete a food from Shopping Cart");
						System.out.println("2. return to previous menu");
						int n1 = read.nextInt();
						switch(n1) {
						case 1: //delete a food from Shopping Cart
							read.nextLine();
							System.out.println("Enter the food's name that you want to delede:");
							String foodName1 = read.nextLine();
							for (Map.Entry<Food, Integer> entry : costomer.ShoppingCart.entrySet()) {
								Food f = (Food) entry.getKey();
								if(f.name.equals(foodName1)) {
									costomer.ShoppingCart.remove(f);
									break;
								}		
							}
							break;
						case 2: //return to previous menu
							b=false;
							break;
							}
						}
						
						break;
					case 3: //The customer wants to finalize the order
						if(cli.customerExist(phoneNumber) != null) { //if customer exist already
							Map<Food, Integer> map = costomer.ShoppingCart;
							costomer = cli.customerExist(phoneNumber);
							costomer.ShoppingCart = map;
							System.out.println(costomer.name+"  "+costomer.phoneNumber+"  "+costomer.postalCode);	
							System.out.println("1. Edit profile information");
							System.out.println("2. Confirm profile information");
							int operation1 = read.nextInt();
							switch(operation1) {
							case 1:
								System.out.println("Enter new username");
								costomer.name = read.next();
								System.out.println("Enter new postal code");
								costomer.postalCode = read.nextLong();
								cli.customerEdit(phoneNumber, costomer.name, costomer.postalCode);
								System.out.println("Your information edit successfully");
								break;
							case 2:
								break;
							}
						}
						else {  //if customer doesn't exist already
							System.out.println("Customer with this phone number doesn't exist already");
							System.out.println("Enter your name");
							String name = read.next();
							System.out.println("Enter your postalCode");
							long postalCode = read.nextLong();
							costomer.name=name;
							costomer.postalCode=postalCode;
							cli.customerInsert(phoneNumber, name, postalCode);
						}
						//insert order to database
						int orderNumber = cli.orderInsert(phoneNumber);
						//write customer's bills in a text file and insert order details to database
						totalPrice =0;
						fw = new FileWriter("C:\\Users\\mashadservice.ir\\Desktop\\customer.txt");
						bf = new BufferedWriter(fw);
						bf.write("Time of order: "+new Timestamp(System.currentTimeMillis())+"\n");
						bf.write("Foods"+"\n");
						for (Map.Entry<Food, Integer> entry : costomer.ShoppingCart.entrySet()) {
							Food f = (Food) entry.getKey();
							bf.write("[Name: "+f.name+"  Price: "+f.price+"  Category: "+f.category+"  Count: "+entry.getValue()+"]"+"\n");
							cli.orderDetailsInsert(orderNumber, f.id, entry.getValue());
							totalPrice += f.price*entry.getValue();
						}
						bf.write("Total Price= "+totalPrice+"\n");
						bf.flush();
						System.out.println("Your order has been successfully registered");
						break;
					case 4: //customer want to exit						
						if(costomer.ShoppingCart.size()!=0) {
							System.out.println("Your cart will be empty. Are you sure? yes/no");
							String s = read.next();
							if(s.equalsIgnoreCase("yes")) {
								costomer.ShoppingCart.clear();
								a=false;
							}
						}else {a=false;}
						break;
					}
				}
				break;
			case 3:
				System.exit(0);
			}
	            
			}			
		}
		catch(Exception e) {
			System.err.println(e);
		}
		
		
		
		
		

	}

}
