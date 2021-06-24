package com.move_up.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.move_up.dto.CoinGiftBoxDTO;
import com.move_up.dto.TimeGiftBoxDTO;
import com.move_up.entity.UserEntity;
import com.move_up.repository.UserRepository;
import com.move_up.service.IGiftBoxService;

@Service
public class GiftBoxService extends BaseService implements IGiftBoxService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public CoinGiftBoxDTO openCoinGiftBox(HttpServletRequest request) {
		// get requested user
		UserEntity userEntity = null;
		if (request.getHeader("Authorization") != null) {
			String authorizationHeader = request.getHeader("Authorization");
			if (!authorizationHeader.equals("") && authorizationHeader.startsWith("Token ")) {
				String token = authorizationHeader.substring(6);
				userEntity = userRepo.findOneByTokenCode(token);
			}
		}

		// Check number of coin gift box
		if (userEntity != null) {
			if (userEntity.getNumOfCoinGiftBox() > 0) {
				CoinGiftBoxDTO coinGiftBoxDto = new CoinGiftBoxDTO();
				// decrease num of coin gift box
				userEntity.setNumOfCoinGiftBox(userEntity.getNumOfCoinGiftBox() - 1);

				// Get random number between 5-20
				int max = 20;
				int min = 5;
				int value = (int) Math.floor(Math.random() * (max - min + 1) + min);

				// increase balance and set value to coinGiftBoxDto
				userEntity.setAccountBalance(userEntity.getAccountBalance() + value);
				int commission = (value*20)/100;
				userEntity.setCommission(userEntity.getCommission() + commission);
				coinGiftBoxDto.setValue(value);

				// save userEntity
				userEntity = userRepo.save(userEntity);

				coinGiftBoxDto.setMessage("Bạn đã nhận được " + value + " xu từ Hộp quà xu.");
				return coinGiftBoxDto;
			}
			return (CoinGiftBoxDTO) this.ExceptionObject(new CoinGiftBoxDTO(), "Bạn không còn Hộp quà xu nào nữa.");
		}

		return (CoinGiftBoxDTO) this.ExceptionObject(new CoinGiftBoxDTO(), "Service does not know user.");
	}

	@Override
	public TimeGiftBoxDTO openTimeGiftBox(HttpServletRequest request) {
		// get requested user
		UserEntity userEntity = null;
		if (request.getHeader("Authorization") != null) {
			String authorizationHeader = request.getHeader("Authorization");
			if (!authorizationHeader.equals("") && authorizationHeader.startsWith("Token ")) {
				String token = authorizationHeader.substring(6);
				userEntity = userRepo.findOneByTokenCode(token);
			}
		}

		// Check number of coin gift box
		if (userEntity != null) {
			if (userEntity.getNumOfTimeGiftBox() > 0) {
				TimeGiftBoxDTO timeGiftBoxDto = new TimeGiftBoxDTO();
				// decrease num of time gift box
				userEntity.setNumOfTimeGiftBox(userEntity.getNumOfTimeGiftBox() - 1);

				// Get random number between 5-20
				int max = 20;
				int min = 5;
				int value = (int) Math.floor(Math.random() * (max - min + 1) + min);

				// increase balance and set value to coinGiftBoxDto
				userEntity.setNumOfDefaultTime(userEntity.getNumOfDefaultTime() + value);
				timeGiftBoxDto.setValue(value);

				// save userEntity
				userEntity = userRepo.save(userEntity);

				timeGiftBoxDto.setMessage("Bạn đã nhận được " + value + " phút từ Hộp quà thời gian.");
				return timeGiftBoxDto;
			}
			return (TimeGiftBoxDTO) this.ExceptionObject(new TimeGiftBoxDTO(), "Bạn không còn Hộp quà thời gian nào nữa.");
		}

		return (TimeGiftBoxDTO) this.ExceptionObject(new TimeGiftBoxDTO(), "Service does not know user.");
	}

}
