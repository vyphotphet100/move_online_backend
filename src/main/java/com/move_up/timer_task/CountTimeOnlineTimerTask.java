package com.move_up.timer_task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.move_up.dto.MessageSocketDTO;
import com.move_up.entity.UserEntity;
import com.move_up.global.Global;
import com.move_up.repository.UserRepository;
import com.move_up.utils.MyUtil;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.TimerTask;

public class CountTimeOnlineTimerTask extends TimerTask {
    private int _count = 0;
    private int _countOnPage = 0;

    private String username = "";
    private String channel = "";
    private SimpMessagingTemplate messageTmp;
    private UserRepository userRepo;

    public CountTimeOnlineTimerTask() {

    }

    public CountTimeOnlineTimerTask(String username, String channel, SimpMessagingTemplate messageTmp, UserRepository userRepo) {
        this.username = username;
        this.channel = channel;
        this.messageTmp = messageTmp;
        this.userRepo = userRepo;
    }

    @Override
    public void run(){
        if (MyUtil.getUserAttribute(username, "fbStatus").equals("online")) {
            _count++;
        } else if (MyUtil.getUserAttribute(username, "fbStatus").equals("checking")) {
            _count = 0;
        } else {
            _count = 0;
            Global.requestedUsernamesList.remove(username);
            cancel();
            return;
        }

        if (MyUtil.getUserAttribute(username, "onPage").equals("false")) {
            _countOnPage++;
            messageTmp.convertAndSend(this.channel, "Are you online?");
        } else
            _countOnPage = 0;

        if (_countOnPage == 30) {
            _count = 0;
            _countOnPage = 0;
            MyUtil.setUserAttribute(username, "fbStatus", "offline");
            return;
        }


        if (_count == 60) {
            _count = 0;
            UserEntity userEntity = userRepo.findOne(username);
            userEntity.setNumOfDefaultTime(userEntity.getNumOfDefaultTime() - 1);
            userEntity.setNumOfTravelledTime(userEntity.getNumOfTravelledTime() + 1);
            userRepo.save(userEntity);
            MyUtil.setUserAttribute(username, "fbStatus", "checking");
            MyUtil.setUserAttribute(username, "onPage", "false");

            ObjectMapper mapper = new ObjectMapper();
            MessageSocketDTO messageSocketDTO = new MessageSocketDTO();
            messageSocketDTO.setType(MessageSocketDTO.MessageType.INCREASE_MINUTE);
            messageSocketDTO.setReceiver(username);
            Content content = new Content();
            content.setNumOfMinute(1);
            content.setMessage("Increase 1 minute.");
            try {
                messageSocketDTO.setContent(mapper.writeValueAsString(content));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            messageTmp.convertAndSend(this.channel, messageSocketDTO);
        }
    }

    private static class Content {
        private Integer numOfMinute;
        private String message;

        public Integer getNumOfMinute() {
            return numOfMinute;
        }

        public void setNumOfMinute(Integer numOfMinute) {
            this.numOfMinute = numOfMinute;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
