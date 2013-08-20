/**
 * 
 */
package edu.vntu.mblog.util;

import java.util.regex.Pattern;

import edu.vntu.mblog.errors.ValidationException;

/**
 * 
 * @author sergey
 */
public class ValidationUtils {
    
	private static final Pattern EMAIL_VALIDATION_PATTERN = 
    		Pattern.compile("^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@" +
    						"(?:[a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*(?:aero|arpa|asia|biz|cat|" +
    						"com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])$");
    
	
	public static void validateLen(String fieldName, String value, int min, int max) {
		if(!checkLen(value, min, max)) 
			throw new ValidationException(fieldName, "Length has to be from " + min + " to " + max + " characters");
			
	}
	
	public static void validateEmail(String fieldName, String email) {
		if(!checkEmail(email)) 
			throw new ValidationException(fieldName, "Is not a valid email");
	}
	
	/**
	 * Checks that string length fits in specified restrictions.
	 * Min and max length checked inclusively.
	 * If string is null false is returned.
	 * 
	 * @param str string to check.
	 * @param min minimal string length (inclusively).
	 * @param max maximum string length (inclusively).
	 * @return true is string has limited length.
	 */
	public static boolean checkLen(String str, int min, int max) {
		if(str == null) return false;
		final int len = str.length();
		return len >= min && len <= max; 
	}
	
	/**
	 * Email validation by reg expression.
	 * @param email string containing email to validate
	 * @return true if email is valid.
	 */
	public static boolean checkEmail(String email) {
		if(email == null) return false;
		int len = email.length(); 
		return  len > 4 && len < 512 && EMAIL_VALIDATION_PATTERN.matcher(email.toLowerCase()).matches();
	}
}
