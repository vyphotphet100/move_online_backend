package com.move_up.dto;

import java.util.ArrayList;
import java.util.List;

public class RoleDTO extends BaseDTO{

	private String code;
	private String name;
    private List<String> usernames = new ArrayList<String>();
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getUsernames() {
		return usernames;
	}
	public void setUsernames(List<String> usernames) {
		this.usernames = usernames;
	}
    
    
}
