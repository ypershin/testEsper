package strat.three.one;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;

public class SeCancelStmt {

	private EPStatement statement;

	public SeCancelStmt(EPAdministrator admin) {
		// on OrderPlacedEvent as ope set var_cancel_price = ope.price-0.50;
		String stmt = "select timestamp, p.price as price, orderId from TickPrice#lastevent() as p, OrderPlaced#lastevent() as op"
				+ " where p.price <= (op.price-0.50)";

		statement = admin.createEPL(stmt);
	}

	public void addListener(UpdateListener listener) {
		statement.addListener(listener);
	}

}
