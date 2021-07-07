package com.move_up.service.management.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import com.move_up.dto.UserDTO;
import com.move_up.entity.MissionEntity;
import com.move_up.entity.UserEntity;
import com.move_up.repository.MissionRepository;
import com.move_up.repository.UserRepository;
import com.move_up.service.management.IUserService;
import com.move_up.utils.JwtUtil;

@Service
public class UserService extends BaseService implements IUserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired 
	private MissionRepository missionRepo;

	@Override
	public UserDTO findAll() {
		UserDTO userDto = new UserDTO();
		List<UserEntity> userEntities = userRepo.findAll();

		if (!userEntities.isEmpty()) {
			for (UserEntity userEntity : userEntities)
				userDto.getListResult().add(this.converter.toDTO(userEntity, UserDTO.class));
			userDto.setMessage("Load user list successfully.");
			return userDto;
		}

		return (UserDTO) this.ExceptionObject(userDto, "There is no user.");
	}

	@Override
	public UserDTO findOne(String username, HttpServletRequest request, String userStr) {
		UserEntity requestedUserEntity = this.getRequestedUser(request);
		if (requestedUserEntity == null) 
			return (UserDTO)this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");
		
		UserDTO userDto = new UserDTO();
		UserEntity userEntity = userRepo.findOne(username);

		if (userEntity != null) {
			if (userStr.contains("requestedRole=user")) {
				if (requestedUserEntity == null || 
					!userEntity.getUsername().equals(requestedUserEntity.getUsername()))
					return (UserDTO) this.ExceptionObject(userDto, "Access Denied");
			}
			
			userDto = this.converter.toDTO(userEntity, UserDTO.class);
			userDto.setMessage("Get user having username = " + username + " successfully.");
			return userDto;
		}

		return (UserDTO) this.ExceptionObject(userDto, "User does not exist.");
	}

	@Override
	public UserDTO save(UserDTO userDto) {
		UserEntity userEntity = userRepo.findOne(userDto.getUsername());
		if (userEntity == null) {
			userEntity = this.converter.toEntity(userDto, UserEntity.class);
			userEntity.setTokenCode(JwtUtil.generateToken(userEntity));
			userEntity.setAccountBalance(0);
			userEntity.setCommission(0);
			userEntity.setNumOfCoinGiftBox(0);
			userEntity.setNumOfDefaultTime(0);
			userEntity.setNumOfStar(0);
			userEntity.setNumOfTimeGiftBox(0);
			userEntity.setNumOfTravelledTime(0);
			userEntity = userRepo.save(userEntity);
			
			userDto = this.converter.toDTO(userEntity, UserDTO.class);
			userDto.setMessage("Đăng ký thành công.");
			return userDto;
		}

		return (UserDTO) this.ExceptionObject(userDto, "Tên đăng nhập này đã tồn tại.");
	}

	@Override
	public UserDTO update(UserDTO userDto) {
		if (userRepo.findOne(userDto.getUsername()) != null) {
			UserEntity userEntity = userRepo.save(this.converter.toEntity(userDto, UserEntity.class));
			userDto = this.converter.toDTO(userEntity, UserDTO.class);
			userDto.setMessage("Cập nhật thông tin người dùng thành công.");
			return userDto;
		}

		return (UserDTO) this.ExceptionObject(userDto, "Tên đăng nhập không tồn tại.");
	}

	@Override
	public UserDTO delete(String username) {
		UserDTO userDto = new UserDTO();
		if (userRepo.findOne(username) != null) {
			userRepo.delete(username);
			userDto.setMessage("Delete user successfully.");
			return userDto;
		}

		return (UserDTO) this.ExceptionObject(userDto, "This username does not exist.");
	}

	@Override
	public UserDTO logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDTO userDto = new UserDTO();
		try {
			if (auth != null) {
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
			userDto.setMessage("Logout successfully.");
			return userDto;
		} catch (Exception ex) {
			return (UserDTO)this.ExceptionObject(userDto, "Something went wrong.");
		}
	}

	@Override
	public UserDTO getCurrentUser(HttpServletRequest request) {
		UserEntity requestedUserEntity = this.getRequestedUser(request);
		if (requestedUserEntity == null) 
			return (UserDTO)this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");
		
		UserDTO userDto = this.converter.toDTO(requestedUserEntity, UserDTO.class);
		userDto.setPassword(null);
		return userDto;
	}

	@Override
	public UserDTO exchangeTimeGiftBoxByStar(HttpServletRequest request) {
		UserEntity requestedUserEntity = this.getRequestedUser(request);
		if (requestedUserEntity == null) 
			return (UserDTO)this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");
		
		if (requestedUserEntity.getNumOfStar() < 10)
			return (UserDTO)this.ExceptionObject(new UserDTO(), "Không đủ số sao để đổi Hộp quà thời gian.");
		
		increaseTimeGiftBox(1, requestedUserEntity);
		requestedUserEntity.setNumOfStar(requestedUserEntity.getNumOfStar() - 10);
		requestedUserEntity = userRepo.save(requestedUserEntity);
		UserDTO userDto = this.converter.toDTO(requestedUserEntity, UserDTO.class);
		userDto.setMessage("Đổi thành công 1 hộp quà thời gian.");
		return userDto;
	}

	@Override
	public UserDTO exchangeCoinGiftBoxByStar(HttpServletRequest request) {
		UserEntity requestedUserEntity = this.getRequestedUser(request);
		if (requestedUserEntity == null) 
			return (UserDTO)this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");
		
		if (requestedUserEntity.getNumOfStar() < 20)
			return (UserDTO)this.ExceptionObject(new UserDTO(), "Không đủ số sao để đổi Hộp quà xu.");
		
		increaseCoinGiftBox(1, requestedUserEntity);
		requestedUserEntity.setNumOfStar(requestedUserEntity.getNumOfStar() - 20);
		requestedUserEntity = userRepo.save(requestedUserEntity);
		UserDTO userDto = this.converter.toDTO(requestedUserEntity, UserDTO.class);
		userDto.setMessage("Đổi thành công 1 hộp quà xu.");
		return userDto;
	}
	
	private void increaseTimeGiftBox(int numOfTimeGiftBox, UserEntity userEntity) {
		userEntity.setNumOfTimeGiftBox(userEntity.getNumOfTimeGiftBox() + numOfTimeGiftBox);
		userRepo.save(userEntity);
	}
	
	private void increaseCoinGiftBox(int numOfCoinGiftBox, UserEntity userEntity) {
		userEntity.setNumOfCoinGiftBox(userEntity.getNumOfCoinGiftBox() + numOfCoinGiftBox);
		userRepo.save(userEntity);
	}

	@Override
	public UserDTO exchangeShirtByStar(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO getReferredUser(HttpServletRequest request) {
		UserEntity requestedUserEntity = this.getRequestedUser(request);
		if (requestedUserEntity == null) 
			return (UserDTO)this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");
		
		UserDTO resDto = new UserDTO();
		List<UserEntity> referredUsers = userRepo.findAllByReferrerUserUsername(requestedUserEntity.getUsername());
		for (UserEntity userEntity : referredUsers) {
			UserDTO userDto = new UserDTO();
			userDto.setUsername(userEntity.getUsername());
			userDto.setCommission(userEntity.getCommission());
			resDto.getListResult().add(userDto);
		}
		
		resDto.setMessage("Load referred-user successfully.");
		return resDto;
	}

}
