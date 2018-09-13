package com.ai.ecs.ecm.mall.wap.platform.utils;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public final class RSAUtils {

	private static final String ALGORITHM = "RSA";

	private static final String PKCS8_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKMSMHndMOr1TIzcFndXar2L7iWTgRb2TyLw1bXXDw/W/0CJXWz1VKY/dwe2pabqXHsRTi1hHi9nVyaWHQ5c8om3AzHj3cr8CW6IjmhuJS5S2+AdPzlw9Nf3xErxrwGTGSV5jO6xzIkjm6iIXfPtXHne9NcbxXiVncrMcFOaLCMNAgMBAAECgYAD0cY+5HZj2nD7j6AbFXoTjHZ8fNL2NbiuydNHmgzpQxDhcxY0Gh7sceoYzSdeHHkkDMi2+WJam+IHrlZp4rVviKN7W6AjV3ZkhLLlII0tRiLRReUDMYftzzywTZbo5YMG9JRt39GL9wE8nnsaO7b0jHTIWT3SDrvMSwHXKRJ25QJBAO0PZY0ny0h2LUPAv48PlDN5ZGtcq6ZfcrMYOflO2HbOogkW9308VcHe1/FXNJ6PtoIjnUnyxit3cWtnnAOKMj8CQQCwGX1vpP5EYWRxbiBf83E14lTiXnQEbyWy/oeq9NfSFrw1W7TJlxMaKEfhnw+OpLFy8XIvhcOABX1vWr4bcr+zAkEA0J1RB7QyT3U4Bjy0FqhmChpUxapKn+G1JWg3dG7vTTYwIAGnD/2tliuOKyNL+hGMUeAXhcDwpcW5+QO3puHRrwJANqmC5T/q6WDt48PSatZPQvGhda7qBmJV6mzVwfxEbeM+wrVXteeeN1VfkqpkEtwOdOZ7kkLMP5X0rLcXIcAJvwJBAMm0spPwD+bKVQhLg9ruxQliRhf3yWXdviSAvCXTZWMmdsH3rucgcaDeuCYuM4ymxQVThbV/tJjKeKi3bvrviqk=";

	private static final PrivateKey PRIVATE_KEY = initPrivateKey(
		"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKMSMHndMOr1TIzcFndXar2L7iWTgRb2TyLw1bXXDw/W/0CJXWz1VKY/dwe2pabqXHsRTi1hHi9nVyaWHQ5c8om3AzHj3cr8CW6IjmhuJS5S2+AdPzlw9Nf3xErxrwGTGSV5jO6xzIkjm6iIXfPtXHne9NcbxXiVncrMcFOaLCMNAgMBAAECgYAD0cY+5HZj2nD7j6AbFXoTjHZ8fNL2NbiuydNHmgzpQxDhcxY0Gh7sceoYzSdeHHkkDMi2+WJam+IHrlZp4rVviKN7W6AjV3ZkhLLlII0tRiLRReUDMYftzzywTZbo5YMG9JRt39GL9wE8nnsaO7b0jHTIWT3SDrvMSwHXKRJ25QJBAO0PZY0ny0h2LUPAv48PlDN5ZGtcq6ZfcrMYOflO2HbOogkW9308VcHe1/FXNJ6PtoIjnUnyxit3cWtnnAOKMj8CQQCwGX1vpP5EYWRxbiBf83E14lTiXnQEbyWy/oeq9NfSFrw1W7TJlxMaKEfhnw+OpLFy8XIvhcOABX1vWr4bcr+zAkEA0J1RB7QyT3U4Bjy0FqhmChpUxapKn+G1JWg3dG7vTTYwIAGnD/2tliuOKyNL+hGMUeAXhcDwpcW5+QO3puHRrwJANqmC5T/q6WDt48PSatZPQvGhda7qBmJV6mzVwfxEbeM+wrVXteeeN1VfkqpkEtwOdOZ7kkLMP5X0rLcXIcAJvwJBAMm0spPwD+bKVQhLg9ruxQliRhf3yWXdviSAvCXTZWMmdsH3rucgcaDeuCYuM4ymxQVThbV/tJjKeKi3bvrviqk=");

	private final static String ENCODE = "GBK";

	private static final PrivateKey initPrivateKey(String pkcs8key) {

		byte[] privKeyByte = Base64.decode(pkcs8key);
		PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privKeyByte);
		KeyFactory kf = null;
		PrivateKey privKey = null;
		try {
			kf = KeyFactory.getInstance("RSA");
			privKey = kf.generatePrivate(privKeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return privKey;
	}

	public static final String decrypt(String text) {

		byte[] encryptByte = Base64.decode(getURLDecoderString(text));
		byte[] dectyptedText = (byte[]) null;
		String result = "";
		try {
			Cipher cipher = Cipher.getInstance("RSA");

			cipher.init(2, PRIVATE_KEY);
			dectyptedText = cipher.doFinal(encryptByte);
			result = new String(dectyptedText);

			result = result.substring(7, result.length() - 14);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * getURLDecoderString URL 解码
	 * @param str
	 * @return
	 * @return String返回说明
	 * @Exception 异常说明
	 * @author：shubo@asiainfo.com
	 * @create：2017年2月22日 上午11:18:15 
	 * @moduser： 
	 * @moddate：
	 * @remark：
	 */
	public static String getURLDecoderString(String str) {

		String result = "";
		if (null == str) {
			return "";
		}
		try {
			result = java.net.URLDecoder.decode(str, ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
}
