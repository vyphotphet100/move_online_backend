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

import com.move_up.dto.MissionDTO;
import com.move_up.service.IMissionService;

@RestController
public class MissionAPI {

	@Autowired
	private IMissionService missionService;
	
	@GetMapping("/api/mission")
	public ResponseEntity<MissionDTO> getMisson() {
		MissionDTO missionDto = missionService.findAll();
		return new ResponseEntity<MissionDTO>(missionDto, missionDto.getHttpStatus());
	}
	
	@GetMapping("/api/mission/{id}")
	public ResponseEntity<MissionDTO> getOneMisson(@PathVariable("id") Long id) {
		MissionDTO missionDto = missionService.findOne(id);
		return new ResponseEntity<MissionDTO>(missionDto, missionDto.getHttpStatus());
	}
	
	@PostMapping("/api/mission")
	public ResponseEntity<MissionDTO> postMisson(@RequestBody MissionDTO missionDto) {
		missionDto = missionService.save(missionDto);
		return new ResponseEntity<MissionDTO>(missionDto, missionDto.getHttpStatus());
	}
	
	@PutMapping("/api/mission")
	public ResponseEntity<MissionDTO> putMisson(@RequestBody MissionDTO missionDto) {
		missionDto = missionService.update(missionDto);
		return new ResponseEntity<MissionDTO>(missionDto, missionDto.getHttpStatus());
	}
	
	@DeleteMapping("/api/mission/{id}")
	public ResponseEntity<MissionDTO> deleteMisson(@PathVariable("id") Long id) {
		MissionDTO missionDto = missionService.delete(id);
		return new ResponseEntity<MissionDTO>(missionDto, missionDto.getHttpStatus());
	}
	
}
