package edu.vntu.mblog.domain;

import java.util.Date;

public class Post {
	
	
	public enum State {
		CONFIRMED, DISABLED, UNVALIDATED;
	}
	
	private long id;
	private String authorLogin;
    private String authorAvatar;
	private String text;
	private Date date;
	private Date confirmDate;
	private State state;
	
	public Post() {}

	public Post(long id, String authorLogin, String authorAvatar, String text, Date date) {
		this();
		this.id = id;
		this.authorLogin = authorLogin;
        this.authorAvatar = authorAvatar;
		this.text = text;
		this.date = date;
		this.confirmDate=null;
		this.state = State.UNVALIDATED;
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
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }
}
