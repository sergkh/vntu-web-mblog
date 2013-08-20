package edu.vntu.mblog.domain;

import java.util.Date;

public class User {
	private long id;
	private Date creationDate;
	private String login;
	private String email;
	private String passHash;
	private Date blockDate;

	public User(long id, Date creationDate, String login, String email,
			String passHash, Date blockDate) {
		super();
		this.id = id;
		this.creationDate = creationDate;
		this.login = login;
		this.email = email;
		this.passHash = passHash;
		this.blockDate = blockDate;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassHash() {
		return passHash;
	}
	public void setPassHash(String passHash) {
		this.passHash = passHash;
	}
	
	public Date getBlockDate() {
		return blockDate;
	}
	
	public void setBlockDate(Date blockDate) {
		this.blockDate = blockDate;
	}
	
}
