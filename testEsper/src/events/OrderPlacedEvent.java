package events;

public class OrderPlacedEvent {

	private int orderId;
	private double price;

	public OrderPlacedEvent(int orderId, double price) {
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
