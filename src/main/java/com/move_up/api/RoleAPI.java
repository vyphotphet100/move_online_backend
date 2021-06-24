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

import com.move_up.dto.RoleDTO;
import com.move_up.service.IRoleService;

@RestController
public class RoleAPI {

	@Autowired
	private IRoleService roleService;
	
	@GetMapping("/api/role")
	public ResponseEntity<RoleDTO> getRole() {
		RoleDTO roleDto = roleService.findAll();
		return new ResponseEntity<RoleDTO>(roleDto, roleDto.getHttpStatus());
	}
	
	@GetMapping("/api/role/{code}")
	public ResponseEntity<RoleDTO> getOneRole(@PathVariable("code") String code) {
		RoleDTO roleDto = roleService.findOne(code);
		return new ResponseEntity<RoleDTO>(roleDto, roleDto.getHttpStatus());
	}
	
	@PostMapping("/api/role")
	public ResponseEntity<RoleDTO> postRole(@RequestBody RoleDTO roleDto) {
		roleDto = roleService.save(roleDto);
		return new ResponseEntity<RoleDTO>(roleDto, roleDto.getHttpStatus());
	}
	
	@PutMapping("/api/role")
	public ResponseEntity<RoleDTO> putRole(@RequestBody RoleDTO roleDto) {
		roleDto = roleService.update(roleDto);
		return new ResponseEntity<RoleDTO>(roleDto, roleDto.getHttpStatus());
	}
	
	@DeleteMapping("/api/role/{code}")
	public ResponseEntity<RoleDTO> deleteRole(@PathVariable("code") String code) {
		RoleDTO roleDto = roleService.delete(code);
		return new ResponseEntity<RoleDTO>(roleDto, roleDto.getHttpStatus());
	}

}
