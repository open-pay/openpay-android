#Openpay-Android  
##Introduction
###What is openpay-android?
openpay-android is a Android library designed to facilitate the processing collect credit card information from a devices directly invoking our services without invoking the origin server.

###Benefits:
* The card information does not have to pass through the origin server, it is sent directly to Openpay.
* It is the easiest and fastest way to integrate a card registration module on a device.

##Installation

1. Clone the git repository.
2. You must have installed the Android SDK with API Level 19 and android-support-v4
3. Add the openpay-android library to your project. (project properties,"Android" category, "Library" section, and add, select the openpay-android project).

###Configuration
Before you can use openpay-android is necessary to configure: merchant id, public key and production Mode

The merchant id and public key were assigned when you created your account. With these data, Openpay can identify the account.

You must configure openpay  when instatiate it:

```java
	Openpay openpay = new Openpay("MERCHANT_ID", "PUBLIC_API_KEY", productionMode);
```
|Notes:|
|:------|
|* Both MERCHANTID as PUBLIC_API_KEY, are obtained from the homepage of your account on the [Openpay](http://www.openpay.mx/) site.|
|* You should never use your private key along with the library, because it is visible on the client side.|

###Creating cards
To create a card, you need to call the method **Openpay.createCard()**:
```java
openPay.createCard(CARD_OBJECT, {CUSTOMER-ID}, OPERATION_CALLBACK);
```

|Notes|
|:----|
|* With this method you can create cards at both merchant and customers depending on if you include the **CUSTOMER-ID** in the call or not. The **CUSTOMER-ID** parameter is optional.|
|* You can see the **CUSTOMER-ID**, into dashboard from the list customers.|
####Example of creating a merchant card:
```java

	Card card = new Card();
	card.holderName("Juan Perez Ramirez");
	card.cardNumber("4111111111111111");
	card.expirationMonth(12);
	card.expirationYear(20);
	card.cvv2("110");

	openpay.createCard(card, new OperationCallBack() {
				
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
####Example of creating customer card:
```java

	Card card = new Card();
	card.holderName("Juan Perez Ramirez");
	card.cardNumber("4111111111111111");
	card.expirationMonth(12);
	card.expirationYear(20);
	card.cvv2("110");

	openpay.createCard(card, "aos2jvwpyyy4nhbodxbu", new OperationCallBack() {
				
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
The first parameter is a  object containing information about the card, the last  parameter define the methods that will be called after the operation was successful or failed (respectively).
The definition of object card find it [here](http://docs.openpay.mx/#tarjetas).

###Creating tokens
To create a token, you need to call the method **Openpay.createToken()**:
```java
openPay.createToken(CARD_OBJECT, OPERATION_CALLBACK);
```

####Example of creating a token:
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

###OperationCallBack methods
The OperationCallBack serve as handles of the result of the card creation.

####onSuccess
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

####onError
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

####onCommunicationError
This method is called when the application cannot contact the Openpay server.



##Card Validation Functions
Openpay-android also includes some utilities to validate a card.

Available methods are:

* `CardValidator.validateHolderName()`
* `CardValidator.validateCVV()`
* `CardValidator.validateExpiryDate()`
* `CardValidator.validateNumber()`

###Number card validation
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
###Security Code Validation
To validate a security code is used the method **CardValidator.validateCVV()**.

This method takes a cvv and card number as Strings and returns true / false if the string is valid. Example:
```javascript
CardValidator.validateCVV("123", "5555555555554444"); // válido
CardValidator.validateCVV("1234", "5555555555554444"); // inválido
CardValidator.validateCVV("A23", "5555555555554444"); // inválido
```
###Expiration date validation
For this purpose is used the method **CardValidator.validateExpiryDate()**.

Receive two Integers as parameters to represent the month and year of expiry of the card. Returns true / false if the combination of both data, month and year, determine a valid expiration date. Example:

```java
CardValidator.validateExpiryDate(1, 13); // inválido
CardValidator.validateExpiryDate(5, 15); // válido
```

##Fraud Protection and device id generation.

As part of our anti-fraud strategy, we use the tools provided by [Kount](http://www.kount.com/) and in order to facilitate its use, we include inside the library an implementation of the data collector and the generation of device-id.  The datacollector detects and uses additional information like geolocation and device information to enable Kount's device fingerprinting. 

The device-id is the device identifier that you need to provided when you make a charge. To generate a device-id you use the method **openpay.getDeviceCollectorDefaultImpl().getDeviceId()**. This method invoke the collect of  device data and send it  to kount.

Every time you make a charge, you will provide an device-id. This device-id join to transaction data will be send to kount's servers to determine the transaction’s integrity.

Note
You can implement a specialized StatusListener and pass it to the collector. Check the classes mx.openpay.android.example.DeviceIdFragment and mx.openpay.android.DeviceCollectorDefaultImpl to see more.


Take a look at the [openpay-pay-android-example](https://github.com/open-pay/openpay-android/tree/master/openpay-android-example) application to see everything put together.
