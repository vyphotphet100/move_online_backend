package com.move_up.service.management;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.move_up.dto.UserDTO;

public interface IUserService extends IBaseService{

	UserDTO findAll();
	UserDTO findOne(String username, HttpServletRequest request, String userStr);
	UserDTO getCurrentUser(HttpServletRequest request);
	UserDTO save(UserDTO userDto);
	UserDTO update(UserDTO userDto, HttpServletRequest request);
	UserDTO delete(String username);
	UserDTO logout(HttpServletRequest request, HttpServletResponse response);

	// action
	UserDTO changePassword(UserDTO userDto, HttpServletRequest request);
	UserDTO checkFacebookAccByPostLink(UserDTO userDto, HttpServletRequest request);
	UserDTO saveFacebookAccByPostLink(UserDTO userDto, HttpServletRequest request);
	
	// mission
	UserDTO exchangeTimeGiftBoxByStar(HttpServletRequest request);
	UserDTO exchangeCoinGiftBoxByStar(HttpServletRequest request);
	UserDTO exchangeShirtByStar(HttpServletRequest request);
	
	// reference
	UserDTO getReferredUser(HttpServletRequest request);
	UserDTO saveReferrerUser(HttpServletRequest request, UserDTO userDto);
}
