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
package mx.openpay.android.example;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import mx.openpay.android.Openpay;
import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;
import mx.openpay.android.model.Token;
import mx.openpay.android.validation.CardValidator;

public class AddCardActivity extends FragmentActivity implements OperationCallBack<Token> {

    private ProgressDialogFragment progressFragment;

    private DeviceIdFragment deviceIdFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add_card);

        this.progressFragment = ProgressDialogFragment.newInstance(R.string.progress_message);

        Openpay openpay = ((OpenPayAppExample) getApplication()).getOpenpay();


        String deviceIdString = openpay.getDeviceCollectorDefaultImpl().setup(AddCardActivity.this);
        System.out.println("deviceIdString");
        System.out.println(deviceIdString);
        DialogFragment fragment = MessageDialogFragment.newInstance(R.string.sessionId, deviceIdString);
        fragment.show(AddCardActivity.this.getFragmentManager(), AddCardActivity.this.getString(R.string.info));
        TextView tv = (TextView) AddCardActivity.this.findViewById(R.id.textView3);
        tv.setText(deviceIdString);


    }

    public void saveCard(final View view) {
        this.addToken();
    }

    public String getDeviceId(final View view) {
        return this.deviceIdFragment.getDeviceId();
    }

    private void addToken() {
        Openpay openpay = ((OpenPayAppExample) this.getApplication()).getOpenpay();
        Card card = new Card();
        boolean isValid = true;

        final EditText holderNameEt = ((EditText) this.findViewById(R.id.holder_name));
        final String holderName = holderNameEt.getText().toString();
        card.holderName(holderName);
        if (!CardValidator.validateHolderName(holderName)) {
            holderNameEt.setError(this.getString(R.string.invalid_holder_name));
            isValid = false;
        }

        final EditText cardNumberEt = ((EditText) this.findViewById(R.id.card_number));
        final String cardNumber = cardNumberEt.getText().toString();
        card.cardNumber(cardNumber);
        if (!CardValidator.validateNumber(cardNumber)) {
            cardNumberEt.setError(this.getString(R.string.invalid_card_number));
            isValid = false;
        }

        EditText cvv2Et = ((EditText) this.findViewById(R.id.cvv2));
        String cvv = cvv2Et.getText().toString();
        card.cvv2(cvv);
        if (!CardValidator.validateCVV(cvv, cardNumber)) {
            cvv2Et.setError(this.getString(R.string.invalid_cvv));
            isValid = false;
        }

        Integer year = this.getInteger(((Spinner) this.findViewById(R.id.year_spinner)).getSelectedItem().toString());

        Integer month = this.getInteger(((Spinner) this.findViewById(R.id.month_spinner)).getSelectedItem().toString());

        if (!CardValidator.validateExpiryDate(month, year)) {
            DialogFragment fragment = MessageDialogFragment.newInstance(R.string.error,
                    this.getString(R.string.invalid_expire_date));
            fragment.show(this.getFragmentManager(), "Error");
            isValid = false;
        }

        card.expirationMonth(month);
        card.expirationYear(year);

        if (isValid) {
            this.progressFragment.show(this.getSupportFragmentManager(), "progress");
            openpay.createToken(card, this);
        }

    }

    @Override
    public void onError(final OpenpayServiceException error) {
        error.printStackTrace();
        this.progressFragment.dismiss();
        int desc = 0;
        String msg = null;
        switch (error.getErrorCode()) {
            case 3001:
                desc = R.string.declined;
                msg = this.getString(desc);
                break;
            case 3002:
                desc = R.string.expired;
                msg = this.getString(desc);
                break;
            case 3003:
                desc = R.string.insufficient_funds;
                msg = this.getString(desc);
                break;
            case 3004:
                desc = R.string.stolen_card;
                msg = this.getString(desc);
                break;
            case 3005:
                desc = R.string.suspected_fraud;
                msg = this.getString(desc);
                break;

            case 2002:
                desc = R.string.already_exists;
                msg = this.getString(desc);
                break;
            default:
                desc = R.string.error_creating_card;
                msg = error.getDescription();
        }

        DialogFragment fragment = MessageDialogFragment.newInstance(R.string.error, msg);
        fragment.show(this.getFragmentManager(), "Error");
    }

    @Override
    public void onCommunicationError(final ServiceUnavailableException error) {
        error.printStackTrace();
        this.progressFragment.dismiss();
        DialogFragment fragment = MessageDialogFragment.newInstance(R.string.error,
                this.getString(R.string.communication_error));
        fragment.show(this.getFragmentManager(), "Error");
    }

    @Override
    public void onSuccess(final OperationResult<Token> result) {
        this.progressFragment.dismiss();
        this.clearData();
        String cardId = result.getResult().getId();
        System.out.println("cardId");
        System.out.println(cardId);
        DialogFragment fragment = MessageDialogFragment.newInstance(R.string.card_added, cardId);
        fragment.show(this.getFragmentManager(), this.getString(R.string.info));
    }

    private void clearData() {
        ((EditText) this.findViewById(R.id.holder_name)).setText("");
        ((EditText) this.findViewById(R.id.card_number)).setText("");
        ((EditText) this.findViewById(R.id.cvv2)).setText("");
        ((Spinner) this.findViewById(R.id.year_spinner)).setSelection(0);
        ((Spinner) this.findViewById(R.id.month_spinner)).setSelection(0);

    }

    private Integer getInteger(final String number) {
        try {
            return Integer.valueOf(number);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

}
