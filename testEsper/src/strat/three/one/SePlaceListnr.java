package strat.three.one;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import ib.api.SimIbClient;

public class SePlaceListnr implements UpdateListener {
	private SimIbClient m_client;

	public SePlaceListnr(SimIbClient client) {
		m_client = client;
	}

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		// TODO Auto-generated method stub
		if (newEvents == null) {
			return; // ignore old events for events leaving the window
		}

		EventBean theEvent = newEvents[0];

		// log.info(String.format("High price and volume detected: symbol = %s,
		// price = %.2f, size = %d",
		// theEvent.get("symbol"), theEvent.get("price"),
		// theEvent.get("size")));

		m_client.placeOrder((String) theEvent.get("symbol"), (String) theEvent.get("timestamp"),
				(double) theEvent.get("price"), (int) theEvent.get("cumSize"));

	}

}
