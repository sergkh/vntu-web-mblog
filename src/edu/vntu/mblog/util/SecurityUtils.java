/**
 * 
 */
package edu.vntu.mblog.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * @author sergey
 */
public class SecurityUtils {
	
	/**
	 * Calculates digest from specified message.
	 * Thread safe.
	 * 
	 * @param message message to find digest for.
	 * @return message digest.
	 */
	public static String digest(String message) {
		try {
			byte[] bytesOfMessage = message.getBytes("UTF-8");
			final MessageDigest digest = MessageDigest.getInstance("SHA-1");
			return byteArray2Hex(digest.digest(bytesOfMessage));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
    private static String byteArray2Hex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
