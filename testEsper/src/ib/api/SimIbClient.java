package ib.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

import events.OrderEvent;
import strat.three.one.SeCancelListnr;
import strat.three.one.SeFillListnr;
import strat.three.one.SxFillListnr;
import strat.three.one.SxStopListnr;
import testEsper.Constants;

public class SimIbClient {

	private static final Logger log = LoggerFactory.getLogger(SimIbClient.class);

	private static boolean blnOrderPlaced = false;
	private EPServiceProvider engine;
	private int orderId = 1;
	private EPStatement seFillStmt = null;
	private EPStatement seCancelStmt = null;
	private EPStatement sxFillStmt = null;
	private EPStatement sxStopStmt = null;

	private double prcEntry, pnl, cumPnl = 0.;

	public SimIbClient(EPServiceProvider engine) {
		this.engine = engine;
	}

	public void placeOrder(String symbol, String timestamp, double price, int size) {
		if (!blnOrderPlaced) {
			blnOrderPlaced = true;
			// activate 'Order Fill(price+0.50)' & 'Order Cancel(price-0.25)'
			// statements
			log.info(String.format("%s\t%s\t%.2f\t%d\t - SE order %d placed @ %.2f", symbol, timestamp, price, size,
					orderId, price + 0.25));
			engine.getEPRuntime().sendEvent(new OrderEvent(orderId++, price + 0.25, "SE", "placed"));

			seFillStmt.addListener(new SeFillListnr(this));
			seCancelStmt.addListener(new SeCancelListnr(this));

			// if (orderId == 10)
			// System.exit(0);

		}
	}

	public void placeXOrder(double price) {
		if (!blnOrderPlaced) {
			blnOrderPlaced = true;
			// activate 'Order Fill(price+0.50)' & 'Order Cancel(price-0.25)'
			// statements
			log.info(String.format("%s\t%s\t%.2f\t\t - SX order %d placed @ %.2f", "ESU8", "*************",
					price + 0.25, orderId, price));
			engine.getEPRuntime().sendEvent(new OrderEvent(orderId++, price, "SX", "placed"));

			sxStopStmt.addListener(new SxStopListnr(this));
			sxFillStmt.addListener(new SxFillListnr(this));
		}
	}

	public void fillOrderSe(String symbol, String timestamp, double price, int orderId, double oPrice) {
		seFillStmt.removeAllListeners();
		seCancelStmt.removeAllListeners();
		log.info(String.format("%s\t%s\t%.2f\t\t - SE order %d filled @ %.2f", symbol, timestamp, price, orderId,
				oPrice));
		engine.getEPRuntime().sendEvent(new OrderEvent(orderId, price, "SE", "filled"));
		prcEntry = oPrice;
		blnOrderPlaced = false;
		placeXOrder(oPrice - 0.25);
	}

	public void fillOrderSx(String symbol, String timestamp, double price, int orderId, double oPrice) {
		sxFillStmt.removeAllListeners();
		sxStopStmt.removeAllListeners();
		pnl = (prcEntry - oPrice) * Constants.MULTIPLIER - Constants.COMMISSION * 2.;
		cumPnl += pnl;
		log.info(String.format("%s\t%s\t%.2f\t\t - SX order %d filled @ %.2f\tpnl %.2f\tcumPnl %.2f", symbol, timestamp,
				price, orderId, oPrice, pnl, cumPnl));
		engine.getEPRuntime().sendEvent(new OrderEvent(orderId, price, "SX", "filled"));
		blnOrderPlaced = false;
	}

	public void cancelOrder(String symbol, String timestamp, double price, int orderId) {
		seCancelStmt.removeAllListeners();
		seFillStmt.removeAllListeners();
		log.info(String.format("%s\t%s\t%.2f\t\t - SE order %d canceled @ %.2f", symbol, timestamp, price, orderId,
				price));
		engine.getEPRuntime().sendEvent(new OrderEvent(orderId, price, "SE", "canceled"));
		blnOrderPlaced = false;
		// System.exit(0);
	}

	public void stopOrder(String symbol, String timestamp, double price, int orderId, double oPrice) {
		sxStopStmt.removeAllListeners();
		sxFillStmt.removeAllListeners();
		pnl = (prcEntry - (price + 0.25)) * Constants.MULTIPLIER - Constants.COMMISSION * 2.;
		cumPnl += pnl;
		log.info(
				String.format("%s\t%s\t%.2f\t - SE order %d stopped @ %.2f, SX order canceled\tpnl %.2f\tcumPnl %.2f",
						symbol, timestamp, price, orderId, price + 0.25, pnl, cumPnl));
		engine.getEPRuntime().sendEvent(new OrderEvent(orderId, price, "SX", "stopped"));
		blnOrderPlaced = false;
		// System.exit(0);
	}

	public void setSeFillStmt(EPStatement seFillStmt) {
		this.seFillStmt = seFillStmt;
	}

	public void setSxFillStmt(EPStatement sxFillStmt) {
		this.sxFillStmt = sxFillStmt;
	}

	public void setSeCancelStmt(EPStatement seCancelStmt) {
		this.seCancelStmt = seCancelStmt;
	}

	public void setSxStopStmt(EPStatement sxStopStmt) {
		this.sxStopStmt = sxStopStmt;
	}

}
