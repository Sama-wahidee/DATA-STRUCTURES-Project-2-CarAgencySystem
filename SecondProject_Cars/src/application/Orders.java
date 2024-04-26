package application;

import java.util.Date;

public class Orders {

	private String customerName;
	private int customerMobile;
	private Date orderDate;
	private Cars orderdCar;
	private String orderStatus;

	public Orders() {

	}

	public Orders(String customerName, int customerMobile, Cars orderdCar, Date orderDate, String orderStatus) {
		this.customerName = customerName;
		this.customerMobile = customerMobile;
		this.orderDate = orderDate;
		this.orderdCar = orderdCar;
		this.orderStatus = orderStatus;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(int customerMobile) {
		this.customerMobile = customerMobile;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Cars getOrderdCar() {
		return orderdCar;
	}

	public void setOrderdCar(Cars orderdCar) {
		this.orderdCar = orderdCar;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	
}
