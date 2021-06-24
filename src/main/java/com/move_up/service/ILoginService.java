package com.move_up.service;

import javax.servlet.http.HttpServletRequest;

import com.move_up.dto.UserDTO;

public interface ILoginService {
	UserDTO login(String username, String password);
	UserDTO logout();
	UserDTO authorization(HttpServletRequest request, String wholeRequestURL);
	
}
