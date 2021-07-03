package com.move_up.api.management;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.move_up.dto.MomoDTO;
import com.move_up.service.management.IMomoService;

@RestController
public class MomoAPI {

	@Autowired 
	private IMomoService momoService;
	
	@GetMapping("/api/momo")
	public ResponseEntity<MomoDTO> getMomo() {
		MomoDTO momoDto = momoService.findAll();
		return new ResponseEntity<MomoDTO>(momoDto, momoDto.getHttpStatus());
	}
	
	@GetMapping("/api/momo/{phoneNumber}")
	public ResponseEntity<MomoDTO> getOneMomo(@PathVariable("phoneNumber") String phoneNumber, HttpServletRequest request) {
		MomoDTO momoDto = momoService.findOne(phoneNumber, request);
		return new ResponseEntity<MomoDTO>(momoDto, momoDto.getHttpStatus());
	}
	
	@PostMapping("/api/momo")
	public ResponseEntity<MomoDTO> postMomo(@RequestBody MomoDTO momoDto, HttpServletRequest request) {
		momoDto = momoService.save(momoDto, request);
		return new ResponseEntity<MomoDTO>(momoDto, momoDto.getHttpStatus());
	}
	
	@PutMapping("/api/momo")
	public ResponseEntity<MomoDTO> putMomo(@RequestBody MomoDTO momoDto) {
		momoDto = momoService.update(momoDto);
		return new ResponseEntity<MomoDTO>(momoDto, momoDto.getHttpStatus());
	}
	
	@DeleteMapping("/api/momo/{phoneNumber}")
	public ResponseEntity<MomoDTO> deleteMomo(@PathVariable("phoneNumber") String phoneNumber) {
		MomoDTO momoDto = momoService.delete(phoneNumber);
		return new ResponseEntity<MomoDTO>(momoDto, momoDto.getHttpStatus());
	}
}
