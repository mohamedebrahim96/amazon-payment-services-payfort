package com.payfort.fortpaymentsdk.security.aes;

import com.payfort.fortpaymentsdk.exceptions.FortException;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;


public class AESCipherManager {

	private final String AES_ALGORITHM = "AES";

	/**
	 * @param msg
	 * @param key
	 * @return
	 */
	public String encryptData(String msg, Key key) {
		Key keyFromKeyStore = key;
		AESCipher cipher = new AESCipher(keyFromKeyStore);
		return cipher.getEncryptedMessage(msg);
	}

	/**
	 * @param msg
	 * @param key
	 * @return
	 */
	public String decryptMsg(String msg, Key key) {
		try {
			Key keyFromKeyStore = key;
			AESCipher cipher = new AESCipher(keyFromKeyStore);
			return cipher.getDecryptedMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			return msg;
		}
	}

	/**
	 * @return
	 * @throws FortException
	 */
	public SecretKeySpec generateAESKey() throws FortException {
		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed("23Feb2016".getBytes());
			KeyGenerator kg = KeyGenerator.getInstance(AES_ALGORITHM);
			kg.init(256, sr);
			return new SecretKeySpec((kg.generateKey()).getEncoded(), AES_ALGORITHM);
		} catch (Exception e) {
			throw new FortException("failed to generate AES key", e);
		}
	}

}
