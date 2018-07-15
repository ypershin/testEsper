package ib.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;

import events.OrderCanceledEvent;
import events.OrderPlacedEvent;

public class SimIbClient {

	private static final Logger log = LoggerFactory.getLogger(SimIbClient.class);

	private static boolean blnOrderPlaced = false;
	private EPServiceProvider engine;
	private int orderId = 1;

	public SimIbClient(EPServiceProvider engine) {
		this.engine = engine;
	}

	public void placeOrder(String symbol, String timestamp, double price, int size) {
		if (!blnOrderPlaced) {
			blnOrderPlaced = true;
			// activate 'Order Fill(price+0.50)' & 'Order Cancel(price-0.25)'
			// statements
			log.info(String.format("%s\t%s\t%.2f\t%d\t - order %d placed @ %.2f", symbol, timestamp, price, size,
					orderId, price + 0.25));
			engine.getEPRuntime().sendEvent(new OrderPlacedEvent(orderId++, price));

		}
	}

	public void cancelOrder(int orderId, double price) {
		log.info("order " + orderId + " cancelled, price " + price);
		engine.getEPRuntime().sendEvent(new OrderCanceledEvent(orderId, price));
		// System.exit(0);
	}

}
