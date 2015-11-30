package labs.json;

public class UserExistenceResponse {
	private boolean exists;

	public UserExistenceResponse() {
		super();
	}

	public UserExistenceResponse(boolean exists) {
		super();
		this.exists = exists;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}
	
	
}
