/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.android.example;

import mx.openpay.android.Openpay;
import android.app.Application;

public class OpenPayAppExample extends Application {

	private final Openpay openpay;
	
	public OpenPayAppExample() {

		this.openpay = new Openpay("mh9ovicn5oqq2wgpyo1v", "pk_4f007c56b11e46189100740e593ee747", false);
	}
	
	
	public Openpay getOpenpay() {
		return this.openpay;
	}

}
