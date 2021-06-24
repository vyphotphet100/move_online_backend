package com.move_up.service;

import javax.servlet.http.HttpServletRequest;

import com.move_up.dto.BaseDTO;
import com.move_up.entity.UserEntity;

public interface IBaseService {
	BaseDTO ExceptionObject(BaseDTO dto, String message);
	UserEntity getRequestedUser (HttpServletRequest request);
}
