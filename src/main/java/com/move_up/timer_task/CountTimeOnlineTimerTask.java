package com.move_up.timer_task;

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
    public void run() {
        if (Global.userAttributes.get(username).contains("fbStatus:online;")) {
            _count++;
        } else if (Global.userAttributes.get(username).contains("fbStatus:checking;")) {
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
            messageTmp.convertAndSend(this.channel, "Increase 1 minute.");
        }
    }
}
