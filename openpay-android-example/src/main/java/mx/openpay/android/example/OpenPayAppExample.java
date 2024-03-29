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
import android.app.Application;

public class OpenPayAppExample extends Application {

	private final Openpay openpay;
	
	public OpenPayAppExample() {

		this.openpay = new Openpay("m2tmftuv5jao96rrezj2", "pk_d5e9bff37db4468da3f80148bb94f263", false);
	}
	
	
	public Openpay getOpenpay() {
		return this.openpay;
	}

}
