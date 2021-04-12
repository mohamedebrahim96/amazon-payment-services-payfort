package com.payfort.fortpaymentsdk.utils;

import android.os.Build;

import com.payfort.fortpaymentsdk.constants.Constants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Any;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;

import static org.junit.Assert.*;
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class MapUtilsTest {

    @Test
    public void getLanguageWithEmptyMap_ThenReturnEnglish() {
        String language = MapUtils.getLanguage(null);
        assertEquals(language, Constants.LANGUAGES.ENGLISH);
    }

    @Test
    public void getLanguageWithMapContainsEnglish_ThenReturnEnglish() {
        HashMap<String, Object> stringAnyHashMap = new HashMap<>();
        stringAnyHashMap.put(Constants.FORT_PARAMS.LANGUAGE,Constants.LANGUAGES.ENGLISH);
        String language = MapUtils.getLanguage(stringAnyHashMap);
        assertEquals(language, Constants.LANGUAGES.ENGLISH);
    }

    @Test
    public void getLanguageWithMapContainsArabic_ThenReturnArabic() {
        HashMap<String, Object> stringAnyHashMap = new HashMap<>();
        stringAnyHashMap.put(Constants.FORT_PARAMS.LANGUAGE,Constants.LANGUAGES.ARABIC);
        String language = MapUtils.getLanguage(stringAnyHashMap);
        assertEquals(language, Constants.LANGUAGES.ARABIC);
    }

    @Test
    public void provideMapWithVisaPaymentOptionValue_ThenReturnVISA() {
        HashMap<String, Object> stringAnyHashMap = new HashMap<>();
        stringAnyHashMap.put(Constants.FORT_PARAMS.PAYMENT_OPTION,Constants.CREDIT_CARDS_TYPES.VISA);

        String paymentOptionValue = MapUtils.getPaymentOptionValue(stringAnyHashMap);
        assertEquals(paymentOptionValue, Constants.CREDIT_CARDS_TYPES.VISA);
    }


    @Test
    public void provideMapWithMezaPaymentOptionValue_ThenReturnMeza() {
        HashMap<String, Object> stringAnyHashMap = new HashMap<>();
        stringAnyHashMap.put(Constants.FORT_PARAMS.PAYMENT_OPTION,Constants.CREDIT_CARDS_TYPES.MEEZA);

        String paymentOptionValue = MapUtils.getPaymentOptionValue(stringAnyHashMap);
        assertEquals(paymentOptionValue, Constants.CREDIT_CARDS_TYPES.MEEZA);
    }

    @Test
    public void displayRememberMe_thenReturnFalse() {
        HashMap<String, Object> stringAnyHashMap = new HashMap<>();
        stringAnyHashMap.put(Constants.FORT_PARAMS.REMEMBER_ME,"NO");


        boolean rememberMe = MapUtils.displayRememberMe(stringAnyHashMap);
        assertEquals(rememberMe, false);

    }

    @Test
    public void displayRememberMe_thenReturnTrue() {
        HashMap<String, Object> stringAnyHashMap = new HashMap<>();

        boolean rememberMe = MapUtils.displayRememberMe(stringAnyHashMap);
        assertEquals(rememberMe, true);

    }


}