package testEsper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import events.OrderEvent;
import events.PrcEvent;
import events.PrcThread;
import events.SizeEvent;
import events.SizeThread;
import ib.api.SimIbClient;
import strat.three.one.SePlaceListnr;

public class MainEsper implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(MainEsper.class);

	private String engineURI;
	private SimIbClient m_client;
	private static String dataFile;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length == 0) {
			System.out.println("arg <dataFile> is missing");
			System.exit(0);
		} else {
			dataFile = args[0];
		}

		new MainEsper("engine").run();

	}

	public MainEsper(String engineURI) {
		this.engineURI = engineURI;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		Configuration configuration = new Configuration();
		configuration.addEventType("PrcEvent", PrcEvent.class.getName());
		configuration.addEventType("SizeEvent", SizeEvent.class.getName());
		configuration.addEventType("OrderEvent", OrderEvent.class.getName());

		// Get engine instance
		EPServiceProvider epService = EPServiceProviderManager.getProvider(engineURI, configuration);
		EPAdministrator admin = epService.getEPAdministrator();

		m_client = new SimIbClient(epService);

		admin.createEPL("insert into TickPrice select symbol, timestamp, price, run, prcChg, cumPrcChg from PrcEvent");
		admin.createEPL("insert into TickSize select symbol, timestamp, run, size, cumSize from SizeEvent");
		admin.createEPL("insert into Orders select orderId, price, orderType, status from OrderEvent");

		EPStatement sePlaceStmt = admin.createEPL(
				"select distinct p.symbol as symbol, p.timestamp as timestamp, price, cumPrcChg, size, cumSize "
						+ "from TickPrice#lastevent() as p, "
						+ "TickSize#lastevent() as s where p.symbol=s.symbol and p.run=s.run and cumPrcChg >= 0.75 and cumSize>=300");
		sePlaceStmt.addListener(new SePlaceListnr(m_client));

		EPStatement seFillStmt = admin
				.createEPL("select symbol, timestamp, p.price as price, orderId, op.price as oPrice "
						+ "from TickPrice#lastevent() as p, Orders(orderType='SE',status='placed')#lastevent() as op "
						+ "where p.price = op.price+0.25");
		m_client.setSeFillStmt(seFillStmt);

		EPStatement sxFillStmt = admin
				.createEPL("select symbol, timestamp, p.price as price, orderId, op.price as oPrice "
						+ "from TickPrice#lastevent() as p, Orders(orderType='SX',status='placed')#lastevent() as op "
						+ "where p.price = op.price-0.25");
		m_client.setSxFillStmt(sxFillStmt);

		EPStatement seCancelStmt = admin.createEPL("select symbol, timestamp, p.price as price, orderId "
				+ "from TickPrice#lastevent() as p, Orders(orderType='SE',status='placed')#lastevent() as op "
				+ "where p.price <= (op.price-0.50)");
		m_client.setSeCancelStmt(seCancelStmt);

		EPStatement sxStopStmt = admin
				.createEPL("select symbol, timestamp, p.price as price, orderId, op.price as oPrice "
						+ "from TickPrice#lastevent() as p, Orders(orderType='SE',status='filled')#lastevent() as op "
						+ "where p.price >= (op.price+0.75)");
		m_client.setSxStopStmt(sxStopStmt);

		PrcThread pt = PrcThread.getInstance();
		pt.setEngine(epService);
		pt.setFileName(dataFile);
		pt.setTicker("ESU8");
		pt.start();

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		new SizeThread(epService, dataFile).start();

	}
}
