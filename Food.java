
public class Food {
	int id;
	String name;
	int price;
	String category;
	Resturant resturant; 
	
	/*Constructor*/
	public Food(int id, String name, int price, String category) {
		this.name = name;
		this.price = price;
		this.category = category;
		this.id = id;
	}

	/*Getter*/
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public String getCategory() {
		return category;
	}

	/*Setter*/
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
}
