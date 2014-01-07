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

import mx.openpay.client.Card;

/**
 * The Class OperationResult.
 */
public class OperationResult {

	/** The card. */
	private Card card;
	
	
	/**
	 * Instantiates a new operation result.
	 *
	 * @param card the card
	 */
	public OperationResult(final Card card) {
		this.card = card;
	}

	/**
	 * Gets the card.
	 *
	 * @return the card
	 */
	public Card getCard() {
		return card;
	}
}
