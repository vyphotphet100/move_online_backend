package com.move_up.service.socket;

import com.move_up.dto.MessageSocketDTO;

public interface ICheckFbStatus extends IBaseSocketService{
    void checkFbStatus(MessageSocketDTO messageSocketDTO);
}
