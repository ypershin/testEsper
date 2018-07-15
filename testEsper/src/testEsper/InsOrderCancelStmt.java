package testEsper;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;

public class InsOrderCancelStmt {
	private EPStatement statement;

	public InsOrderCancelStmt(EPAdministrator admin) {
		String stmt = "insert into OrderCanceled select orderId, price from OrderCanceledEvent";

		statement = admin.createEPL(stmt);
	}

	// public void addListener(UpdateListener listener) {
	// statement.addListener(new PriceListener());
	// }

}
