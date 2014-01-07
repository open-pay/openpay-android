/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.android.example;

import mx.openpay.android.Openpay;
import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.validation.CardValidator;
import mx.openpay.client.Card;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class AddCardActivity extends FragmentActivity implements OperationCallBack{

	private ProgressDialogFragment progressFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_card);
		
		progressFragment = ProgressDialogFragment.newInstance(R.string.progress_message);
	}

	public void saveCard(View view){
		
		addCard();
	}
	
	
	private void addCard(){
		Openpay openpay = ((OpenPayAppExample) getApplication()).getopenpay();
		Card card = new Card();
		boolean isValid = true;
		
		final EditText holderNameEt =((EditText) findViewById(R.id.holder_name));
		final String holderName = holderNameEt.getText().toString(); 
		card.holderName(holderName);
		if (!CardValidator.validateHolderName(holderName)){
			holderNameEt.setError(this.getString(R.string.invalid_holder_name));
			isValid = false;
		}
		
		final EditText cardNumberEt = ((EditText) findViewById(R.id.card_number));
		final String cardNumber = cardNumberEt.getText().toString();
		card.cardNumber(cardNumber);
		if (!CardValidator.validateNumber(cardNumber)){
			cardNumberEt.setError(this.getString(R.string.invalid_card_number));
			isValid = false;
		}
		
		EditText cvv2Et =  ((EditText) findViewById(R.id.cvv2));
		String cvv = cvv2Et.getText().toString();
		card.cvv2(cvv);
		if(!CardValidator.validateCVV(cvv, cardNumber)){
			cvv2Et.setError(this.getString(R.string.invalid_cvv));
			isValid = false;
		}
		
		Integer year = this.getInteger(((Spinner) findViewById(R.id.year_spinner)).getSelectedItem().toString());
		
		Integer month = this.getInteger(((Spinner) findViewById(R.id.month_spinner)).getSelectedItem().toString());

		if (!CardValidator.validateExpiryDate(month, year)) {
			  DialogFragment fragment = MessageDialogFragment.newInstance(R.string.error, this.getString(R.string.invalid_expire_date));
		        fragment.show(this.getFragmentManager(), "Error");
		        isValid = false;
		}
		
		card.expirationMonth(month);
		card.expirationYear(year);
		
		
		if (isValid){
			progressFragment.show(getSupportFragmentManager(), "progress");
			openpay.createCard(card, this);	
		} 
		
	}

	@Override
	public void onError(OpenpayServiceException error) {
		error.printStackTrace();
		progressFragment.dismiss();
		int desc = 0;
		switch( error.getErrorCode()){
		 case 3001:
			 desc = R.string.declined;
			 break;
		 case 3002:
			 desc = R.string.expired;
			 break;
		 case 3003:
			 desc = R.string.insufficient_funds;
			 break;
		 case 3004:
			 desc = R.string.stolen_card;
			 break;
		 case 3005:
			 desc = R.string.suspected_fraud;
			 break;
			 
		 case 2002:
			 desc = R.string.already_exists;
			 break;
		default:
			desc = R.string.error_creating_card;
		}
		
		  DialogFragment fragment = MessageDialogFragment.newInstance(R.string.error, this.getString(desc));
	        fragment.show(this.getFragmentManager(), "Error");
	}
	
	@Override
	public void onCommunicationError(ServiceUnavailableException error) {
		error.printStackTrace();
		progressFragment.dismiss();
		  DialogFragment fragment = MessageDialogFragment.newInstance(R.string.error, this.getString(R.string.communication_error));
	        fragment.show(this.getFragmentManager(), "Error");
	}

	@Override
	public void onSuccess(OperationResult operationResult) {
		progressFragment.dismiss();
		this.clearData();
	    DialogFragment fragment = MessageDialogFragment.newInstance(R.string.card_added, this.getString(R.string.card_created));
        fragment.show(this.getFragmentManager(), this.getString(R.string.info));

	}
	
	
	private void clearData() {
		((EditText) findViewById(R.id.holder_name)).setText("");
		((EditText) findViewById(R.id.card_number)).setText("");
		((EditText) findViewById(R.id.cvv2)).setText("");
		((Spinner) findViewById(R.id.year_spinner)).setSelection(0);
		((Spinner) findViewById(R.id.month_spinner)).setSelection(0);
		
	}

	private Integer getInteger(final String number){
		try {
			return Integer.valueOf(number);
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}
}





