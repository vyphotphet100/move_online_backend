package com.move_up.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.move_up.dto.MessageDTO;
import com.move_up.service.impl.MessageService;

@CrossOrigin
@RestController
public class MessageAPI {

	@Autowired
	private MessageService messageService;
	
	@GetMapping("/api/message")
	public ResponseEntity<MessageDTO> getMesage() {
		MessageDTO messageDto = messageService.findAll();
		return new ResponseEntity<MessageDTO>(messageDto, messageDto.getHttpStatus());
	}
	
	@GetMapping("/api/message/{id}")
	public ResponseEntity<MessageDTO> getOneMesage(@PathVariable("id") Long id) {
		MessageDTO messageDto = messageService.findOne(id);
		return new ResponseEntity<MessageDTO>(messageDto, messageDto.getHttpStatus());
	}
	
	@PostMapping("/api/message")
	public ResponseEntity<MessageDTO> postMesage(@RequestBody MessageDTO messageDto) {
		messageDto = messageService.save(messageDto);
		return new ResponseEntity<MessageDTO>(messageDto, messageDto.getHttpStatus());
	}
	
	@DeleteMapping("/api/message/{id}")
	public ResponseEntity<MessageDTO> deleteMesage(@PathVariable("id") Long id) {
		MessageDTO messageDto = messageService.delete(id);
		return new ResponseEntity<MessageDTO>(messageDto, messageDto.getHttpStatus());
	}
}
