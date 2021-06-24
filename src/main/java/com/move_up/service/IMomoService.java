package com.move_up.service;

import javax.servlet.http.HttpServletRequest;

import com.move_up.dto.MomoDTO;

public interface IMomoService extends IBaseService{

	MomoDTO findAll();
	MomoDTO findOne(String phoneNumber, HttpServletRequest request);
	MomoDTO save(MomoDTO momoDto, HttpServletRequest request);
	MomoDTO update(MomoDTO momoDto);
	MomoDTO delete(String phoneNumber);
}
