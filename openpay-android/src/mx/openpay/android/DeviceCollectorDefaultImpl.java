/*
 * Copyright 2013 Opencard Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.openpay.android;

import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.util.Log;

import com.devicecollector.DeviceCollector;
import com.devicecollector.DeviceCollector.ErrorCode;

/**
 * @author Luis Delucio
 *
 */
public class DeviceCollectorDefaultImpl implements DeviceCollector.StatusListener {

	private DeviceCollector dc;
	private Date startTime = new Date();
	private boolean running = false;
	private boolean finished = false;
	private String sessionId;
	private boolean hasError = false;
	private String errorMessage;
	private String baseUrl;
	private Long cancelSetupTime = 10000L;

	public DeviceCollectorDefaultImpl(final String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public String setup(final Activity activity, final DeviceCollector.StatusListener statusListener) {
		if (this.sessionId == null || this.sessionId.equals("")) {
			this.sessionId = UUID.randomUUID().toString();
			this.sessionId = this.sessionId.replace("-", "");
		}
		if(this.dc == null) {
			this.dc = new DeviceCollector(activity);
		}
		if (statusListener != null) {
			this.dc.setStatusListener(statusListener);
			this.collect();
		} else {
			if (!this.running) {
				if (!this.finished || this.hasError) {
					this.dc.setStatusListener(this);
					this.collect();
				} else {
					this.debug("Already completed for this transaction. Why are you trying to run again?\n");
				}
			} else {
				this.debug("Already running\n");
				long totalTime = this.getTotalTime();
				if (totalTime >= this.cancelSetupTime) {
					this.cancelSetup();
				}
			}

		}
		return this.sessionId;
	}

	public String setup(final Activity activity) {
		return this.setup(activity, null);
	}

	private void collect() {
		this.dc.setMerchantId(Openpay.OPENPAY_MERCHANT_ID);
		this.dc.setCollectorUrl(this.baseUrl + "/logo.htm");
		this.debug("Collect with sessionid [" + this.sessionId + "]\n");
		// Start collecting
		this.startTime = new Date();
		this.dc.collect(this.sessionId);
	}

	/**
	 * Tell the library to stop immediately.
	 */
	public void cancelSetup() {
		if (!this.finished && this.running && null != this.dc) {
			this.debug("Trying to stop...\n");
			this.dc.stopNow();
			this.debug("Stopping complete!");
		} else {
			this.debug("Nothing to stop\n");
		}
	} // end stopNow ()

	@Override
	public void onCollectorError(final ErrorCode code, final Exception ex) {
		long totalTime = this.getTotalTime();
		this.hasError = true;
		this.finished = true;
		if (null != ex) {
			if (code.equals(ErrorCode.MERCHANT_CANCELLED)) {
				this.debug("Merchant Cancelled\n");
			} else {
				this.errorMessage = "Collector Failed in (" + totalTime + ") ms. It had an error [" + code + "]:"
						+ ex.getMessage();
				this.sessionId = null;
				this.debug(this.errorMessage);
				this.debug("Stack Trace:");
				for (StackTraceElement element : ex.getStackTrace()) {
					this.debug(element.getClassName() + " " + element.getMethodName() + "(" + element.getLineNumber()
							+ ")");
				}
			}
		} else {
			this.debug("Collector failed in (" + totalTime + ") ms. It had an error [" + code + "]:");
		}

	}

	@Override
	public void onCollectorStart() {
		long totalTime = this.getTotalTime();
		this.debug("Starting collector (" + totalTime + ")ms....\n");
		this.running = true;
	}

	@Override
	public void onCollectorSuccess() {
		long totalTime = this.getTotalTime();
		this.debug("Collector finished successfully in (" + totalTime + ") ms\n");
		this.running = false;
		this.finished = true;
	}

	private long getTotalTime() {
		Date stopTime = new Date();
		return stopTime.getTime() - this.startTime.getTime();
	}

	private void debug(final String string) {
		Log.d(this.getClass().getName(), string);
	}

	public Long getCancelSetupTime() {
		return this.cancelSetupTime;
	}

	public void setCancelSetupTime(final Long cancelSetupTime) {
		this.cancelSetupTime = cancelSetupTime;
	}
}

