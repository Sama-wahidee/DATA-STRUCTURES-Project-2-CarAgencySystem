//Name:sama wahidee
//Id:1211503
//Section:1
package application;

public class DoubleNode {
	String Brand;
	DoubleNode next;
	DoubleNode previous;
	LinkedList linkedList;

//Double linked list node Constructor 
	public DoubleNode(String brand, LinkedList linkedList) {
		this.Brand = brand;
		this.linkedList = linkedList;
	}

	// getter for brand
	public String getBrand() {
		return Brand;
	}

	// setter for brand
	public void setBrand(String location) {
		this.Brand = location;
	}

}
