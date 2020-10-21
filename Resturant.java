
public class Resturant {
	
	String name;
	int shippingCost;
	int region;
	
	/*Constructor*/
	public Resturant(String name, int shippingCost, int region) {
		this.name = name;
		this.region = region;
		this.shippingCost = shippingCost;
	}

	/*Getter*/
	public String getName() {
		return name;
	}

	public int getShippingCost() {
		return shippingCost;
	}

	public int getRegion() {
		return region;
	}

	/*Setter*/
	public void setName(String name) {
		this.name = name;
	}

	public void setShippingCost(int shippingCost) {
		this.shippingCost = shippingCost;
	}

	public void setRegion(int region) {
		this.region = region;
	}
	
	

}
