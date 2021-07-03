package com.move_up.service.socket;

import com.move_up.dto.BaseDTO;
import com.move_up.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;

public interface IBaseSocketService {
    BaseDTO ExceptionObject(BaseDTO dto, String message);
}
