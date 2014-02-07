/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.android.test;

import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Address;
import mx.openpay.android.model.Card;
import mx.openpay.android.model.Token;
import mx.openpay.android.services.ServicesFactory;
import mx.openpay.android.services.TokenService;

import org.junit.After;
import org.junit.Test;

import android.test.InstrumentationTestCase;

/**
 * @author Luis Delucio
 * 
 */
public class TokenServicesTest extends InstrumentationTestCase {

	private static final String URL_SANDBOX = "https://sandbox-api.openpay.mx";
	private TokenService tokenService;

	@After
	protected void setUp() throws Exception {
		super.setUp();
		this.tokenService = ServicesFactory.getInstance(URL_SANDBOX, "mh9ovicn5oqq2wgpyo1v",
				"pk_4f007c56b11e46189100740e593ee747").getService(TokenService.class);
	}

	@Test
	public void createMerchantCard() throws OpenpayServiceException, ServiceUnavailableException {

		Token newCard = this.tokenService.create(this.getCard());

		assertNotNull(newCard);
		assertNotNull(newCard.getCard());
	}

	@Test
	public void createMerchantCardWithAddress() throws OpenpayServiceException, ServiceUnavailableException {

		Card card = this.getCard();
		card.address(this.getAddres());
		Token newCard = this.tokenService.create(card);

		assertNotNull(newCard);
		assertNotNull(newCard.getCard());
		assertNotNull(newCard.getCard().getAddress());
	}

	@Test
	public void createMerchantCard_ErrorDataCard() throws ServiceUnavailableException {
		Card card = this.getCard();
		card.expirationYear(12);
		try {
			this.tokenService.create(card);
		} catch (OpenpayServiceException e) {
			assertNotNull(e.getDescription());
			assertNotNull(e.getRequestId());
			assertNotNull(e.getCategory());
			assertNotNull(e.getHttpCode());
			assertNotNull(e.getErrorCode());
		}
	}

	private Address getAddres() {
		return new Address().city("Mexico").countryCode("MX").line1("Calle de la felicidad").line2("Numero 20")
				.line3("Col ensueño").postalCode("76900").state("Mexico");
	}

	private Card getCard() {
		Card card = new Card().cardNumber("4111111111111111").holderName("Pedro Paramo").expirationMonth(2)
				.expirationYear(15).cvv2("123");
		return card;
	}

}
