/**
 * 
 */
package edu.vntu.mblog.errors;

/**
 * 
 * @author sergey
 */
public class ValidationException extends RuntimeException {
	
	private final String fieldName;
	private final String error;
	
	public ValidationException(String fldName, String error) {
		this.fieldName = fldName;
		this.error = error;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getError() {
		return error;
	}
}
