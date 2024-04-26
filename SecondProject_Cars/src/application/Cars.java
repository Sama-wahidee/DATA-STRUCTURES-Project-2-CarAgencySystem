package application;

public class Cars {
	private String brand;
	private String model;
	private int year;
	private String color;
	private int price;
	private String url = "/resources/no (1).png";

	public Cars() {

	}

	public Cars(String brand, String model, int year, String color, int price) {
		super();
		this.brand = brand;
		this.model = model;
		this.year = year;
		this.color = color;
		this.price = price;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		return false;
	}

	public int compareTo(Cars c) {
		if ((this.getBrand().compareTo(c.getBrand()) == 0) && (this.model.compareTo(c.getModel()) == 0)
				&& (Integer.compare(this.getYear(), c.getYear()) == 0) && (this.getColor().compareTo(c.getColor()) == 0)
				&& (Integer.compare(this.getPrice(), c.getPrice()) == 0)) {
			return 0;
		}
		return 1;
	}

	public String toString() {
		return brand + ", " + model + ", " + year + ", " + color + ", " + price / 1000 + "K";
	}
}
