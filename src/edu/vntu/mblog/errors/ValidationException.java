/**
 * 
 */
package edu.vntu.mblog.errors;

/**
 * 
 * @author sergey
 */
public class ValidationException extends Exception {
	private static final long serialVersionUID = 8074360044012276675L;
	
	private final String fieldName;
	
	public ValidationException(String fldName, String message) {
		super(message);
		this.fieldName = fldName;
	}

	public String getFieldName() {
		return fieldName;
	}
}
