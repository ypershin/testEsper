package strat.three.one;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;

public class SePlaceStmt {
	private EPStatement statement;

	public SePlaceStmt(EPAdministrator admin, double prcChg) {
		String stmt = "select distinct p.symbol as symbol, p.timestamp as timestamp, price, cumPrcChg, size from TickPrice#lastevent() as p, "
				+ "TickSize#lastevent() as s where p.symbol=s.symbol and cumPrcChg >= " + prcChg;

		statement = admin.createEPL(stmt);
		
	}

	public void addListener(UpdateListener listener) {
		statement.addListener(listener);
	}

}
