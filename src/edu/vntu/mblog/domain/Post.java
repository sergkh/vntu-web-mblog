package edu.vntu.mblog.domain;

import java.util.Date;

public class Post {
	
	private  long entryId;
	private long ownerId;
	private String text;
	private Date date;
	
	public Post(long ownerId, String text, Date date) {
		this(0L, ownerId, text, date);
	}
	
	public Post(long entryId, long ownerId, String text, Date date) {
		this.entryId = entryId;
		this.ownerId = ownerId;
		this.text = text;
		this.date = date;
	}

	public long getentryId() {
		return entryId;
	}

	public void setentryId(long entryId) {
		this.entryId = entryId;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
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
	
	

}
