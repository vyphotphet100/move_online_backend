package com.move_up.service.socket;

import com.move_up.dto.MessageSocketDTO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface IMissionSocketService extends IBaseSocketService{
    void viewAds(MessageSocketDTO messageSocketDto) throws IOException;
    void viewAdsDone(MessageSocketDTO messageSocketDto);
}
