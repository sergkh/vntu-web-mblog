package edu.vntu.mblog.domain;

import java.util.Date;

public class Post {
	
	private long id;
	private String authorLogin;
	private String text;
	private Date date;
	private Date confirmDate;
	private boolean confirmed;
	
	public Post() {}

	public Post(long id, String authorLogin, String text, Date date) {
		this();
		this.id = id;
		this.authorLogin = authorLogin;
		this.text = text;
		this.date = date;
		this.confirmDate=null;
		confirmed=false;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getAuthorLogin() {
		return authorLogin;
	}
	
	public void setAuthorLogin(String authorLogin) {
		this.authorLogin = authorLogin;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", authorLogin=" + authorLogin + ", text="
				+ text + ", date=" + date + ", confirmDate=" + confirmDate + "]";
	}
	
	

}
