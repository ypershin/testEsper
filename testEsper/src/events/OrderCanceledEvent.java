package events;

public class OrderCanceledEvent {

	private int orderId;
	private double price;

	public OrderCanceledEvent(int orderId, double price) {
		this.orderId = orderId;
		this.price = price;
	}

	public int getOrderId() {
		return orderId;
	}

	public double getPrice() {
		return price;
	}

}
