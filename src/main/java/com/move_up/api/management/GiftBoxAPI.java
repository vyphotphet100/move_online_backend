package com.move_up.api.management;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.move_up.dto.BaseDTO;
import com.move_up.dto.CoinGiftBoxDTO;
import com.move_up.dto.TimeGiftBoxDTO;
import com.move_up.service.management.IGiftBoxService;

@RestController
public class GiftBoxAPI {

	@Autowired 
	private IGiftBoxService giftBoxService;
	
	@GetMapping("/api/gift_box/coin_gift_box")
	public ResponseEntity<BaseDTO> getCoinGiftBox(@RequestParam("option") String option, HttpServletRequest request) {
		if (option.equals("open")) {
			CoinGiftBoxDTO coinGiftBoxDto = giftBoxService.openCoinGiftBox(request);
			return new ResponseEntity<BaseDTO>(coinGiftBoxDto, coinGiftBoxDto.getHttpStatus());
		}
		return null;
	}
	
	@GetMapping("/api/gift_box/time_gift_box")
	public ResponseEntity<BaseDTO> getTimeGiftBox(@RequestParam("option") String option, HttpServletRequest request) {
		if (option.equals("open")) {
			TimeGiftBoxDTO timeGiftBoxDto = giftBoxService.openTimeGiftBox(request);
			return new ResponseEntity<BaseDTO>(timeGiftBoxDto, timeGiftBoxDto.getHttpStatus());
		}
		return null;
	}
}
