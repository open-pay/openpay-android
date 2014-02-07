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


/**
 * The Class OperationResult.
 */
public class OperationResult<T> {

	/** The card. */
	private T result;
	
	
	/**
	 * Instantiates a new operation result.
	 *
	 * @param card the card
	 */
	public OperationResult(final T result) {
		this.result = result;
	}

	/**
	 * Gets the card.
	 *
	 * @return the card
	 */
	public T getResult() {
		return this.result;
	}
}
