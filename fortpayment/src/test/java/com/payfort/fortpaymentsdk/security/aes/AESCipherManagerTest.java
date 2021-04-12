package com.payfort.fortpaymentsdk.security.aes;

import android.os.Build;

import com.payfort.fortpaymentsdk.exceptions.FortException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.crypto.spec.SecretKeySpec;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class AESCipherManagerTest {
    AESCipherManager aesCipherManager;

    @Before
    public void setUp() throws Exception {
        aesCipherManager = new AESCipherManager();
    }

    @Test
    public void encryptData() throws FortException {
        SecretKeySpec secretKeySpec = aesCipherManager.generateAESKey();
        String fortSDK = aesCipherManager.encryptData("FortSDK", secretKeySpec);
        assert(fortSDK!=null);
    }

    @Test
    public void encryptData_then_decryptMsg() throws FortException {
        SecretKeySpec secretKeySpec = aesCipherManager.generateAESKey();
        String fortSDK = aesCipherManager.encryptData("FortSDK", secretKeySpec);
        assert(fortSDK!=null);
        String decryptMsg = aesCipherManager.decryptMsg(fortSDK, secretKeySpec);
        assert(decryptMsg.equals("FortSDK"));
    }


    @Test
    public void getKeyEncoding_isBASE32() throws FortException {
        AESCipher aesCipher = new AESCipher(aesCipherManager.generateAESKey());
        String key = aesCipher.getKey(KeyEncoding.BASE32);
        System.out.println(key!=null);
    }

    @Test
    public void getKeyEncoding_isBASE64() throws FortException {
        AESCipher aesCipher = new AESCipher(aesCipherManager.generateAESKey());
        String key = aesCipher.getKey(KeyEncoding.BASE64);
        System.out.println(key!=null);
    }

}

