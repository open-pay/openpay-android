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
