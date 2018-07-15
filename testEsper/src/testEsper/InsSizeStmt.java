package testEsper;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPStatement;

public class InsSizeStmt {

	private EPStatement statement;

	public InsSizeStmt(EPAdministrator admin) {
		String stmt = "insert into TickSize select symbol, timestamp, size from SizeEvent";

		statement = admin.createEPL(stmt);
	}

}
