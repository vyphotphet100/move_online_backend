package com.move_up.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.move_up.dto.AnnouncementDTO;
import com.move_up.service.IAnnouncementService;

@RestController
public class AnnouncementAPI {
	
	@Autowired
	private IAnnouncementService announcementService; 

	@GetMapping("/api/announcement")
	public ResponseEntity<AnnouncementDTO> getAnnouncement() {
		AnnouncementDTO announcementDto = announcementService.findAll();
		return new ResponseEntity<AnnouncementDTO>(announcementDto, announcementDto.getHttpStatus());
	}
	
	@GetMapping("/api/announcement/{id}")
	public ResponseEntity<AnnouncementDTO> getOneAnnouncement(@PathVariable("id") Long id) {
		AnnouncementDTO announcementDto = announcementService.findOne(id);
		return new ResponseEntity<AnnouncementDTO>(announcementDto, announcementDto.getHttpStatus());
	}
	
	@PostMapping("/api/announcement")
	public ResponseEntity<AnnouncementDTO> postAnnouncement(@RequestBody AnnouncementDTO announcementDto) {
		announcementDto = announcementService.save(announcementDto);
		return new ResponseEntity<AnnouncementDTO>(announcementDto, announcementDto.getHttpStatus());
	}
	
	@PutMapping("/api/announcement")
	public ResponseEntity<AnnouncementDTO> putAnnouncement(@RequestBody AnnouncementDTO announcementDto) {
		announcementDto = announcementService.update(announcementDto);
		return new ResponseEntity<AnnouncementDTO>(announcementDto, announcementDto.getHttpStatus());
	}
	
	@DeleteMapping("/api/announcement/{id}")
	public ResponseEntity<AnnouncementDTO> deleteAnnouncement(@PathVariable("id") Long id) {
		AnnouncementDTO announcementDto = announcementService.delete(id);
		return new ResponseEntity<AnnouncementDTO>(announcementDto, announcementDto.getHttpStatus());
	}
}
