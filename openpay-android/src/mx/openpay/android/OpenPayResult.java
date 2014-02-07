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
 * @author Luis Delucio
 *
 */
public class OpenPayResult<T> {
	private OpenpayServiceException openpayServiceException;

	private OperationResult<T> operationResult;

	private ServiceUnavailableException serviceUnavailableException;

	public OpenpayServiceException getOpenpayServiceException() {
		return this.openpayServiceException;
	}

	public void setOpenpayServiceException(final OpenpayServiceException openpayServiceException) {
		this.openpayServiceException = openpayServiceException;
	}

	public OperationResult<T> getOperationResult() {
		return this.operationResult;
	}

	public void setOperationResult(final OperationResult<T> operationResult) {
		this.operationResult = operationResult;
	}

	public ServiceUnavailableException getServiceUnavailableException() {
		return this.serviceUnavailableException;
	}

	public void setServiceUnavailableException(final ServiceUnavailableException serviceUnavailableException) {
		this.serviceUnavailableException = serviceUnavailableException;
	}
}
