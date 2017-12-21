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
package mx.openpay.android.example;

import mx.openpay.android.Openpay;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Example Headless Fragement implementation for API 11 and higher.
 * 
 * @author Kount <custserv@kount.com>
 * @version SVN: $Id: CheckoutFragment.java 24966 2013-12-18 16:55:23Z gjd $
 * @copyright 2013 Kount, Inc. All Rights Reserved.
 */
@TargetApi (Build.VERSION_CODES.KITKAT)
public class DeviceIdFragment extends Fragment {

  /**
   * Fragment initialization. We want to be retained.
   */
  @Override
  public void onCreate (final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.setRetainInstance(true);
  }


  /**
   * Handle the checkout
   *
   */
	public String getDeviceId() {
		Openpay openpay = ((OpenPayAppExample) this.getActivity().getApplication()).getOpenpay();
		String deviceIdString = openpay.getDeviceCollectorDefaultImpl().setup(this.getActivity());
		if (deviceIdString == null) {
			this.printMsg(openpay.getDeviceCollectorDefaultImpl().getErrorMessage());
		} else {
			this.printMsg(deviceIdString);
		}

		return deviceIdString;

	}


	/*
	 * Debug messages. Send to the view and to the logs.
	 * 
	 * @param message The message to pass to the view and logs
	 */
	private void printMsg(final String msg) {

		if (this.getActivity() != null) {
			this.getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Log.d("AddCardActivity", msg);
					TextView tv = (TextView) DeviceIdFragment.this.getActivity().findViewById(R.id.textView3);
					tv.append(msg);
				}
			});
		}
	}
}
