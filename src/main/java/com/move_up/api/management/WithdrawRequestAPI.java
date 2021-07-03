package com.move_up.api.management;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.move_up.dto.WithdrawRequestDTO;
import com.move_up.service.management.IWithdrawRequestService;

@RestController
public class WithdrawRequestAPI {

	@Autowired
	private IWithdrawRequestService withdrawRequestService;

	@GetMapping("/api/withdraw_request")
	public ResponseEntity<WithdrawRequestDTO> getWithdrawRequest(
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "sort_by", required = false) String sortBy) {
		WithdrawRequestDTO withdrawRequestDto = new WithdrawRequestDTO();
		
		Sort sort = null;
		if (sortBy != null) {
			if (sortBy.charAt(0) == '+') 
				sort = new Sort(Sort.Direction.ASC, sortBy.substring(1));
			else if (sortBy.charAt(0) == '-') 
				sort = new Sort(Sort.Direction.DESC, sortBy.substring(1));
		}
		
		if (limit != null && offset != null) {
			if (sort == null)
				withdrawRequestDto = withdrawRequestService.findAllWithPaging(limit, offset);
			else 
				withdrawRequestDto = withdrawRequestService.findAllWithPagingAndSort(limit, offset, sort);
			return new ResponseEntity<WithdrawRequestDTO>(withdrawRequestDto, withdrawRequestDto.getHttpStatus());
			
		}
		withdrawRequestDto = withdrawRequestService.findAll();
		return new ResponseEntity<WithdrawRequestDTO>(withdrawRequestDto, withdrawRequestDto.getHttpStatus());
	}

	@GetMapping("/api/withdraw_request/{id}")
	public ResponseEntity<WithdrawRequestDTO> getOneWithdrawRequest(@PathVariable("id") Long id,
			HttpServletRequest request) {
		WithdrawRequestDTO withdrawRequestDto = withdrawRequestService.findOne(id, request, "requestedRole=user");
		return new ResponseEntity<WithdrawRequestDTO>(withdrawRequestDto, withdrawRequestDto.getHttpStatus());
	}

	@PostMapping("/api/withdraw_request")
	public ResponseEntity<WithdrawRequestDTO> postWithdrawRequest(@RequestBody WithdrawRequestDTO withdrawRequestDto,
			HttpServletRequest request) {
		withdrawRequestDto = withdrawRequestService.save(withdrawRequestDto, request, "requestedRole=user");
		return new ResponseEntity<WithdrawRequestDTO>(withdrawRequestDto, withdrawRequestDto.getHttpStatus());
	}

	@PutMapping("/api/withdraw_request")
	public ResponseEntity<WithdrawRequestDTO> putWithdrawRequest(@RequestBody WithdrawRequestDTO withdrawRequestDto,
			HttpServletRequest request) {
		withdrawRequestDto = withdrawRequestService.update(withdrawRequestDto, request, "requestedRole=user");
		return new ResponseEntity<WithdrawRequestDTO>(withdrawRequestDto, withdrawRequestDto.getHttpStatus());
	}

	@DeleteMapping("/api/withdraw_request/{id}")
	public ResponseEntity<WithdrawRequestDTO> deleteWithdrawRequest(@PathVariable("id") Long id,
			HttpServletRequest request) {
		WithdrawRequestDTO withdrawRequestDto = withdrawRequestService.delete(id, request, "requestedRole=user");
		return new ResponseEntity<WithdrawRequestDTO>(withdrawRequestDto, withdrawRequestDto.getHttpStatus());
	}
}
