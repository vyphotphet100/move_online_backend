package com.move_up.service.socket.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.move_up.dto.MessageSocketDTO;
import com.move_up.global.Global;
import com.move_up.service.socket.ICheckFbStatus;
import com.move_up.utils.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CheckFbStatus extends BaseSocketService implements ICheckFbStatus {
    @Autowired
    private SimpMessagingTemplate messageTmp;

    @Override
    public void checkFbStatus(MessageSocketDTO messageSocketDTO) {
        // get username
        ObjectMapper mapper = new ObjectMapper();
        String username = null;
        try {
            username = mapper.readValue((String)messageSocketDTO.getContent(), Content.class).getUsername();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (username == null)
            return;

        if (messageSocketDTO.getType().equals(MessageSocketDTO.MessageType.FB_ACTIVE)) {
            if (MyUtil.getUserAttribute(username, "fbStatus").equals("checking")) {
                MessageSocketDTO messageSendToUser = new MessageSocketDTO();
                messageSendToUser.setReceiver(username);
                messageSendToUser.setType(MessageSocketDTO.MessageType.FB_ACTIVE);
                Content content = new Content();
                content.setMessage("You are online");
                messageSendToUser.setContent(content);
                messageTmp.convertAndSend("/channel/" + username, messageSendToUser);

                MyUtil.setUserAttribute(username, "fbStatus", "online");
            }

        }

        if (messageSocketDTO.getType().equals(MessageSocketDTO.MessageType.FB_INACTIVE)) {
            MessageSocketDTO messageSendToUser = new MessageSocketDTO();
            messageSendToUser.setReceiver(username);
            messageSendToUser.setType(MessageSocketDTO.MessageType.FB_INACTIVE);
            Content content = new Content();
            content.setMessage("You are offline");
            messageSendToUser.setContent(content);
            messageTmp.convertAndSend("/channel/" + username, messageSendToUser);

            MyUtil.setUserAttribute(username, "fbStatus", "offline");
            Global.requestedUsernamesList.remove(username);
        }

        if (messageSocketDTO.getType().equals(MessageSocketDTO.MessageType.ON_PAGE)) {
            if (MyUtil.getUserAttribute(username, "fbStatus").equals("checking")) {
                MyUtil.setUserAttribute(username, "fbStatus", "online");
                MyUtil.setUserAttribute(username, "onPage", "true");
            }
        }
    }

    private static class Content {
        private String username;
        private String message;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
