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
@TargetApi (Build.VERSION_CODES.HONEYCOMB)
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
   * @param view
   */
	public String getDeviceId() {
		Openpay openpay = ((OpenPayAppExample) this.getActivity().getApplication()).getOpenpay();
		String deviceIdString = openpay.getDeviceCollectorDefaultImpl().getDeviceId(this.getActivity());
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
