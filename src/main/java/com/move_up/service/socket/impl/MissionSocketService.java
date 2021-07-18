package com.move_up.service.socket.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.move_up.dto.MessageSocketDTO;
import com.move_up.entity.MissionEntity;
import com.move_up.entity.UserEntity;
import com.move_up.global.Global;
import com.move_up.repository.MissionRepository;
import com.move_up.repository.UserRepository;
import com.move_up.service.socket.IMissionSocketService;
import com.move_up.timer_task.ViewAdsTimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Timer;

@Service
public class MissionSocketService extends BaseSocketService implements IMissionSocketService {

    @Autowired
    private MissionRepository missionRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SimpMessagingTemplate messageTmp;

    @Override
    public void viewAds(MessageSocketDTO messageSocketDto) throws IOException {
        UserEntity userEntity = userRepo.findOneByTokenCode(messageSocketDto.getToken());
        String userAttr = Global.userAttributes.get(userEntity.getUsername());
        ObjectMapper objectMapper = new ObjectMapper();

        if (messageSocketDto.getType().equals(MessageSocketDTO.MessageType.EXCEPTION)) {
            if (userAttr == null)
                return;
            if (userAttr.contains("view-ads:stopped;"))
                return;
            if (userAttr.contains("view-ads:counting;")) {
                Global.userAttributes.put(userEntity.getUsername(),
                        Global.userAttributes.get(userEntity.getUsername()).replace(
                                "view-ads:counting;",
                                "view-ads:stopped;"));
                return;
            }
        }

        if (messageSocketDto.getType().equals(MessageSocketDTO.MessageType.NOTIFICATION)) {
            if (userAttr == null) {
                Global.userAttributes.put(userEntity.getUsername(), "view-ads:counting;");
            }

            if (userAttr != null) {
                if (userAttr.contains("view-ads:counting;"))
                    return;
                else if (userAttr.contains("view-ads:stopped;"))
                    Global.userAttributes.put(userEntity.getUsername(),
                            userAttr.replace( "view-ads:stopped;","view-ads:counting;"));
                else if (!userAttr.contains("view-ads"))
                    Global.userAttributes.put(userEntity.getUsername(), userAttr + "view-ads:counting;");
            }

        }

        ViewAdsMission viewAdsMission = objectMapper.readValue((String) messageSocketDto.getContent(), ViewAdsMission.class);
        MissionEntity missionEntity = missionRepo.findById(viewAdsMission.getId()).get();

        String channel = "/channel/" + userEntity.getUsername() + "/view_ads";
        Integer missionTime = missionEntity.getTime();
        ViewAdsTimerTask viewAdsTimerTask = new ViewAdsTimerTask(channel, missionTime, userEntity.getUsername(), messageTmp);

        Timer timer = new Timer();
        timer.schedule(viewAdsTimerTask, 0, 1000);
    }

    @Override
    public void viewAdsDone(MessageSocketDTO messageSocketDto) {

    }

    private static class ViewAdsMission {
        private Long id;
        private Integer time;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getTime() {
            return time;
        }

        public void setTime(Integer time) {
            this.time = time;
        }
    }

    private static class ExceptionMessage {
        private String code;
        private String description;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
