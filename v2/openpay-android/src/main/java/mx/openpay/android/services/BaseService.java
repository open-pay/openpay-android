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
	private final static JsonFactory JSON_FACTORY = new JacksonFactory();
	private final static HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
	private final static String EMPTY_PASSWORD = "";
	private final static String API_VERSION = "v1";
	private final static String URL_SEPARATOR = "/";
	private static final String AGENT = "openpay-android/";

	private static final int DEFAULT_CONNECTION_TIMEOUT = 60000;

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
		return HTTP_TRANSPORT.createRequestFactory(new OpenpayHttpRequestInitializer());
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

	private class OpenpayHttpRequestInitializer implements HttpRequestInitializer {
		@Override
		public void initialize(final HttpRequest request) {
			request.setParser(new JsonObjectParser(JSON_FACTORY));
			request.getHeaders().setBasicAuthentication(BaseService.this.apiKey, EMPTY_PASSWORD);
			String version = this.getClass().getPackage().getImplementationVersion();
			if (version == null) {
				version = "1.0.1-UNKNOWN";
			}
			request.setSuppressUserAgentSuffix(true);
			request.getHeaders().setUserAgent(AGENT + version);
			request.setUnsuccessfulResponseHandler(new ServiceUnsuccessfulResponseHandler());
			request.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);
			request.setReadTimeout(DEFAULT_CONNECTION_TIMEOUT);
		}

		private class ServiceUnsuccessfulResponseHandler implements HttpUnsuccessfulResponseHandler {
			@Override
			public boolean handleResponse(final HttpRequest request, final HttpResponse response, final boolean arg2)
					throws IOException {
				OpenpayServiceException exception = new JsonObjectParser(BaseService.JSON_FACTORY).parseAndClose(
						response.getContent(), response.getContentCharset(), OpenpayServiceException.class);
				throw exception;
			}

		}
	}

}


