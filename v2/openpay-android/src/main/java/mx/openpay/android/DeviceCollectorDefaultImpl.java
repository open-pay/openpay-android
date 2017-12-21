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
package mx.openpay.android;


import android.app.Activity;
import android.provider.Settings;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.UUID;


/**
 * @author Hugo Hernández
 */
public class DeviceCollectorDefaultImpl {

    private String baseUrl;
    private String merchantId;
    private String errorMessage;

    public DeviceCollectorDefaultImpl(final String baseUrl, final String merchantId) {
        this.baseUrl = baseUrl;
        this.merchantId = merchantId;
    }

    public String setup(final Activity activity) {
        try {
            // Generamos sessionId
            String sessionId = UUID.randomUUID().toString();
            sessionId = sessionId.replace("-", "");

            // Obtenemos identifierForVendor
            String identifierForVendor = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

            // Inicializamos WebView con el identifierForVendor
            WebView webViewDF = new WebView(activity);
            webViewDF.setWebViewClient(new WebViewClient());
            webViewDF.getSettings().setJavaScriptEnabled(true);
            String identifierForVendorScript = String.format("var identifierForVendor = '%s';", identifierForVendor);
            webViewDF.evaluateJavascript(identifierForVendorScript, null);

            // Ejecutamos OpenControl
            String urlAsString = String.format("%s/oa/logo.htm?m=%s&s=%s", this.baseUrl, this.merchantId, sessionId);
            webViewDF.loadUrl(urlAsString);

            return sessionId;
        } catch (final Exception e) {
            this.logError(this.getClass().getName(), e.getMessage());
            this.errorMessage = e.getMessage();
            return null;
        }
    }

    private void logError(final String tag, final String content) {
        if (content.length() > 4000) {
            Log.e(tag, content.substring(0, 4000));
            this.logError(tag, content.substring(4000));
        } else {
            Log.e(tag, content);
        }
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

}
