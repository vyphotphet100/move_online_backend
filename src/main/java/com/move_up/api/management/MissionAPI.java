package com.move_up.api.management;

import com.move_up.dto.MessageSocketDTO;
import com.move_up.timer_task.ViewAdsTimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.move_up.dto.MissionDTO;
import com.move_up.service.management.IMissionService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MissionAPI {

	@Autowired
	private IMissionService missionService;
	
	@GetMapping("/api/mission")
	public ResponseEntity<MissionDTO> getMission() {
		MissionDTO missionDto = missionService.findAll();
		return new ResponseEntity<MissionDTO>(missionDto, missionDto.getHttpStatus());
	}
	
	@GetMapping("/api/mission/{id}")
	public ResponseEntity<MissionDTO> getOneMission(@PathVariable("id") Long id) {
		MissionDTO missionDto = missionService.findOne(id);
		return new ResponseEntity<MissionDTO>(missionDto, missionDto.getHttpStatus());
	}
	
	@PostMapping("/api/mission")
	public ResponseEntity<MissionDTO> postMission(@RequestBody MissionDTO missionDto) {
		missionDto = missionService.save(missionDto);
		return new ResponseEntity<MissionDTO>(missionDto, missionDto.getHttpStatus());
	}
	
	@PutMapping("/api/mission")
	public ResponseEntity<MissionDTO> putMission(@RequestBody MissionDTO missionDto) {
		missionDto = missionService.update(missionDto);
		return new ResponseEntity<MissionDTO>(missionDto, missionDto.getHttpStatus());
	}
	
	@DeleteMapping("/api/mission/{id}")
	public ResponseEntity<MissionDTO> deleteMission(@PathVariable("id") Long id) {
		MissionDTO missionDto = missionService.delete(id);
		return new ResponseEntity<MissionDTO>(missionDto, missionDto.getHttpStatus());
	}

	@PostMapping("/api/mission/do_mission/{missionId}")
	public ResponseEntity<MissionDTO> doMission(HttpServletRequest request, @PathVariable Long missionId) {
		MissionDTO missionDto = missionService.doMission(request, missionId);
		return new ResponseEntity<MissionDTO>(missionDto, missionDto.getHttpStatus());
	}
	
}
