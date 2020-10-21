import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class Customer{
	
	String name;
	long phoneNumber;
	long postalCode;
	Date registrationDate;
	int totalPurchase;
	Map<Food,Integer> ShoppingCart = new HashMap<Food,Integer>();
	
	/*Constructor*/
	public Customer (){}
	
	/*Constructor*/
	public Customer (long phoneNumber) throws Exception{	
		setPhoneNumber(phoneNumber);
	}
	
	/*Constructor*/
	public Customer (String name, long phoneNumber, long postalCode) throws Exception{
		this.name = name;
		setPhoneNumber(phoneNumber);
		setPostalCode(postalCode);
	}
	
	/*Constructor*/
	public Customer (String name, long phoneNumber, Date registrationDate, int totalPurchase) throws Exception{
		this.name = name;
		setPhoneNumber(phoneNumber);
		this.registrationDate = registrationDate;
		this.totalPurchase = totalPurchase;
	}
	
	/*Getter*/
	public String getName() {
		return name;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public long getPostalCode() {
		return postalCode;
	}

	/*Setter*/
	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneNumber(long phoneNumber) throws Exception{
		if(String.valueOf(phoneNumber).matches("^\\d{10}$"))
			this.phoneNumber = phoneNumber;
		else 
			throw new Exception("phoneNumberDigitsException");
	}

	public void setPostalCode(long postalCode) throws Exception{
		if(String.valueOf(postalCode).matches("^\\d{10}$"))
			this.postalCode = postalCode;
		else 
			throw new Exception("postalCodeDigitsException");
	}
}
