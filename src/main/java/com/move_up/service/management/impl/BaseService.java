package com.move_up.service.management.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.move_up.converter.Converter;
import com.move_up.dto.BaseDTO;
import com.move_up.entity.UserEntity;
import com.move_up.repository.UserRepository;
import com.move_up.service.management.IBaseService;

@Service
public class BaseService implements IBaseService{

	@Autowired
	protected Converter converter;
	
	@Autowired
	protected UserRepository userRepo;
	
	@Override
	public BaseDTO ExceptionObject(BaseDTO dto, String message) {
		dto.setHttpStatus(HttpStatus.FORBIDDEN);
		dto.setMessage(message);
		return dto;
	}

	@Override
	public UserEntity getRequestedUser(HttpServletRequest request) {
		if (request.getHeader("Authorization") != null) {
			String authorizationHeader = request.getHeader("Authorization");
			if (!authorizationHeader.equals("") && authorizationHeader.startsWith("Token ")) {
				String token = authorizationHeader.substring(6);
				return userRepo.findOneByTokenCode(token);
			}
		}

		return null;
	}
	
	
}
