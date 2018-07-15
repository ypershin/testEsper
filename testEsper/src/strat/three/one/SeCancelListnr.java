package strat.three.one;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import ib.api.SimIbClient;

public class SeCancelListnr implements UpdateListener {
	private SimIbClient m_client;

	public SeCancelListnr(SimIbClient client) {
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

		m_client.cancelOrder((int) theEvent.get("orderId"), (double) theEvent.get("price"));

	}

}
