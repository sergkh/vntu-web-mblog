/**
 * 
 */
package edu.vntu.mblog.errors;

/**
 * 
 * @author sergey
 */
public class AuthenticationExeption extends Exception {
	private static final long serialVersionUID = -4722630362611558281L;

	public AuthenticationExeption(String message) {
		super(message);
	}
}
