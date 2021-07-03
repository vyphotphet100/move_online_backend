package com.move_up.service.management.impl;

import javax.servlet.http.HttpServletRequest;

import com.move_up.service.management.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.move_up.dto.UserDTO;
import com.move_up.entity.UserEntity;
import com.move_up.repository.UserRepository;
import com.move_up.utils.JwtUtil;

@Service
public class LoginService extends BaseService implements ILoginService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDTO login(String username, String password) {
		// Check admin
		UserEntity userEntity = userRepo.findOneByUsernameAndPassword(username, password);
		if (userEntity != null) {
			UserDTO userDto = this.converter.toDTO(userEntity, UserDTO.class);
			userDto.setTokenCode(JwtUtil.getKeyTokenTail(userDto.getTokenCode()));
			String newToken = JwtUtil.generateToken(userDto);
			userDto.setTokenCode(newToken);
			userEntity = userRepo.save(this.converter.toEntity(userDto, UserEntity.class));
			userDto = this.converter.toDTO(userEntity, UserDTO.class);
			userDto.setMessage("Login successfully.");

			return userDto;
		}

		return (UserDTO) this.ExceptionObject(new UserDTO(), "Invalid username or password.");
	}

	@Override
	public UserDTO logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		UserDTO userDto = new UserDTO();
		userDto.setMessage("Logout successfully.");
		return userDto;
	}

	@Override
	public UserDTO authorization(HttpServletRequest request, String wholeRequestURL) {
		UserDTO userDto = this.converter.toDTO(this.getRequestedUser(request), UserDTO.class);
		
		if (userDto == null)
			return (UserDTO) this.ExceptionObject(new UserDTO(), "Bạn không có quyền.");

		if (wholeRequestURL.contains("/admin") && !userDto.getRoleCodes().contains("ADMIN"))
			return (UserDTO) this.ExceptionObject(new UserDTO(), "Bạn không có quyền.");
		
		
		userDto.setMessage("Cho phép truy cập.");
		return userDto;
	}

}
