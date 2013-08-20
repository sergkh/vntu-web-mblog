package edu.vntu.mblog.domain;

import java.util.Date;
import java.util.EnumSet;

public class User {
	
	public enum Roles {
		USER, ADMIN, MODERATOR;
	}
	
	private long id;
	private Date creationDate;
	private String login;
	private String email;
	private String passHash;
	private Date blockDate;
	private EnumSet<Roles> roles;
	
	public User(String login, String email, String passHash) {
		this(0L, null, login, email, passHash, null);
	}
	
	public User(long id, Date creationDate, String login, String email,
			String passHash, Date blockDate) {
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

	public EnumSet<Roles> getRoles() {
		return roles;
	}

	public void setRoles(EnumSet<Roles> roles) {
		this.roles = roles;
	}
	
}
