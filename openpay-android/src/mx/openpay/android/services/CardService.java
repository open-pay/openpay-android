/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.android.services;

import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;

/**
 * @author Luis Delucio
 *
 */
public class CardService extends BaseService<Card, Card> {

	private static final String MERCHANT_CARDS = "cards";
	private static final String CUSTOMER_CARDS = "customers/%s/cards";

	public CardService(final String baseUrl, final String merchantId, final String apiKey) {
		super(baseUrl, merchantId, apiKey, Card.class);
	}

	public Card create(final Card card, final String customerId) throws OpenpayServiceException,
			ServiceUnavailableException {
		String resourceUrl = MERCHANT_CARDS;
		if (customerId != null && !customerId.equals("")) {
			resourceUrl = String.format(CUSTOMER_CARDS, customerId);
		}

		return this.post(resourceUrl, card);
	}

}
