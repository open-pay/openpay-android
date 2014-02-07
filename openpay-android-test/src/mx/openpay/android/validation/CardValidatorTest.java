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
package mx.openpay.android.validation;

import mx.openpay.android.validation.CardType;
import mx.openpay.android.validation.CardValidator;

import org.junit.Test;

import junit.framework.TestCase;

public class CardValidatorTest extends TestCase {

	@Test
	public void testValidateHolderName_empty() {
		assertFalse(CardValidator.validateHolderName(""));
	}

	@Test
	public void testValidateHolderName_Null() {
		assertFalse(CardValidator.validateHolderName(null));
	}

	@Test
	public void testValidateHolderName() {
		assertTrue(CardValidator.validateHolderName("Juan Pérez"));
	}

	public void testValidateCVV_AMEX_invalid() {
		assertFalse(CardValidator.validateCVV("345", "345346796229970"));
	}
	
	@Test
	public void testValidateCVV_AMEX_null() {
		assertFalse(CardValidator.validateCVV(null, "345346796229970"));
	}
	
	@Test
	public void testValidateCVV_AMEX_empty() {
		assertFalse(CardValidator.validateCVV("", "345346796229970"));
	}
	
	@Test
	public void testValidateCVV_AMEX_valid() {
		assertTrue(CardValidator.validateCVV("3453", "345346796229970"));
	}
	
	@Test
	public void testValidateCVV_Card_notsuported() {
		assertFalse(CardValidator.validateCVV("345", "6011118279869071"));
	}
	
	@Test
	public void testValidateCVV_VISA_valid() {
		assertTrue(CardValidator.validateCVV("343", "4556259101406470"));
	}
	
	@Test
	public void testValidateCVV_VISA_invalid() {
		assertFalse(CardValidator.validateCVV("3463", "4556259101406470"));
	}
	
	@Test
	public void testValidateCVV_VISA_null() {
		assertFalse(CardValidator.validateCVV(null, "4556259101406470"));
	}
	
	@Test
	public void testValidateCVV_MASTERCARD_empty() {
		assertFalse(CardValidator.validateCVV("", "5381749254338270"));
	}
	
	@Test
	public void testValidateCVV_MASTERCARD_2digits() {
		assertFalse(CardValidator.validateCVV("12", "5381749254338270"));
	}
	
	@Test
	public void testValidateCVV_MASTERCARD_valid() {
		assertTrue(CardValidator.validateCVV("123", "5381749254338270"));
	}

	@Test
	public void testValidateExpiryDate_month_invalid() {
		assertFalse(CardValidator.validateExpiryDate(34, 19));
	}
	
	@Test
	public void testValidateExpiryDate_month_negative() {
		assertFalse(CardValidator.validateExpiryDate(-0, 19));
	}
	
	@Test
	public void testValidateExpiryDate_month_null() {
		assertFalse(CardValidator.validateExpiryDate(null, 19));
	}
	
	@Test
	public void testValidateExpiryDate_year_null() {
		assertFalse(CardValidator.validateExpiryDate(12, null));
	}
	
	@Test
	public void testValidateExpiryDate_expired() {
		assertFalse(CardValidator.validateExpiryDate(12, 13));
	}
	
	@Test
	public void testValidateExpiryDate() {
		assertTrue(CardValidator.validateExpiryDate(04, 19));
	}

	@Test
	public void testValidateNumber_empty() {
		assertFalse(CardValidator.validateNumber(""));
	}
	
	@Test
	public void testValidateNumber_null() {
		assertFalse(CardValidator.validateNumber(null));
	}
	
	@Test
	public void testValidateNumber_invalid() {
		assertFalse(CardValidator.validateNumber("5297076863267688"));
	}
	
	@Test
	public void testValidateNumber_unsupported() {
		assertFalse(CardValidator.validateNumber("6011499047154529"));
	}
	
	@Test
	public void testValidateNumber_Visa() {
		assertTrue(CardValidator.validateNumber("4556259101406470"));
	}

	@Test
	public void testValidateNumber_Visa_13() {
		assertTrue(CardValidator.validateNumber("4222222222222"));
	}
	
	@Test
	public void testValidateNumber_Mastercard() {
		assertTrue(CardValidator.validateNumber("5343436606355247"));
	}
	
	@Test
	public void testValidateNumber_Amex() {
		assertTrue(CardValidator.validateNumber("349647370177162"));
	}

	@Test
	public void testGetType_visa() {
		assertEquals(CardType.VISA, CardValidator.getType("4012888888881881"));
	}

	@Test
	public void testGetType_visa_13() {
		assertEquals(CardType.VISA, CardValidator.getType("4222222222222"));
	}
	
	@Test
	public void testGetType_mastercard() {
		assertEquals(CardType.MASTERCARD, CardValidator.getType("5105105105105100"));
	}
	
	@Test
	public void testGetType_amex() {
		assertEquals(CardType.AMEX, CardValidator.getType("378282246310005"));
	}
	
	@Test
	public void testGetType_unsupported() {
		assertFalse(CardValidator.validateNumber("3530111333300000"));
	}

}
