package events;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPServiceProvider;

import testEsper.Constants;

public class PrcThread extends Thread {

	private static final Logger log = LoggerFactory.getLogger(PrcThread.class);

	private static PrcThread prcThread = null;

	private EPServiceProvider engine;
	private String fileName, ticker;
	private int run = 0;

	private PrcThread() {
	}

	@Override
	public void run() {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(fileName));
			String line;
			String[] sa;
			boolean blnRTH = false;
			double[] prc = new double[2];
			double prcChg, cumPrcChg = 0., signPrev = 0;

			int size, cumSize = 0;

			try {
				// skip the first line
				line = bf.readLine();
				while ((line = bf.readLine()) != null) {
					sa = line.split(",");
					prc[1] = Double.parseDouble(sa[2]);

					if (!blnRTH && sa[1].startsWith(Constants.START_TIME)) {
						blnRTH = true;
						prc[0] = prc[1];
					}

					if (blnRTH) {
						prcChg = prc[1] - prc[0];

						size = Integer.parseInt(sa[3]);

						if (!(prcChg == 0. || Math.signum(prcChg) == signPrev)) {
							run += 1;
							cumPrcChg = prcChg;
							cumSize = size;
						} else {
							cumPrcChg += prcChg;
							cumSize += size;
						}

						PrcEvent pe = new PrcEvent(ticker, sa[1], prc[1], run, prcChg, cumPrcChg);
						//log.info(pe.toString());
						engine.getEPRuntime().sendEvent(pe);
						
						SizeEvent se = new SizeEvent("ESU8", sa[1], run, size, cumSize);
						//log.info(se.toString());
						engine.getEPRuntime().sendEvent(se);
						
						
						signPrev = Math.signum(prcChg);
						prc[0] = prc[1];
						Thread.sleep(Constants.THREAD_SLEEP_TIME);
					}
				}
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (

		FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getRun() {
		return run;
	}

	public void setEngine(EPServiceProvider engine) {
		this.engine = engine;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public static PrcThread getInstance() {
		if (prcThread == null)
			prcThread = new PrcThread();

		return prcThread;

	}

}
