package com.chtr.tmoauto.T3;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.chtr.tmoauto.logging.Logging;



/**
 * 
 * @author Gaurav Kumar
 * @company Infosys Ltd.
 * @since Nov 17, 2015
 * 
 */
public class Timer_T3 {

	private transient boolean isRunning = false;
	private transient int iStartTime = 0;
	private transient int iPollingTime = 10;
	private transient long lnInTime, lnOutTime, lnTotalTime, lnHours, lnMins, lnSecs;
	private transient String strTimeIn, strTimeOut, strTotalTime;
	Logging log = new Logging();
    String Hrs = " Hrs ";
    String Min = " Min ";
    String Sec = " Sec";
     

	Logging logmanager = new Logging();

	public  Timer_T3 getInstance() {
		return new Timer_T3();
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(final boolean running) {
		this.isRunning = running;
	}

	public int getStartTime() {
		return iStartTime;
	}

	public void setStartTime(final int startTime) {
		this.iStartTime = startTime;
	}

	public int getPollingTime() {
		return iPollingTime;
	}

	public void setPollingTime(final int pollingTime) {
		this.iPollingTime = pollingTime;
	}

	public boolean isTimeout(final int stopTime) throws InterruptedException {
		if (iStartTime >= stopTime) {
			isRunning = true;
		}
		iStartTime += iPollingTime;
		return isRunning;
	}

	public void delay(final int pollingTime) throws InterruptedException {
		Thread.sleep(pollingTime * 1000);
	}

	public void inTime() {
		final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		final Date time1 = new Date();
		strTimeIn = dateFormat.format(time1);
		lnInTime = System.currentTimeMillis();

	}

	public void outTime() throws InterruptedException, IOException {
		final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		final Date time1 = new Date();
		strTimeOut = dateFormat.format(time1);
		lnOutTime = System.currentTimeMillis();
		lnTotalTime = (lnOutTime - lnInTime);
		lnSecs = lnTotalTime / 1000;
		lnMins = lnSecs / 60;
		lnHours = lnMins / 60;
		lnSecs = lnSecs - lnMins * 60;
		lnMins = lnMins - lnHours * 60;

		strTotalTime = Long.toString(lnHours).concat(Hrs).concat(
				Long.toString(lnMins)).concat(Min).concat(
				Long.toString(lnSecs)).concat(Sec);
	log.fw_writeLogEntry("Exit time: " + strTimeOut,"NA");
	log.fw_writeLogEntry("Total time: " + strTotalTime,"NA");
	
	}

	public String getStrTotalTime() {
		return strTotalTime;
	}

	public void setStrTotalTime(final String strTotalTime) {
		this.strTotalTime = strTotalTime;
	}

	

}
