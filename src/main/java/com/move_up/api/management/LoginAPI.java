package com.move_up.api.management;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.move_up.dto.UserDTO;
import com.move_up.service.management.ILoginService;

@Controller
public class LoginAPI {

	@Autowired
	private ILoginService loginService;

	@PostMapping("/login")
	public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDto) {
		UserDTO resDto = loginService.login(userDto.getUsername(), userDto.getPassword());
		return new ResponseEntity<UserDTO>(resDto, resDto.getHttpStatus());
	}
	
	@GetMapping("/log_out")
	public ResponseEntity<UserDTO> logout() {
		UserDTO userDto = loginService.logout();
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}
	
	@PostMapping("/authorization")
	public ResponseEntity<UserDTO> authorization(HttpServletRequest request, @RequestBody UserDTO userDto) {
		userDto = loginService.authorization(request, userDto.getListRequest().get(0).toString());
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}
}
