package com.move_up.service;

import javax.servlet.http.HttpServletRequest;

import com.move_up.dto.CoinGiftBoxDTO;
import com.move_up.dto.TimeGiftBoxDTO;

public interface IGiftBoxService extends IBaseService{

	CoinGiftBoxDTO openCoinGiftBox(HttpServletRequest request);
	TimeGiftBoxDTO openTimeGiftBox(HttpServletRequest request);
}
