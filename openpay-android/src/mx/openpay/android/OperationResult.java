/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
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
