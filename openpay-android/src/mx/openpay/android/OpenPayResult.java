/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
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
