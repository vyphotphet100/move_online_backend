package com.move_up.service.socket.impl;

import com.move_up.converter.Converter;
import com.move_up.dto.BaseDTO;
import com.move_up.entity.UserEntity;
import com.move_up.repository.UserRepository;
import com.move_up.service.socket.IBaseSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class BaseSocketService implements IBaseSocketService {
    @Autowired
    protected Converter converter;

    @Override
    public BaseDTO ExceptionObject(BaseDTO dto, String message) {
        dto.setHttpStatus(HttpStatus.FORBIDDEN);
        dto.setMessage(message);
        return dto;
    }
}
