package com.move_up.service.management.impl;

import javax.servlet.http.HttpServletRequest;

import com.move_up.global.Global;
import com.move_up.timer_task.CountTimeOnlineTimerTask;
import com.move_up.utils.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.move_up.converter.Converter;
import com.move_up.dto.BaseDTO;
import com.move_up.entity.UserEntity;
import com.move_up.repository.UserRepository;
import com.move_up.service.management.IBaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

@Service
public class BaseService implements IBaseService {

    @Autowired
    protected Converter converter;

    @Autowired
    protected UserRepository userRepo;

    @Autowired
    private SimpMessagingTemplate messageTmp;

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
                UserEntity userEntity = userRepo.findOneByTokenCode(token);

                if (userEntity == null)
                    return null;

                // add username to global requested username list for checking fbStatus
                if (!Global.requestedUsernamesList.contains(userEntity.getUsername())) {
                    Global.requestedUsernamesList.add(userEntity.getUsername());
                    MyUtil.setUserAttribute(userEntity.getUsername(), "fbStatus", "checking");
                    MyUtil.setUserAttribute(userEntity.getUsername(), "onPage", "true");

                    Timer timer = new Timer();
                    CountTimeOnlineTimerTask countTime = new CountTimeOnlineTimerTask(userEntity.getUsername(),
                            "/channel/" + userEntity.getUsername(), messageTmp, userRepo);
                    timer.schedule(countTime, 0, 1000);
                }
                return userEntity;
            }
        }

        return null;
    }


}
