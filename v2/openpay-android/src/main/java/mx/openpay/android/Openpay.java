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

    /**
     * The openpay api.
     */
    private CardService cardService;
    private TokenService tokenService;
    private DeviceCollectorDefaultImpl deviceCollectorDefaultImpl;

    /**
     * Instantiates a new open pay.
     *
     * @param merchantId the merchant id
     * @param apiKey     the api key
     */
    public Openpay(final String merchantId, final String apiKey, final Boolean productionMode) {
        String baseUrl = URL_SANDBOX;
        if (productionMode) {
            baseUrl = URL_PRODUCTION;
        }
        ServicesFactory servicesFactory = ServicesFactory.getInstance(baseUrl, merchantId, apiKey);
        this.cardService = servicesFactory.getService(CardService.class);
        this.tokenService = servicesFactory.getService(TokenService.class);
        this.deviceCollectorDefaultImpl = new DeviceCollectorDefaultImpl(baseUrl, merchantId);
    }

    /**
     * Creates the card.
     *
     * @param card              the card params
     * @param operationCallBack the operation call back
     */
    public void createCard(final Card card, final OperationCallBack<Card> operationCallBack) {
        this.createCard(card, null, operationCallBack);
    }

    /**
     * @param card
     * @param customerId
     * @param operationCallBack
     */
    public void createCard(final Card card, final String customerId, final OperationCallBack<Card> operationCallBack) {
        CardAsyncTask cardAsyncTask = new CardAsyncTask(card, customerId, operationCallBack, this.cardService);
        cardAsyncTask.execute();
    }

    private class CardAsyncTask extends AsyncTask<Void, Void, OpenPayResult<Card>> {

        private Card card;

        private String customerId;

        private OperationCallBack<Card> operationCallBack;

        private CardService cardService;

        public CardAsyncTask(final Card card, final String customerId, final OperationCallBack<Card> operationCallBack, final CardService cardService) {
            this.card = card;
            this.customerId = customerId;
            this.operationCallBack = operationCallBack;
            this.cardService = cardService;
        }

        @Override
        protected OpenPayResult<Card> doInBackground(Void... voids) {
            OpenPayResult<Card> openPayResult = new OpenPayResult<Card>();
            try {
                Card newCard = this.cardService.create(this.card, this.customerId);
                openPayResult.setOperationResult(new OperationResult<Card>(newCard));
            } catch (OpenpayServiceException e) {
                openPayResult.setOpenpayServiceException(e);
            } catch (ServiceUnavailableException e) {
                openPayResult.setServiceUnavailableException(e);
            }
            return openPayResult;
        }

        @Override
        protected void onPostExecute(final OpenPayResult<Card> result) {
            if (result.getOperationResult() != null) {
                this.operationCallBack.onSuccess(result.getOperationResult());
            } else if (result.getOpenpayServiceException() != null) {
                this.operationCallBack.onError(result.getOpenpayServiceException());
            } else if (result.getServiceUnavailableException() != null) {
                this.operationCallBack.onCommunicationError(result.getServiceUnavailableException());
            }
        }

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
