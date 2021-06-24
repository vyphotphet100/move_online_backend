package com.move_up.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "withdraw_request")
public class WithdrawRequestEntity extends BaseEntity{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne 
    @JoinColumn(name = "username")
    private UserEntity user;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "amount_of_money")
	private Integer amountOfMoney;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "payment_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date paymentDate;

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
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
