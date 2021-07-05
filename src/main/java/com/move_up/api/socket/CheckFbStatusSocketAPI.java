package com.move_up.api.socket;

import com.move_up.dto.MessageSocketDTO;
import com.move_up.service.socket.ICheckFbStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckFbStatusSocketAPI {
    @Autowired
    private ICheckFbStatus checkFbStatus;

    @MessageMapping("/send-fb-status")
    public void sendFbStatus(@Payload MessageSocketDTO messageSocketDTO) {
        checkFbStatus.checkFbStatus(messageSocketDTO);
    }
}
