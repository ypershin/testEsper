package events;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPServiceProvider;

import testEsper.Constants;

public class SizeThread extends Thread {

	private static final Logger log = LoggerFactory.getLogger(SizeThread.class);

	private EPServiceProvider engine;
	private String fileName;

	public SizeThread(EPServiceProvider engine, String fileName) {
		this.engine = engine;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(fileName));
			String line;
			String[] sa;
			boolean blnRTH = false;

			try {
				while ((line = bf.readLine()) != null) {
					sa = line.split(",");
					if (!blnRTH && sa[1].startsWith("08:30"))
						blnRTH = true;

					if (blnRTH) {
//						log.info(String.format("%s\t%s", sa[1], sa[3]));
						engine.getEPRuntime().sendEvent(new SizeEvent("ESU8", sa[1], Integer.parseInt(sa[3])));
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
}
