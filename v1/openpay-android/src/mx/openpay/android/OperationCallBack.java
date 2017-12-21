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

/**
 * The Interface OperationCallBack.
 */
public interface OperationCallBack<T> {

	/**
	 * On error.
	 *
	 * @param error the error
	 */
	void onError(final OpenpayServiceException error);
	
	/**
	 * On communication error.
	 *
	 * @param error the error
	 */
	void onCommunicationError(final ServiceUnavailableException error);
    
    /**
     * On success.
     *
     * @param operationResult the operation result
     */
	void onSuccess(final OperationResult<T> operationResult);
}
