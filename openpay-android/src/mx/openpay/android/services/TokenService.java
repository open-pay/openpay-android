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
import mx.openpay.android.model.Token;

/**
 * @author Luis Delucio
 *
 */
public class TokenService extends BaseService<Card, Token> {
	private static final String MERCHANT_TOKENS = "tokens";

	public TokenService(final String baseUrl, final String merchantId, final String apiKey) {
		super(baseUrl, merchantId, apiKey, Token.class);
	}

	public Token create(final Card card) throws OpenpayServiceException, ServiceUnavailableException {
		return this.post(MERCHANT_TOKENS, card);
	}

}
