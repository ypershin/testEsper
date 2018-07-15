package testEsper;

public class Constants {
	public static final String[] ticker = { "IBM", "MSFT" };
	public static final long THREAD_SLEEP_TIME = 5;

	public static final String START_TIME = "07:30";

	public static enum OrderType {
		LE, LX, SE, SX
	};

	public static final double COMMISSION = 2.05;
	public static final double MULTIPLIER = 50.;

}
