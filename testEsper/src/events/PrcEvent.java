package events;

public class PrcEvent {

	private String symbol;
	private String timestamp;
	private double price;
	private int run;
	private double prcChg, cumPrcChg;

	public PrcEvent(String symbol, String timestamp, double price, int run, double prcChg, double cumPrcChg) {
		this.symbol = symbol;
		this.timestamp = timestamp;
		this.price = price;
		this.run = run;
		this.prcChg = prcChg;
		this.cumPrcChg = cumPrcChg;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public double getPrice() {
		return price;
	}

	public int getRun() {
		return run;
	}

	public double getPrcChg() {
		return prcChg;
	}

	public double getCumPrcChg() {
		return cumPrcChg;
	}

	@Override
	public String toString() {
		return "PrcEvent [symbol=" + symbol + ", timestamp=" + timestamp + ", price=" + price + ", run=" + run
				+ ", prcChg=" + prcChg + ", cumPrcChg=" + cumPrcChg + "]";
	}


}
