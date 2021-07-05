package com.move_up.service.socket.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.move_up.dto.MessageSocketDTO;
import com.move_up.global.Global;
import com.move_up.service.socket.ICheckFbStatus;
import com.move_up.utils.MyUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CheckFbStatus extends BaseSocketService implements ICheckFbStatus {

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
            if (MyUtil.getUserAttribute(username, "fbStatus").equals("checking"))
                MyUtil.setUserAttribute(username, "fbStatus", "online");
        }

        if (messageSocketDTO.getType().equals(MessageSocketDTO.MessageType.FB_INACTIVE)) {
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
