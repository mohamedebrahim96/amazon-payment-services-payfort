package com.payfort.fortpaymentsdk.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import com.payfort.fortpaymentsdk.domain.model.FortSdkCache;

import java.util.Locale;

public class LocalizationService {

    public static String getDefaultLocale(Context context) {
        Configuration config = context.getResources().getConfiguration();
        Locale sysLocale = getSystemLocal(config);
        return sysLocale.getLanguage();
    }

    public Context updateByLanguage(Context context, String language, String systemDefaultLanguage) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();

        if (language != null && !language.equals("") && !language.equals(systemDefaultLanguage)) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale);

                context.getResources().getConfiguration().setLocale(locale);
                context = context.createConfigurationContext(config);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                setSystemLocale(config, locale);
                context = context.createConfigurationContext(config);
            } else {
                setSystemLocaleLegacy(config, locale);
                resources.updateConfiguration(config, resources.getDisplayMetrics());
            }

        }

        return context;
    }

    private static Locale getSystemLocal(Configuration config) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getSystemLocale(config);
        }
        return getSystemLocaleLegacy(config);

    }

    @TargetApi(Build.VERSION_CODES.N)
    public void restoreLocale(Context context, String systemDefaultLanguage) {
        if (systemDefaultLanguage != null) {
            updateByLanguage(context, systemDefaultLanguage, FortSdkCache.getREQUEST_LANGUAGE());

        }
    }

    private static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Locale getSystemLocale(Configuration config) {
        return LocaleList.getDefault().get(0);
    }

    protected void setSystemLocaleLegacy(Configuration config, Locale locale) {
        config.locale = locale;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void setSystemLocale(Configuration config, Locale locale) {
        config.setLayoutDirection(locale);
        config.setLocale(locale);
    }
}
