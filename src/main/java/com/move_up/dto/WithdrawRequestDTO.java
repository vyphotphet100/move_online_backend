package com.move_up.dto;

import java.util.Date;

public class WithdrawRequestDTO extends BaseDTO{

	private Long id;
	private String username;
	private String type;
	private Integer amountOfMoney;
	private String status;
	private Date paymentDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getAmountOfMoney() {
		return amountOfMoney;
	}
	public void setAmountOfMoney(Integer amountOfMoney) {
		this.amountOfMoney = amountOfMoney;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	
}
