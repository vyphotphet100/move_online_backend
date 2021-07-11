package com.move_up.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity{

	@Id
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;

	@Column(name = "fullname")
	private String fullname;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "facebook_link")
	private String facebookLink;

	@Column(name = "facebook_name")
	private String facebookName;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "address")
	private String address;

	@Column(name = "picture", columnDefinition="TEXT")
	private String picture;
	
	@Column(name = "account_balance")
	private Integer accountBalance;
	
	@Column(name = "num_of_star")
	private Integer numOfStar;
	
	@Column(name = "num_of_coin_gift_box")
	private Integer numOfCoinGiftBox;
	
	@Column(name = "num_of_time_gift_box")
	private Integer numOfTimeGiftBox;
	
	@Column(name = "num_of_default_time")
	private Integer numOfDefaultTime;
	
	@Column(name = "num_of_travelled_time")
	private Integer numOfTravelledTime;
	
	@OneToMany(mappedBy = "referrerUser", fetch = FetchType.EAGER)
    private List<UserEntity> referredUsers = new ArrayList<UserEntity>();
	
	@ManyToOne
    @JoinColumn(name = "referrer_user")
    private UserEntity referrerUser;

	@Column(name = "commission")
	private Integer commission;
	
	@Column(name = "token_code", columnDefinition = "TEXT")
	private String tokenCode;
	
	@ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "role_code") 
    )
    private List<RoleEntity> roles = new ArrayList<RoleEntity>();
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "momo_phone_number", referencedColumnName = "phone_number")
    private MomoEntity momo;
	
	@ManyToMany
    @JoinTable(name = "perform",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "mission_id") 
    )
    private List<MissionEntity> missions = new ArrayList<MissionEntity>();
	
	@OneToMany(mappedBy = "sentUser") 
    private List<MessageEntity> sentMessages = new ArrayList<MessageEntity>();
	
	@OneToMany(mappedBy = "receivedUser") 
    private List<MessageEntity> receivedMessages = new ArrayList<MessageEntity>();
	
	@OneToMany(mappedBy = "user") 
    private List<WithdrawRequestEntity> withdrawRequests = new ArrayList<WithdrawRequestEntity>();
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFacebookLink() {
		return facebookLink;
	}

	public void setFacebookLink(String facebookLink) {
		this.facebookLink = facebookLink;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public List<UserEntity> getReferredUsers() {
		return referredUsers;
	}

	public void setReferredUsers(List<UserEntity> referredUsers) {
		this.referredUsers = referredUsers;
	}

	public UserEntity getReferrerUser() {
		return referrerUser;
	}

	public void setReferrerUser(UserEntity referrerUser) {
		this.referrerUser = referrerUser;
	}

	public String getTokenCode() {
		return tokenCode;
	}

	public void setTokenCode(String tokenCode) {
		this.tokenCode = tokenCode;
	}

	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}

	public MomoEntity getMomo() {
		return momo;
	}

	public void setMomo(MomoEntity momo) {
		this.momo = momo;
	}

	public List<MissionEntity> getMissions() {
		return missions;
	}

	public void setMissions(List<MissionEntity> missions) {
		this.missions = missions;
	}

	public List<MessageEntity> getSentMessages() {
		return sentMessages;
	}

	public void setSentMessages(List<MessageEntity> sentMessages) {
		this.sentMessages = sentMessages;
	}

	public List<MessageEntity> getReceivedMessages() {
		return receivedMessages;
	}

	public void setReceivedMessages(List<MessageEntity> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}

	public List<WithdrawRequestEntity> getWithdrawRequests() {
		return withdrawRequests;
	}

	public void setWithdrawRequests(List<WithdrawRequestEntity> withdrawRequests) {
		this.withdrawRequests = withdrawRequests;
	}

	public Integer getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(Integer accountBalance) {
		this.accountBalance = accountBalance;
	}

	public Integer getNumOfStar() {
		return numOfStar;
	}

	public void setNumOfStar(Integer numOfStar) {
		this.numOfStar = numOfStar;
	}

	public Integer getNumOfCoinGiftBox() {
		return numOfCoinGiftBox;
	}

	public void setNumOfCoinGiftBox(Integer numOfCoinGiftBox) {
		this.numOfCoinGiftBox = numOfCoinGiftBox;
	}

	public Integer getNumOfTimeGiftBox() {
		return numOfTimeGiftBox;
	}

	public void setNumOfTimeGiftBox(Integer numOfTimeGiftBox) {
		this.numOfTimeGiftBox = numOfTimeGiftBox;
	}

	public Integer getNumOfDefaultTime() {
		return numOfDefaultTime;
	}

	public void setNumOfDefaultTime(Integer numOfDefaultTime) {
		this.numOfDefaultTime = numOfDefaultTime;
	}

	public Integer getNumOfTravelledTime() {
		return numOfTravelledTime;
	}

	public void setNumOfTravelledTime(Integer numOfTravelledTime) {
		this.numOfTravelledTime = numOfTravelledTime;
	}

	public Integer getCommission() {
		return commission;
	}

	public void setCommission(Integer commission) {
		this.commission = commission;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getFacebookName() {
		return facebookName;
	}

	public void setFacebookName(String facebookName) {
		this.facebookName = facebookName;
	}
}
