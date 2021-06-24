package com.move_up.dto;

public class MessageDTO extends BaseDTO{
	
	private Long id;
	private String content;
    private String sentUsername;
    private String receivedUsername;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSentUsername() {
		return sentUsername;
	}
	public void setSentUsername(String sentUsername) {
		this.sentUsername = sentUsername;
	}
	public String getReceivedUsername() {
		return receivedUsername;
	}
	public void setReceivedUsername(String receivedUsername) {
		this.receivedUsername = receivedUsername;
	}
    
    
}
