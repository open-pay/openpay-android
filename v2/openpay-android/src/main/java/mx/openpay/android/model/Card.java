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
package mx.openpay.android.model;

import android.annotation.SuppressLint;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

@SuppressLint("DefaultLocale")
public class Card {

	@Key("creation_date")
	private DateTime creationDate;
	@Key
	private String id;

	@Key("bank_name")
	private String bankName;

	@Key("allows_payouts")
	private Boolean allowsPayouts;

	@Key("holder_name")
	private String holderName;

	@Key("expiration_month")
	private String expirationMonth;

	@Key("expiration_year")
	private String expirationYear;
	
	@Key
	private Address address;

	@Key("card_number")
	private String cardNumber;
	@Key
	private String brand;

	@Key("allows_charges")
	private Boolean allowsCharges;

	@Key("bank_code")
	private String bankCode;
	@Key
	private String type;
	@Key
	private String cvv2;

    /**
     * Card number. Required.
     */
    public Card cardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    /**
     * Security code. Required only for charges.
     */
    public Card cvv2(final String cvv2) {
        this.cvv2 = cvv2;
        return this;
    }

    /**
     * Expiration month. Required only for charges.
     */
    public Card expirationMonth(final Integer expirationMonth) {
        this.expirationMonth = this.getTwoDigitString(expirationMonth);
        return this;
    }

    /**
     * Expiration year. Required only for charges.
     */
    public Card expirationYear(final Integer expirationYear) {
        this.expirationYear = this.getTwoDigitString(expirationYear);
        return this;
    }

    private String getTwoDigitString(final Integer number) {
        if (number == null) {
            return null;
        }
        return String.format("%02d", number);
    }

    /**
     * Card Holder name. Required.
     */
    public Card holderName(final String holderName) {
        this.holderName = holderName;
        return this;
    }

    /**
     * Card Holder address. Optional.
     */
    public Card address(final Address address) {
        this.address = address;
        return this;
    }

    /**
     * Bank code. See <a
     * href="http://es.wikipedia.org/w/index.php?title=CLABE&oldid=71482742#C.C3.B3digo_de_Banco">this</a> for an
     * incomplete list as of December, 2013.
     */
    public Card bankCode(final Integer bankCode) {
        if (bankCode == null) {
            this.bankCode = null;
        } else {
            this.bankCode = String.format("%03d", bankCode);
        }
        return this;
    }

    /**
     * Bank code in a three-digit string.
     * @see CreateCardParams#bankCode(Integer)
     */
    public Card bankCode(final String bankCode) {
        this.bankCode = bankCode;
        return this;
    }

	@Override
	public String toString() {
		return String
				.format("Card [creationDate=%s, id=%s, bankName=%s, allowsPayouts=%s, holderName=%s, expirationMonth=%s, expirationYear=%s, address=%s, cardNumber=%s, brand=%s, allowsCharges=%s, bankCode=%s, type=%s, cvv2=%s]",
						this.creationDate, this.id, this.bankName, this.allowsPayouts, this.holderName,
						this.expirationMonth, this.expirationYear, this.address, this.cardNumber, this.brand,
						this.allowsCharges, this.bankCode, this.type, this.cvv2);
	}

	public DateTime getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(final DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(final String bankName) {
		this.bankName = bankName;
	}

	public Boolean getAllowsPayouts() {
		return this.allowsPayouts;
	}

	public void setAllowsPayouts(final Boolean allowsPayouts) {
		this.allowsPayouts = allowsPayouts;
	}

	public String getHolderName() {
		return this.holderName;
	}

	public void setHolderName(final String holderName) {
		this.holderName = holderName;
	}

	public String getExpirationMonth() {
		return this.expirationMonth;
	}

	public void setExpirationMonth(final String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public String getExpirationYear() {
		return this.expirationYear;
	}

	public void setExpirationYear(final String expirationYear) {
		this.expirationYear = expirationYear;
	}

	public Address getAddress() {
		return this.address;
	}

	public void setAddress(final Address address) {
		this.address = address;
	}

	public String getCardNumber() {
		return this.cardNumber;
	}

	public void setCardNumber(final String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(final String brand) {
		this.brand = brand;
	}

	public Boolean getAllowsCharges() {
		return this.allowsCharges;
	}

	public void setAllowsCharges(final Boolean allowsCharges) {
		this.allowsCharges = allowsCharges;
	}

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(final String bankCode) {
		this.bankCode = bankCode;
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getCvv2() {
		return this.cvv2;
	}

	public void setCvv2(final String cvv2) {
		this.cvv2 = cvv2;
	}

}
