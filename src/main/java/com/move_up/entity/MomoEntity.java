package com.move_up.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "momo")
public class MomoEntity extends BaseEntity {

	@Id
	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "name")
	private String name;

	@OneToOne(mappedBy = "momo", fetch = FetchType.EAGER)
	private UserEntity user;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
