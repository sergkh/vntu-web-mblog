package edu.vntu.mblog.errors;

/**
 * 
 * @author sergey
 */
public class UserNotFoundException extends Exception {
	private static final long serialVersionUID = -8177466008360293587L;

	private final String identifier;
	
	public UserNotFoundException(String identifier, String message) {
		super(message);
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return identifier;
	}
}
