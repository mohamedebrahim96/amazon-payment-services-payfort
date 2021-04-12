package com.payfort.fortpaymentsdk.security;

import android.os.Build;

import com.payfort.fortpaymentsdk.exceptions.FortException;
import com.payfort.fortpaymentsdk.security.aes.AESCipher;
import com.payfort.fortpaymentsdk.security.aes.AESCipherManager;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.spec.SecretKeySpec;

import static org.junit.Assert.*;
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class DataSecurityServiceTest {
    private static final String PUBLIC_KEY_MODULUS = "114148583453528745752158573299299462974849455803660656482072552191881163973185343560593952047234554690302302264823153536655488960387116585505622616432041543078622970397250697451778064891543319594178153797228157167683013081933196126650429380296544727748466019924362570851520110617956047194419951742367399790853";
    private static final String PUBLIC_KEY_EXPONENT = "65537";
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void encryptRequestData() throws Throwable {
        KeyFactory fact = KeyFactory.getInstance("RSA");

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(
                PUBLIC_KEY_MODULUS), new BigInteger(PUBLIC_KEY_EXPONENT));
        PublicKey pubKey = fact.generatePublic(publicKeySpec);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("saleh","saleh");
        AESCipherManager aesCipherManager = new AESCipherManager();

        String s = DataSecurityService.encryptRequestData(jsonObject.toString(), (RSAPublicKey) pubKey, aesCipherManager.generateAESKey());

        assertTrue(s!=null);

    }

    @Test
    public void encryptRequestDataThenDecryptData() throws Throwable {
        KeyFactory fact = KeyFactory.getInstance("RSA");

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(
                PUBLIC_KEY_MODULUS), new BigInteger(PUBLIC_KEY_EXPONENT));
        PublicKey pubKey = fact.generatePublic(publicKeySpec);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("FortSDK","FortSDK");
        AESCipherManager aesCipherManager = new AESCipherManager();

        SecretKeySpec secretKeySpec = aesCipherManager.generateAESKey();
        String s = DataSecurityService.encryptRequestData(jsonObject.toString(), (RSAPublicKey) pubKey, secretKeySpec);
        assertTrue(s!=null);

    }


}