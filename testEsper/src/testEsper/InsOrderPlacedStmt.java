package testEsper;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;

public class InsOrderPlacedStmt {
	private EPStatement statement;

	public InsOrderPlacedStmt(EPAdministrator admin) {
		String stmt = "insert into OrderPlaced select orderId, price from OrderPlacedEvent";

		statement = admin.createEPL(stmt);
	}

	// public void addListener(UpdateListener listener) {
	// statement.addListener(new PriceListener());
	// }

}
