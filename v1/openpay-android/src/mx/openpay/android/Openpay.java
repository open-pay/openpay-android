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

import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;
import mx.openpay.android.model.Token;
import mx.openpay.android.services.CardService;
import mx.openpay.android.services.ServicesFactory;
import mx.openpay.android.services.TokenService;
import android.os.AsyncTask;

/**
 * The Class OpenPay.
 */
public class Openpay {

	private static final String URL_SANDBOX = "https://sandbox-api.openpay.mx";

	private static final String URL_PRODUCTION = "https://api.openpay.mx";

	public static final String OPENPAY_MERCHANT_ID = "203000";

	/** The openpay api. */
	private CardService cardService;
	private TokenService tokenService;
	private DeviceCollectorDefaultImpl deviceCollectorDefaultImpl;

	/**
	 * Instantiates a new open pay.
	 * 
	 * @param merchantId
	 *            the merchant id
	 * @param apiKey
	 *            the api key
	 */
	public Openpay(final String merchantId, final String apiKey, final Boolean productionMode) {
		String baseUrl = URL_SANDBOX;
		if (productionMode) {
			baseUrl = URL_PRODUCTION;
		}
		ServicesFactory servicesFactory = ServicesFactory.getInstance(baseUrl, merchantId, apiKey);
		this.cardService = servicesFactory.getService(CardService.class);
		this.tokenService = servicesFactory.getService(TokenService.class);
		this.deviceCollectorDefaultImpl = new DeviceCollectorDefaultImpl(baseUrl);
	}

	/**
	 * 
	 * @param customerId
	 * @param card
	 * @return
	 */
	public void createCard(final Card card, final String customerId, final OperationCallBack<Card> operationCallBack) {
		new AsyncTask<Void, Void, OpenPayResult<Card>>() {

			@Override
			protected OpenPayResult<Card> doInBackground(final Void... params) {
				OpenPayResult<Card> openPayResult = new OpenPayResult<Card>();
				try {
					Card newCard = Openpay.this.cardService.create(card, customerId);
					openPayResult.setOperationResult(new OperationResult<Card>(newCard));
				} catch (OpenpayServiceException e) {
					openPayResult.setOpenpayServiceException(e);
				} catch (ServiceUnavailableException e) {
					openPayResult.setServiceUnavailableException(e);
				}
				return openPayResult;
			}

			protected void onPostExecute(final OpenPayResult<Card> result) {

				if (result.getOperationResult() != null) {
					operationCallBack.onSuccess(result.getOperationResult());
				} else if (result.getOpenpayServiceException() != null) {
					operationCallBack.onError(result.getOpenpayServiceException());
				} else if (result.getServiceUnavailableException() != null) {
					operationCallBack.onCommunicationError(result.getServiceUnavailableException());
				}

			}

		}.execute();

	}

	/**
	 * Creates the card.
	 * 
	 * @param cardParams
	 *            the card params
	 * @param operationCallBack
	 *            the operation call back
	 */
	public void createCard(final Card card, final OperationCallBack<Card> operationCallBack) {
		this.createCard(card, null, operationCallBack);
	}

	public void createToken(final Card card, final OperationCallBack<Token> operationCallBack) {
		new AsyncTask<Void, Void, OpenPayResult<Token>>() {

			@Override
			protected OpenPayResult<Token> doInBackground(final Void... params) {
				OpenPayResult<Token> openPayResult = new OpenPayResult<Token>();
				try {
					Token newToken = Openpay.this.tokenService.create(card);
					openPayResult.setOperationResult(new OperationResult<Token>(newToken));
				} catch (OpenpayServiceException e) {
					openPayResult.setOpenpayServiceException(e);
				} catch (ServiceUnavailableException e) {
					openPayResult.setServiceUnavailableException(e);
				}
				return openPayResult;
			}

			protected void onPostExecute(final OpenPayResult<Token> result) {

				if (result.getOperationResult() != null) {
					operationCallBack.onSuccess(result.getOperationResult());
				} else if (result.getOpenpayServiceException() != null) {
					operationCallBack.onError(result.getOpenpayServiceException());
				} else if (result.getServiceUnavailableException() != null) {
					operationCallBack.onCommunicationError(result.getServiceUnavailableException());
				}

			}

		}.execute();
	}

	public DeviceCollectorDefaultImpl getDeviceCollectorDefaultImpl() {
		return this.deviceCollectorDefaultImpl;
	}
}