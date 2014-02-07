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
