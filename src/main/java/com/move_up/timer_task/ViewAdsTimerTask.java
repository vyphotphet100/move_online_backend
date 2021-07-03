package com.move_up.timer_task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.move_up.dto.MessageSocketDTO;
import com.move_up.global.Global;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class ViewAdsTimerTask extends TimerTask {
    public ViewAdsTimerTask() {

    }

    public ViewAdsTimerTask(String channel, Integer time, String receiver, SimpMessagingTemplate messageTmp) {
        this.channel = channel;
        this.time = time;
        this.receiver = receiver;
        this.messageTmp = messageTmp;
    }

    private SimpMessagingTemplate messageTmp;
    private String channel;
    private Integer time;
    private String receiver;
    private Integer _count = 0;

    @Override
    public void run() {
        String userAttr = Global.userAttributes.get(receiver);
        if (userAttr == null || !userAttr.contains("view-ads:counting;"))
            cancel();

        ObjectMapper mapper = new ObjectMapper();
        _count++;
        MessageSocketDTO messageSocketDto = new MessageSocketDTO();
        messageSocketDto.setReceiver(this.receiver);
        messageSocketDto.setType(MessageSocketDTO.MessageType.TIME_COUNT);
        TimeCount timeCount = new TimeCount();
        timeCount.setTime(_count);
        try {
            messageSocketDto.setContent(mapper.writeValueAsString(timeCount));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        messageTmp.convertAndSend(this.channel, messageSocketDto);

        if (_count.equals(this.time)) {
            Message message = new Message();
            message.setMessage("Done");
            messageSocketDto.setType(MessageSocketDTO.MessageType.NOTIFICATION);
            try {
                messageSocketDto.setContent(mapper.writeValueAsString(message));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            messageSocketDto.setReceiver(this.receiver);
            messageTmp.convertAndSend(this.channel, messageSocketDto);
            Global.userAttributes.put(receiver,
                    userAttr.replace("view-ads:counting;", "view-ads:stopped;"));
            cancel();
        }
    }

    private class TimeCount {
        private Integer time;

        public Integer getTime() {
            return time;
        }

        public void setTime(Integer time) {
            this.time = time;
        }
    }

    private class Message {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
