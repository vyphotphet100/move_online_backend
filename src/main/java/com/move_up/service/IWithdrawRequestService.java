package com.move_up.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Sort;

import com.move_up.dto.WithdrawRequestDTO;

public interface IWithdrawRequestService extends IBaseService{

	WithdrawRequestDTO findAll();
	WithdrawRequestDTO findAllWithPagingAndSort(int limit, int offset, Sort sort);
	WithdrawRequestDTO findAllWithPaging(int limit, int offset);
	WithdrawRequestDTO findOne(Long id, HttpServletRequest request, String withdrawRequestString);
	WithdrawRequestDTO save(WithdrawRequestDTO withdrawRequestDto, HttpServletRequest request, String withdrawRequestString);
	WithdrawRequestDTO update(WithdrawRequestDTO withdrawRequestDto, HttpServletRequest request, String withdrawRequestString);
	WithdrawRequestDTO delete(Long id, HttpServletRequest request, String withdrawRequestString);
}
