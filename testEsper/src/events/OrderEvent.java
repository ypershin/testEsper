package events;

public class OrderEvent {

	private int orderId;
	private double price;
	private String orderType;
	private String status;

	public OrderEvent(int orderId, double price, String orderType, String status) {
		this.orderId = orderId;
		this.price = price;
		this.orderType = orderType;
		this.status = status;
	}

	public int getOrderId() {
		return orderId;
	}

	public double getPrice() {
		return price;
	}

	public String getOrderType() {
		return orderType;
	}

	public String getStatus() {
		return status;
	}

}
