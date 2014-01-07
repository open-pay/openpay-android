/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.android.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement the Luhn algorithm (with Luhn key on the last digit). See also
 * <a href="http://en.wikipedia.org/wiki/Luhn_algorithm">http://en.wikipedia.org/wiki/Luhn_algorithm</a> and
 * <a href="http://www.merriampark.com/anatomycc.htm">http://www.merriampark.com/anatomycc.htm</a>.
 *
 * @author Hardy Ferentschik
 */
public class LuhnValidator {
	private static final int multiplicator = 2;


	public static boolean passesLuhnTest(String value) {
		char[] chars = value.toCharArray();

		List<Integer> digits = new ArrayList<Integer>();
		for ( char c : chars ) {
			if ( Character.isDigit( c ) ) {
				digits.add( c - '0' );
			}
		}
		int length = digits.size();
		int sum = 0;
		boolean even = false;
		for ( int index = length - 1; index >= 0; index-- ) {
			int digit = digits.get( index );
			if ( even ) {
				digit *= multiplicator;
			}
			if ( digit > 9 ) {
				digit = digit / 10 + digit % 10;
			}
			sum += digit;
			even = !even;
		}
		return sum % 10 == 0;
	}
}
