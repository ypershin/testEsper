package testEsper;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;

public class InsPrcStmt {
	private EPStatement statement;

	public InsPrcStmt(EPAdministrator admin) {
		String stmt = "insert into TickPrice select symbol, timestamp, price, run, prcChg, cumPrcChg from PrcEvent";

		statement = admin.createEPL(stmt);
	}

	// public void addListener(UpdateListener listener) {
	// statement.addListener(new PriceListener());
	// }

}
