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
package mx.openpay.android.model;

import com.google.api.client.util.Key;

public class Token {

	@Key
	private String id;

	@Key
	private Card card;

	@Override
	public String toString() {
		return String.format("Token [id=%s, card=%s]", this.id, this.card);
	}

	public Token id(final String id) {
		this.id = id;
		return this;
	}

	public Token card(final Card card) {
		this.card = card;
		return this;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Card getCard() {
		return this.card;
	}

	public void setCard(final Card card) {
		this.card = card;
	}

}
