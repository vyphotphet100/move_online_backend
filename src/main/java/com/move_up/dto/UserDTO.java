package com.move_up.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDTO extends BaseDTO implements UserDetails{

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String fullname;
	private String email;
	private String facebookLink;
	private String facebookName;
	private String phoneNumber;
	private String address;
	private String picture;
	private Integer accountBalance;
	private Integer numOfStar;
	private Integer numOfCoinGiftBox;
	private Integer numOfTimeGiftBox;
	private Integer numOfDefaultTime;
	private Integer numOfTravelledTime;
    private List<String> referredUsernames = new ArrayList<String>();
    private String referrerUsername;
	private Integer commission;
	private String tokenCode;
    private List<String> roleCodes = new ArrayList<String>();
    private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
    private String momoPhoneNumber;
    private List<Long> missionIds = new ArrayList<Long>();
    private List<Long> sentMessageIds = new ArrayList<Long>();
    private List<Long> receivedMessageIds = new ArrayList<Long>();
    private List<Long> withdrawRequestIds = new ArrayList<Long>();
    
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
	public List<String> getReferredUsernames() {
		return referredUsernames;
	}
	public void setReferredUsernames(List<String> referredUsernames) {
		this.referredUsernames = referredUsernames;
	}
	public String getReferrerUsername() {
		return referrerUsername;
	}
	public void setReferrerUsername(String referrerUsername) {
		this.referrerUsername = referrerUsername;
	}
	public String getTokenCode() {
		return tokenCode;
	}
	public void setTokenCode(String tokenCode) {
		this.tokenCode = tokenCode;
	}
	public List<String> getRoleCodes() {
		return roleCodes;
	}
	public void setRoleCodes(List<String> roleCodes) {
		this.roleCodes = roleCodes;
	}
	public String getMomoPhoneNumber() {
		return momoPhoneNumber;
	}
	public void setMomoPhoneNumber(String momoPhoneNumber) {
		this.momoPhoneNumber = momoPhoneNumber;
	}
	public List<Long> getMissionIds() {
		return missionIds;
	}
	public void setMissionIds(List<Long> missionIds) {
		this.missionIds = missionIds;
	}
	public List<Long> getSentMessageIds() {
		return sentMessageIds;
	}
	public void setSentMessageIds(List<Long> sentMessageIds) {
		this.sentMessageIds = sentMessageIds;
	}
	public List<Long> getReceivedMessageIds() {
		return receivedMessageIds;
	}
	public void setReceivedMessageIds(List<Long> receivedMessageIds) {
		this.receivedMessageIds = receivedMessageIds;
	}
	public List<Long> getWithdrawRequestIds() {
		return withdrawRequestIds;
	}
	public void setWithdrawRequestIds(List<Long> withdrawRequestIds) {
		this.withdrawRequestIds = withdrawRequestIds;
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
	public Integer getCommission() {
		return commission;
	}
	public void setCommission(Integer commission) {
		this.commission = commission;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authorities;
	}
	
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
    
    
}
