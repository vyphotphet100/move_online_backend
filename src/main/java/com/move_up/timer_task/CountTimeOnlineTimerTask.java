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
    private int _countIncreCoinGiftBox = 0;
    private int _defaultTimeIncreCoinGiftBox = 1;

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

        UserEntity userEntity = userRepo.findOne(username);
        if (_count == 60 && userEntity.getNumOfDefaultTime() > 0) {
            _count = 0;
            _countIncreCoinGiftBox++;
            userEntity.setNumOfDefaultTime(userEntity.getNumOfDefaultTime() - 1);
            userEntity.setNumOfTravelledTime(userEntity.getNumOfTravelledTime() + 1);
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

            // check increase coin gift box
            if (_countIncreCoinGiftBox == _defaultTimeIncreCoinGiftBox) {
                _countIncreCoinGiftBox = 0;
                messageSocketDTO.setType(MessageSocketDTO.MessageType.INCREASE_COIN_GIFT_BOX);
                messageSocketDTO.setReceiver(username);
                content = new Content();
                content.setNumOfMinute(null);
                content.setMessage("Increase 1 coin gift box.");
                try {
                    messageSocketDTO.setContent(mapper.writeValueAsString(content));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                userEntity.setNumOfCoinGiftBox(userEntity.getNumOfCoinGiftBox() + 1);
                messageTmp.convertAndSend(this.channel, messageSocketDTO);

                // get random number of default increasing coin gift box value
                int max = 1;
                int min = 5;
                _defaultTimeIncreCoinGiftBox = (int) Math.floor(Math.random() * (max - min + 1) + min);
            }

            userRepo.save(userEntity);
        }

        if (userEntity.getNumOfDefaultTime() == 0)
            _count = 0;
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
