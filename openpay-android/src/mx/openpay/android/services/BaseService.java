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

import java.io.IOException;

import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;


/**
 * @author Luis Delucio
 *
 */
public abstract class BaseService<V, T> {
	public final static JsonFactory JSON_FACTORY = new JacksonFactory();
	private final static HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
	private final static String EMPTY_PASSWORD = "";
	private final static String API_VERSION = "v1";
	private final static String URL_SEPARATOR = "/";

	protected String baseUrl;
	protected String merchantId;
	protected String apiKey;

	protected Class<T> clazz;

	public BaseService(final String baseUrl, final String merchantId, final String apiKey, final Class<T> clazz) {
		this.merchantId = merchantId;
		this.apiKey = apiKey;
		this.clazz = clazz;
		this.baseUrl = baseUrl;
	}

	public HttpRequestFactory getRequestFactory() {
		return HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(final HttpRequest request) {
				request.setParser(new JsonObjectParser(JSON_FACTORY));
				request.getHeaders().setBasicAuthentication(BaseService.this.apiKey, EMPTY_PASSWORD);
				request.getHeaders().setUserAgent("OpenPay-");
				request.setUnsuccessfulResponseHandler(new ServiceUnsuccessfulResponseHandler());
			}
		});
	}

	public GenericUrl getGenericUrl(final String resourceUrl) {
		StringBuilder urlBuilder = new StringBuilder(this.baseUrl).append(URL_SEPARATOR).append(API_VERSION)
				.append(URL_SEPARATOR).append(this.merchantId).append(URL_SEPARATOR)
				.append(resourceUrl);
		return new GenericUrl(urlBuilder.toString());
	}

	public HttpContent getContent(final V jsonData) {
		return new JsonHttpContent(JSON_FACTORY, jsonData);
	}

	public T post(final String resourceUrl, final V data) throws OpenpayServiceException,
			ServiceUnavailableException {
		try {
			HttpRequest request = this.getRequestFactory().buildPostRequest(this.getGenericUrl(resourceUrl),
					this.getContent(data));
			T newObject = request.execute().parseAs(this.clazz);
			return newObject;
		} catch (IOException e) {
			if (e instanceof OpenpayServiceException) {
				throw (OpenpayServiceException) e;
			}
			throw new ServiceUnavailableException(e);
		}
	}

}

class ServiceUnsuccessfulResponseHandler implements HttpUnsuccessfulResponseHandler {
	@Override
	public boolean handleResponse(final HttpRequest request, final HttpResponse response, final boolean arg2)
			throws IOException {
		OpenpayServiceException exception = new JsonObjectParser(BaseService.JSON_FACTORY).parseAndClose(
				response.getContent(), response.getContentCharset(), OpenpayServiceException.class);
		throw exception;
	}

}
