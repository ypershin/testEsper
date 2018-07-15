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

	private EPServiceProvider engine;
	private String fileName, ticker;
	private int run = 0;

	public PrcThread(EPServiceProvider engine, String fileName, String ticker) {
		this.engine = engine;
		this.fileName = fileName;
		this.ticker = ticker;
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

			try {
				// skip the first line
				line = bf.readLine();
				while ((line = bf.readLine()) != null) {
					sa = line.split(",");
					prc[1] = Double.parseDouble(sa[2]);

					if (!blnRTH && sa[1].startsWith("07:30")) {
						blnRTH = true;
						prc[0] = prc[1];
					}

					if (blnRTH) {
						prcChg = prc[1] - prc[0];
						if (!(prcChg == 0. || Math.signum(prcChg) == signPrev)) {
							run += 1;
							cumPrcChg = prcChg;
						} else
							cumPrcChg += prcChg;

						PrcEvent pe = new PrcEvent(ticker, sa[1], prc[1], run, prcChg, cumPrcChg);
						log.info(pe.toString());
						engine.getEPRuntime().sendEvent(pe);
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
	
	
}
