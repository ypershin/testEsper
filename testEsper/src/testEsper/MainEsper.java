package testEsper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

import events.OrderCanceledEvent;
import events.OrderPlacedEvent;
import events.PrcEvent;
import events.SizeEvent;
import events.SizeThread;
import events.PrcThread;
import ib.api.SimIbClient;
import strat.three.one.SeCancelListnr;
import strat.three.one.SeCancelStmt;
import strat.three.one.SePlaceListnr;
import strat.three.one.SePlaceStmt;

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
		configuration.addEventType("OrderPlacedEvent", OrderPlacedEvent.class.getName());
		configuration.addEventType("OrderCanceledEvent", OrderCanceledEvent.class.getName());

		// Get engine instance
		EPServiceProvider epService = EPServiceProviderManager.getProvider(engineURI, configuration);

		m_client = new SimIbClient(epService);

		// epService.getEPAdministrator().getConfiguration().addVariable("var_cancel_price",
		// Double.class, 0.0);

		new InsPrcStmt(epService.getEPAdministrator());
		// PriceStmt stmtPrice = new PriceStmt(epService.getEPAdministrator());
		// stmtPrice.addListener(new PriceListener());

		new InsSizeStmt(epService.getEPAdministrator());

		new InsOrderPlacedStmt(epService.getEPAdministrator());
		new InsOrderCancelStmt(epService.getEPAdministrator());
		epService.getEPRuntime().sendEvent(new OrderCanceledEvent(0, 0.0));

		SePlaceStmt sePlaceStmt = new SePlaceStmt(epService.getEPAdministrator(), 0.75);
		sePlaceStmt.addListener(new SePlaceListnr(m_client));

		SeCancelStmt seCancelStmt = new SeCancelStmt(epService.getEPAdministrator());
		seCancelStmt.addListener(new SeCancelListnr(m_client));

		new PrcThread(epService, dataFile, "ESU8").start();
		new SizeThread(epService, dataFile).start();

	}
}
