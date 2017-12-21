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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Luis Delucio
 *
 */
public class ServicesFactory {

	private static volatile ServicesFactory INSTANCE = null;

	private String baseUrl;
	private String merchantId;
	private String apiKey;
	
	private ServicesFactory(final String baseUrl,
			final String merchantId, final String apiKey) {
		this.baseUrl = baseUrl;
		this.merchantId = merchantId;
		this.apiKey = apiKey;
	}
	
	public static ServicesFactory getInstance(final String baseUrl, final String merchantId, final String apiKey) {
		if (INSTANCE == null) {
			synchronized (ServicesFactory.class) {
				if (INSTANCE == null) {
					INSTANCE = new ServicesFactory(baseUrl, merchantId, apiKey);
				}
			}
		}
		return INSTANCE;
	}
	
	public <V extends BaseService<?, ?>> V getService(final Class<V> type) {
		try {
			Constructor<V> ctor = type.getDeclaredConstructor(String.class, String.class, String.class);
			ctor.setAccessible(true);
			return ctor.newInstance(this.baseUrl, this.merchantId, this.apiKey);
			// en todos los casos regresa nulo.
		} catch (NoSuchMethodException e) {
			return null;
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		}
	}

}
