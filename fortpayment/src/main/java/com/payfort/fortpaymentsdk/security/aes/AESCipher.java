package com.payfort.fortpaymentsdk.security.aes;

import com.google.common.base.Throwables;
import com.google.common.io.BaseEncoding;

import org.bouncycastle.util.encoders.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCipher {

	private static final String ALGORITHM_AES256 = "AES/CBC/PKCS5Padding";
	private static final byte[] INITIAL_IV = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private final SecretKeySpec secretKeySpec;
	private final Cipher cipher;
	private IvParameterSpec iv;

	/**
	 * Create AESCipher based on existing {@link Key}
	 *
	 * @param key Key
	 */
	public AESCipher(Key key) {
		this(key.getEncoded());
	}

	/**
	 * Create AESCipher based on existing {@link Key} and Initial Vector (iv) in bytes
	 *
	 * @param key Key
	 */
	public AESCipher(Key key, byte[] iv) {
		this(key.getEncoded(), iv);
	}

	/**
	 * <p>Create AESCipher using a byte[] array as a key</p>
	 * <p/>
	 * <p><strong>NOTE:</strong> Uses an Initial Vector of 16 0x0 bytes. This should not be used to create strong security.</p>
	 *
	 * @param key Key
	 */
	public AESCipher(byte[] key) {
		this(key, INITIAL_IV);
	}

	private AESCipher(byte[] key, byte[] iv) {
		try {
			this.secretKeySpec = new SecretKeySpec(key, "AES");
			this.iv = new IvParameterSpec(iv);
			this.cipher = Cipher.getInstance(ALGORITHM_AES256);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Takes message and encrypts with Key
	 *
	 * @param message String
	 * @return String Base64 encoded
	 */
	public String getEncryptedMessage(byte[] message) {
		try {
			Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);

			byte[] encryptedTextBytes = cipher.doFinal(message);

			return BaseEncoding.base64().encode(encryptedTextBytes);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	public String getEncryptedMessage(String message) {
		try {
			Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);

			byte[] encryptedTextBytes = cipher.doFinal(message.getBytes("UTF-8"));

			return BaseEncoding.base64().encode(encryptedTextBytes);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Takes Base64 encoded String and decodes with provided key
	 *
	 * @param message String encoded with Base64
	 * @return String
	 */
	public String getDecryptedMessage(byte[] message) {
		try {
			Cipher cipher = getCipher(Cipher.DECRYPT_MODE);

			// byte[] encryptedTextBytes = BaseEncoding.base64().decode(message);
			byte[] encryptedTextBytes = message;
			byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

			return new String(decryptedTextBytes);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	public String getDecryptedMessage(String message) {
		try {
			Cipher cipher = getCipher(Cipher.DECRYPT_MODE);

			byte[] encryptedTextBytes =  Base64.decode(message.getBytes("utf-8"));
			return new String(cipher.doFinal(encryptedTextBytes));
		} catch (Exception e) {
			return message;
			//throw Throwables.propagate(e);
		}
	}

	/**
	 * Get IV in Base64 Encoded String
	 *
	 * @return String Base64 Encoded
	 */
	public String getIV() {
		return BaseEncoding.base64().encode(iv.getIV());
	}

	/**
	 * Base64 encoded version of key
	 *
	 * @return String
	 */
	public String getKey() {
		return getKey(KeyEncoding.BASE64);
	}

	public String getKey(KeyEncoding encoding) {
		String result = null;
		switch (encoding) {
			case BASE64:
				result = BaseEncoding.base64().encode(secretKeySpec.getEncoded());
				break;
			case HEX:
				result = BaseEncoding.base16().encode(secretKeySpec.getEncoded());
				break;
			case BASE32:
				result = BaseEncoding.base32().encode(secretKeySpec.getEncoded());
				break;
		}

		return result;
	}

	private Cipher getCipher(int encryptMode) throws InvalidKeyException, InvalidAlgorithmParameterException {
		cipher.init(encryptMode, getSecretKeySpec(), iv);
		return cipher;
	}

	private SecretKeySpec getSecretKeySpec() {
		return secretKeySpec;
	}

}
