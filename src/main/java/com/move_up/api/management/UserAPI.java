package com.move_up.api.management;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import com.move_up.dto.UserDTO;
import com.move_up.service.management.IUserService;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class UserAPI {

	@Autowired
	private IUserService userService;

	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping("/api/user")
	public ResponseEntity<UserDTO> getUser(HttpServletRequest request) {
		UserDTO userDto = userService.findAll();
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	@GetMapping("/api/user/{username}")
	public ResponseEntity<UserDTO> getOneUser(@PathVariable("username") String username, HttpServletRequest request) {
		UserDTO userDto = userService.findOne(username, request, "requestedRole=user");
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}

	@PostMapping("/api/user")
	public ResponseEntity<UserDTO> postUser(@RequestBody UserDTO userDto) {
		userDto = userService.save(userDto);
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}

	@PreAuthorize("hasAnyAuthority('USER')")
	@PutMapping("/api/user")
	public ResponseEntity<UserDTO> putUser(@RequestBody UserDTO userDto, HttpServletRequest request) {
		userDto = userService.update(userDto, request);
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}

	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@DeleteMapping("/api/user/{username}")
	public ResponseEntity<UserDTO> deleteUser(@PathVariable("username") String username, HttpServletResponse response) {
		UserDTO userDto = userService.delete(username);
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}

	@PreAuthorize("hasAnyAuthority('USER')")
	@GetMapping("/api/user/option/current_user")
	public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
		UserDTO userDto = userService.getCurrentUser(request);
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}

	@PreAuthorize("hasAnyAuthority('USER')")
	@GetMapping("/api/user/exchange_time_gift_box") 
	public ResponseEntity<UserDTO> exchangeTimeGiftBox(HttpServletRequest request) {
		UserDTO userDto = userService.exchangeTimeGiftBoxByStar(request);
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}

	@PreAuthorize("hasAnyAuthority('USER')")
	@GetMapping("/api/user/exchange_coin_gift_box") 
	public ResponseEntity<UserDTO> exchangeCoinGiftBox(HttpServletRequest request) {
		UserDTO userDto = userService.exchangeCoinGiftBoxByStar(request);
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}

	@PreAuthorize("hasAnyAuthority('USER')")
	@GetMapping("/api/user/referred_user") 
	public ResponseEntity<UserDTO> getReferredUser(HttpServletRequest request) {
		UserDTO userDto = userService.getReferredUser(request);
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}

	@PreAuthorize("hasAnyAuthority('USER')")
	@GetMapping("/api/user/logout")
	public ResponseEntity<UserDTO> logout(HttpServletRequest request, HttpServletResponse response) {
		UserDTO userDto = userService.logout(request, response);
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}

	@PreAuthorize("hasAnyAuthority('USER')")
	@PostMapping("/api/user/change_password")
	public ResponseEntity<UserDTO> changePassword(@RequestBody UserDTO userDto, HttpServletRequest request) {
		userDto = userService.changePassword(userDto, request);
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}

	@PreAuthorize("hasAnyAuthority('USER')")
	@PostMapping("/api/user/check_facebook")
	public ResponseEntity<UserDTO> checkFacebookAccByPostLink(@RequestBody UserDTO userDto, HttpServletRequest request) {
		userDto = userService.checkFacebookAccByPostLink(userDto, request);
		return new ResponseEntity(userDto, userDto.getHttpStatus());
	}

	@PreAuthorize("hasAnyAuthority('USER')")
	@PostMapping("/api/user/save_facebook")
	public ResponseEntity<UserDTO> saveFacebookAccByPostLink(@RequestBody UserDTO userDto, HttpServletRequest request) {
		userDto = userService.saveFacebookAccByPostLink(userDto, request);
		return new ResponseEntity(userDto, userDto.getHttpStatus());
	}

	@PreAuthorize("hasAnyAuthority('USER')")
	@PostMapping("/api/user/save_referrer_user")
	public ResponseEntity<UserDTO> saveReferrerUser(@RequestBody UserDTO userDto, HttpServletRequest request) {
		userDto = userService.saveReferrerUser(request, userDto);
		return new ResponseEntity(userDto, userDto.getHttpStatus());
	}

	@RequestMapping(value = "/api/user/check_referrer_exist", method = RequestMethod.OPTIONS)
	public ResponseEntity<UserDTO> checkReferrerExist(@RequestBody UserDTO userDto) {
		userDto = userService.checkReferrerExist(userDto);
		return new ResponseEntity<UserDTO>(userDto, userDto.getHttpStatus());
	}

	@GetMapping(value = "/api/user/picture/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getImage(@PathVariable String username) throws IOException {
		File file = new File("src/main/resources/static/picture/" + username + ".jpg");
		return FileUtils.readFileToByteArray(file);
	}
}
