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

import java.util.concurrent.CountDownLatch;

import mx.openpay.android.Openpay;
import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Address;
import mx.openpay.android.model.Card;

import org.junit.After;
import org.junit.Test;

import android.test.InstrumentationTestCase;
import android.util.Log;

/**
 * @author Luis Delucio
 *
 */
@SuppressWarnings("unchecked")
public class OpenpayTest extends InstrumentationTestCase {

	Openpay openpay;
	CountDownLatch signal;

	@After
	protected void setUp() throws Exception {
		super.setUp();
		this.signal = new CountDownLatch(1);
		this.openpay = new Openpay("mh9ovicn5oqq2wgpyo1v", "pk_4f007c56b11e46189100740e593ee747", false);
	}


	@Test
	public void createCard() throws InterruptedException {
		this.openpay.createCard(this.getCard(), new LocalOperationCallBack());
		this.signal.await();
	}

	@Test
	public void createCardWithAddress() throws InterruptedException {
		Card card = this.getCard();
		card.address(this.getAddres());
		this.openpay.createCard(card, new LocalOperationCallBack());
		this.signal.await();
	}

	@Test
	public void createToken() throws InterruptedException {
		this.openpay.createToken(this.getCard(), new LocalOperationCallBack());
		this.signal.await();
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

	@SuppressWarnings("rawtypes")
	private class LocalOperationCallBack implements OperationCallBack {

		@Override
		public void onError(final OpenpayServiceException error) {
			Log.d(OpenpayTest.class.getName(), error.description);
			OpenpayTest.this.signal.countDown();
		}

		@Override
		public void onCommunicationError(final ServiceUnavailableException error) {
			Log.d(OpenpayTest.class.getName(), error.getMessage());
			OpenpayTest.this.signal.countDown();
		}

		@Override
		public void onSuccess(final OperationResult operationResult) {
			Log.d(OpenpayTest.class.getName(), operationResult.getResult().toString());
			assertNotNull(operationResult);
			OpenpayTest.this.signal.countDown();
		}

	}
}
