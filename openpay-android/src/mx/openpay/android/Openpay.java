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
import mx.openpay.client.core.HttpServiceClient;
import mx.openpay.client.core.JsonSerializer;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.impl.DefaultHttpServiceClient;
import mx.openpay.client.core.impl.DefaultSerializer;
import mx.openpay.client.core.operations.CardOperations;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import android.os.AsyncTask;

/**
 * The Class OpenPay.
 */
public class Openpay {
	
	private static final String URL_SANDBOX = "https://sandbox-api.openpay.mx/v1/";
	
	private static final String URL_PRODUCTION = "https://api.openpay.mx/v1/";
	
	/** The openpay api. */
	private CardOperations cardOperations;
	
	/**
	 * Instantiates a new open pay.
	 *
	 * @param merchantId the merchant id
	 * @param apiKey the api key
	 */
	public Openpay(final String merchantId, final String apiKey, final Boolean productionMode) {
		String url = URL_SANDBOX;
		
		if (productionMode) {
			url = URL_PRODUCTION;
		}
		
		JsonSerializer serializer = new DefaultSerializer();
		HttpServiceClient httpClient = new DefaultHttpServiceClient(false);
		JsonServiceClient jsonServiceClient = new JsonServiceClient(url, merchantId, apiKey, serializer , httpClient);
		this.cardOperations = new CardOperations(jsonServiceClient);
	}

	
	 /**
	  * 
	 * @param customerId
	 * @param card
	 * @return
	 */
	public void createCard(final Card card, final String customerId, final OperationCallBack operationCallBack){
		new AsyncTask<Void, Void, OpenPayResult>() {

			@Override
			protected OpenPayResult doInBackground(final Void... params) {
				OpenPayResult openPayResult = new OpenPayResult();
				try {
					Card newCard;
					
					if (customerId == null) {
						newCard = Openpay.this.cardOperations.create(card);
					} else {
						newCard = Openpay.this.cardOperations.create(customerId, card);
					}
					
					openPayResult.setOperationResult(new OperationResult(newCard));
				} catch (OpenpayServiceException e) {
					openPayResult.setOpenpayServiceException(e);
				} catch (ServiceUnavailableException e) {
					openPayResult.setServiceUnavailableException(e);
				}
				return openPayResult;
			}
			
			 protected void onPostExecute(final OpenPayResult result) {
				 
				 if (result.getOperationResult() != null){
					 operationCallBack.onSuccess(result.getOperationResult());
				 } else  if (result.getOpenpayServiceException() != null) {
					 operationCallBack.onError(result.getOpenpayServiceException());
				 } else  if (result.getServiceUnavailableException() != null) {
					 operationCallBack.onCommunicationError(result.getServiceUnavailableException());
				 }  
				 
            }
			
		}.execute();
		
	
	 }
	
	/**
	 * Creates the card.
	 *
	 * @param cardParams the card params
	 * @param operationCallBack the operation call back
	 */
	public void createCard(final Card card, final OperationCallBack operationCallBack){
		this.createCard(card, null, operationCallBack);
	}
}

class OpenPayResult {
	
	private OpenpayServiceException openpayServiceException;
	
	private OperationResult operationResult;
	
	private ServiceUnavailableException serviceUnavailableException;

	public OpenpayServiceException getOpenpayServiceException() {
		return openpayServiceException;
	}

	public void setOpenpayServiceException(
			OpenpayServiceException openpayServiceException) {
		this.openpayServiceException = openpayServiceException;
	}

	public OperationResult getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(OperationResult operationResult) {
		this.operationResult = operationResult;
	}

	public ServiceUnavailableException getServiceUnavailableException() {
		return serviceUnavailableException;
	}

	public void setServiceUnavailableException(
			ServiceUnavailableException serviceUnavailableException) {
		this.serviceUnavailableException = serviceUnavailableException;
	}
}
