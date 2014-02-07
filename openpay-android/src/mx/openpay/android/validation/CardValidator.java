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

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * The Class CardValidator.
 *
 * @author ismael
 */
@SuppressLint("SimpleDateFormat") 
public class CardValidator {
	
	public static boolean validateCard(final String holderName, final String cardNumber, final Integer expMonth, final Integer expYear, final String cvv){
		return validateHolderName(holderName)
		&& validateCVV(cvv, cardNumber)
		&& validateExpiryDate(expMonth, expYear)
		&& validateNumber(cardNumber);
		
	}
	
	/**
	 * Validate holder name.
	 *
	 * @param holderName the holder name
	 * @return true, if successful
	 */
	public static boolean validateHolderName(final String holderName){
		return holderName != null && holderName.trim().length() > 0;
	}
	
	/**
	 * Validate cvv.
	 *
	 * @param cvv the cvv
	 * @param cardNumber the card number
	 * @return true, if successful
	 */
	public static boolean validateCVV(final String cvv, final String cardNumber) {
		if (cvv == null || cvv.trim().length() == 0) {
			return false;
		} 
		
		CardType type = getType(cardNumber);
		if (CardType.AMEX.equals(type) && cvv.trim().length() != 4) {
			return false;
		}
		
		
		if ((CardType.MASTERCARD.equals(type) || CardType.VISA.equals(type)) && cvv.trim().length() != 3) {
			return false;
		}
		
		if (CardType.UNKNOWN.equals(type)){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Validate expiry date.
	 *
	 * @param expirationMonth the expiration month
	 * @param expirationYear the expiration year
	 * @return true, if successful
	 */
	public static boolean validateExpiryDate(final Integer expirationMonth, final Integer expirationYear){
		if (!validateMonth(expirationMonth)){
			return false;
		}
		
		if (expirationYear == null){
			return false;
		}
		
		Calendar today = new GregorianCalendar();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yy");
		Date expirationDate;
		try {
			expirationDate = dateFormat.parse(expirationMonth + "-" + expirationYear);
			Calendar cardExpiration = new GregorianCalendar();
			cardExpiration.setTime(expirationDate);

			if (cardExpiration.get(Calendar.YEAR) > today.get(Calendar.YEAR)) {
				return true;
			} else if (cardExpiration.get(Calendar.YEAR) == today.get(Calendar.YEAR)
					&& cardExpiration.get(Calendar.MONTH) >= today.get(Calendar.MONTH)) {
				return true;
			}
			
		} catch (ParseException e) {
			
		}
		
		return false;
		
	}
	
	/**
	 * Validate month.
	 *
	 * @param month the month
	 * @return true, if successful
	 */
	public static boolean validateMonth(final Integer month) {
    	if (month == null) {
    		return false;
    	}
    	
    	return (month >= 1 && month <= 12);
    	
    	//validar fecha pasada
    }

	/**
	 * Validate number.
	 *
	 * @param number the number
	 * @return true, if successful
	 */
	public static boolean validateNumber(final String number){
		
		if (number == null || number.trim().length() == 0){
			return false;
		}
		
		if (!LuhnValidator.passesLuhnTest(number)){
			return false;
		}
		
		String trimNumber = number.trim();
		CardType type = getType(number);
		
		if (CardType.AMEX.equals(type) && trimNumber.length() != 15) {
			return false;
		} else if (CardType.MASTERCARD.equals(type) && trimNumber.length() != 16){
			return false;
		} else if (CardType.VISA.equals(type) && !(trimNumber.length() == 16 || trimNumber.length() == 13)){
			return false;
		}  else if (CardType.UNKNOWN.equals(type)){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Starts with.
	 *
	 * @param cardNumber the card number
	 * @param prefixes the prefixes
	 * @return true, if successful
	 */
	public static boolean startsWith(final String cardNumber, final String... prefixes) {
        for (String prefix : prefixes) {
            if (cardNumber.startsWith(prefix)) {
                return true;
            }
        }
        
        return false;
    }
	
	/**
	 * Gets the type.
	 *
	 * @param cardNumber the card number
	 * @return the type
	 */
	public static CardType getType(final String cardNumber) {
        if (cardNumber != null && cardNumber.trim().length() > 0) {
            if (startsWith(cardNumber, "34", "37")) {
                return CardType.AMEX;
            } else if (startsWith(cardNumber, "4")) {
                return CardType.VISA;
            } else if (startsWith(cardNumber, "51", "52", "53", "54", "55")) {
                return CardType.MASTERCARD;
            }
        }
        
        return CardType.UNKNOWN;
    }
}
