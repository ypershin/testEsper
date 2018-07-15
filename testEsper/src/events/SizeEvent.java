package events;

public class SizeEvent {

	private String symbol;
	private String timestamp;
	private int size;

	public SizeEvent(String symbol, String timestamp, int size) {
		this.symbol = symbol;
		this.size = size;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public int getSize() {
		return size;
	}
}
