![Openpay-Android](http://www.openpay.mx/img/github/android.jpg)

Android library designed to facilitate the processing collect credit card information from a devices directly invoking our services without invoking the origin server.

Current version: v2.0.0

## Benefits:
* The card information does not have to pass through the origin server, it is sent directly to Openpay.
* It is the easiest and fastest way to integrate a card registration module on a device.

## Installation

1. Download de latest SDK released version (SDK-v2.0.0.zip).
2. You must have installed the Android SDK with API Level 19-27 and android-support-v13.
3. Add the openpay-android library (openpay-v2.0.0.aar) to your project.
4. Add the needed dependencies to your project:

```java
dependencies {
    compile 'com.android.support:support-v13:27.0.2'
    compile 'com.google.http-client:google-http-client:1.23.0'
    compile 'com.google.http-client:google-http-client-android:1.23.0'
    compile 'com.google.http-client:google-http-client-jackson2:1.23.0'
    compile 'com.fasterxml.jackson.core:jackson-core:2.9.2'
}
```

## Configuration
Before you can use openpay-android is necessary to configure: merchant id, public key and production Mode

The merchant id and public key were assigned when you created your account. With these data, Openpay can identify the account.

You must configure openpay  when instatiate it:

```java
	Openpay openpay = new Openpay("MERCHANT_ID", "PUBLIC_API_KEY", productionMode);
```

### Enable sandbox Mode
To test your implementation, there Sandox environment, which is enabled when you pass **false** value to parameter **productionMode**

|Notes:|
|:------|
|* Both MERCHANTID as PUBLIC_API_KEY, are obtained from the homepage of your account on the [Openpay](http://www.openpay.mx/) site.|
|* You should never use your private key along with the library, because it is visible on the client side.|

### Creating tokens
To create a token, you need to call the method **Openpay.createToken()**:
```java
openPay.createToken(CARD_OBJECT, OPERATION_CALLBACK);
```

#### Example of creating a token:
```java

	Card card = new Card();
	card.holderName("Juan Perez Ramirez");
	card.cardNumber("4111111111111111");
	card.expirationMonth(12);
	card.expirationYear(20);
	card.cvv2("110");

	openpay.createToken(card, new OperationCallBack() {
				
				@Override
				public void onSuccess(OperationResult arg0) {
					//Handlo in success
				}
				
				@Override
				public void onError(OpenpayServiceException arg0) {
					//Handle Error
				}
				
				@Override
				public void onCommunicationError(ServiceUnavailableException arg0) {
					//Handle communication error
				}
			});	
```

The first parameter is a  object containing information about the card, the last  parameter define the methods that will be called after the operation was successful or failed (respectively). The result will be a token object.
The definition of Token object find it [here](http://docs.openpay.mx/#tokens).

### OperationCallBack methods
The OperationCallBack serve as handles of the result of the card creation.

#### onSuccess
This method is called when the card is successful created. Get a single parameter which is a OperationResult that contains the card object.
Complete example of implementing a function onSuccess:

```java
onSuccess(OperationResult operationResult)  {
		progressFragment.dismiss();
		this.clearData();
	    DialogFragment fragment = MessageDialogFragment.newInstance(R.string.card_added, this.getString(R.string.card_created));
        fragment.show(this.getFragmentManager(), this.getString(R.string.info));
}
```

#### onError
This method is called when occurs some error on creating a card. Get a single parameter of type OpenpayServiceException that contains the error code.

Complete example of implementing a function onError:

```java
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
```

#### onCommunicationError
This method is called when the application cannot contact the Openpay server.



## Card Validation Functions
Openpay-android also includes some utilities to validate a card.

Available methods are:

* `CardValidator.validateHolderName()`
* `CardValidator.validateCVV()`
* `CardValidator.validateExpiryDate()`
* `CardValidator.validateNumber()`

### Number card validation
To validate a card number can use the method **CardValidator.validateNumber()**.

This method receives as parameter a String with the card number to be validated and return one true / false if it is a valid card number and is accepted by Openpay. 
Example:
```java
CardValidator.validateNumber("5555555555554444");
```
This method is very useful for determining whether a card number is valid and if a candidate for use with Openpay, so we recommend that you use before attempting create a card.

Examples:
```java
OpenPay.card.validateCardNumber("5555555555554444"); // TRUE. Valid card number and accepted by OpenPay (MASTERCARD)

OpenPay.card.validateCardNumber("6011861883604117"); // FALSE. Number of valid card but not accepted by OpenPay (Discover)
```
### Security Code Validation
To validate a security code is used the method **CardValidator.validateCVV()**.

This method takes a cvv and card number as Strings and returns true / false if the string is valid. Example:
```javascript
CardValidator.validateCVV("123", "5555555555554444"); // válido
CardValidator.validateCVV("1234", "5555555555554444"); // inválido
CardValidator.validateCVV("A23", "5555555555554444"); // inválido
```
### Expiration date validation
For this purpose is used the method **CardValidator.validateExpiryDate()**.

Receive two Integers as parameters to represent the month and year of expiry of the card. Returns true / false if the combination of both data, month and year, determine a valid expiration date. Example:

```java
CardValidator.validateExpiryDate(1, 13); // inválido
CardValidator.validateExpiryDate(5, 15); // válido
```

## Fraud detection using device data
OpenPay can use the device information of a transaction in order to better detect fraudulent transactions.
To do this, add the following code to your activity or fragment, when collecting payment information:

```java
String deviceIdString = openpay.getDeviceCollectorDefaultImpl().setup(activity);
```

This method generates an identifier for the customer's device data. This value needs to be stored during checkout, and sent to OpenPay when processing the charge.

The method takes one parameter: 

Activity. The actual activity object.


Take a look at the [openpay-pay-android-example](https://github.com/open-pay/openpay-android/tree/master/v2/openpay-android-example) application to see everything put together.
