package events;

public class SizeEvent {

	private String symbol;
	private String timestamp;
	private int run, size, cumSize;

	public SizeEvent(String symbol, String timestamp, int run, int size, int cumSize) {
		this.symbol = symbol;
		this.timestamp = timestamp;
		this.run = run;
		this.size = size;
		this.cumSize = cumSize;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public int getRun() {
		return run;
	}

	public int getSize() {
		return size;
	}

	public int getCumSize() {
		return cumSize;
	}

	@Override
	public String toString() {
		return "SizeEvent [symbol=" + symbol + ", timestamp=" + timestamp + ", run=" + run + ", size=" + size
				+ ", cumSize=" + cumSize + "]";
	}

}
