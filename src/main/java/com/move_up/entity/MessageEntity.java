package com.move_up.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "message")
public class MessageEntity extends BaseEntity{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "content", columnDefinition = "TEXT")
	private String content;
	
	@ManyToOne 
    @JoinColumn(name = "sent_username")
    private UserEntity sentUser;
	
	@ManyToOne 
    @JoinColumn(name = "received_username")
    private UserEntity receivedUser;

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

	public UserEntity getSentUser() {
		return sentUser;
	}

	public void setSentUser(UserEntity sentUser) {
		this.sentUser = sentUser;
	}

	public UserEntity getReceivedUser() {
		return receivedUser;
	}

	public void setReceivedUser(UserEntity receivedUser) {
		this.receivedUser = receivedUser;
	}
	
	
}
