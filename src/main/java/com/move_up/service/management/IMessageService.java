package com.move_up.service.management;

import com.move_up.dto.MessageDTO;

public interface IMessageService extends IBaseService{

	MessageDTO findAll();
	MessageDTO findOne(Long id);
	MessageDTO save(MessageDTO messageDto);
	MessageDTO delete(Long id);
}
