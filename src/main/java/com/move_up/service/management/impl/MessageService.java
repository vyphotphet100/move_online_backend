package com.move_up.service.management.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.move_up.dto.MessageDTO;
import com.move_up.entity.MessageEntity;
import com.move_up.repository.MessageRepository;
import com.move_up.service.management.IMessageService;

@Service
public class MessageService extends BaseService implements IMessageService {

	@Autowired
	private MessageRepository messageRepo;

	@Override
	public MessageDTO findAll() {
		MessageDTO messageDto = new MessageDTO();
		List<MessageEntity> messageEntities = messageRepo.findAll();

		if (!messageEntities.isEmpty()) {
			for (MessageEntity messageEntity : messageEntities)
				messageDto.getListResult().add(this.converter.toDTO(messageEntity, MessageDTO.class));
			messageDto.setMessage("Load message list successfully.");
			return messageDto;
		}

		return (MessageDTO) this.ExceptionObject(messageDto, "There is no message.");
	}

	@Override
	public MessageDTO findOne(Long id) {
		MessageDTO messageDto = new MessageDTO();
		MessageEntity messageEntity = messageRepo.findById(id).get();

		if (messageEntity != null) {
			messageDto = this.converter.toDTO(messageEntity, MessageDTO.class);
			messageDto.setMessage("Get message having id = " + id + " successfully.");
			return messageDto;
		}

		return (MessageDTO) this.ExceptionObject(messageDto, "Message does not exist.");
	}

	@Override
	public MessageDTO save(MessageDTO messageDto) {
		MessageEntity announcementEntity = messageRepo.save(this.converter.toEntity(messageDto, MessageEntity.class));
		messageDto = this.converter.toDTO(announcementEntity, MessageDTO.class);
		messageDto.setMessage("Send message successfully.");
		return messageDto;

	}

	@Override
	public MessageDTO delete(Long id) {
		MessageDTO messageDto = new MessageDTO();
		if (messageRepo.findById(id) != null) {
			messageRepo.deleteById(id);
			messageDto.setMessage("Delete message successfully.");
			return messageDto;
		}

		return (MessageDTO) this.ExceptionObject(messageDto, "This message id does not exist.");
	}

}
