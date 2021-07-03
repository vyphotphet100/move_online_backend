package com.move_up.api.socket;

import com.move_up.dto.MessageSocketDTO;
import com.move_up.service.socket.IMissionSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class MissionSocketAPI {
    @Autowired
    private IMissionSocketService missionSocketService;

    @MessageMapping("/view_ads")
    public void viewAds(@Payload MessageSocketDTO messageSocketDto) throws IOException {
        missionSocketService.viewAds(messageSocketDto);
    }

    @MessageMapping("/view_ads/done")
    public void viewAdsDone(@Payload MessageSocketDTO messageSocketDto) throws IOException {
        missionSocketService.viewAds(messageSocketDto);
    }
}
