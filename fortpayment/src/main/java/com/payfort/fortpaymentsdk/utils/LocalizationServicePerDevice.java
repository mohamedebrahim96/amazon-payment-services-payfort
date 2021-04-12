package com.payfort.fortpaymentsdk.utils;

import android.content.Context;

public class LocalizationServicePerDevice extends LocalizationService {
    @Override
    public Context updateByLanguage(Context context, String language, String systemDefaultLanguage) {
        // do nothing till google solve the webkit bug
        // @link: https://groups.google.com/a/chromium.org/forum/#!msg/android-webview-dev/m0EtO3IXNn0/_lYzvfokAwAJ
        return context;
    }
}
