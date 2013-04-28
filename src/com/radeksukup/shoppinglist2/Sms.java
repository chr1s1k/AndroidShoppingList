/**
 * 
 */
package com.radeksukup.shoppinglist2;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Radek Sukup
 *
 */
public class Sms {

	private int id;
	private int threadId;
	private String sender;
	private String body;
	private long date;

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getThreadId() {
		return threadId;
	}
	
	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
	
	public String getSender() {
		
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getDate() {
		if (new SimpleDateFormat("d.M.y").format(new Date(date)).equals(new SimpleDateFormat("d.M.y").format(new Date()))) {
			return new SimpleDateFormat("k:mm").format(new Date(date)); // returns only hour and minute
		} else {
			return new SimpleDateFormat("d.M.").format(new Date(date)); // return date
		}
	}
	
	public void setDate(long date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return body.length() > 30 ? body.substring(0, 30) + "..." : body; // returns only part of sms body if is too long
	}
	
}
