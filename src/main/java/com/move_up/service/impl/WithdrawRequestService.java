package com.move_up.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.move_up.dto.WithdrawRequestDTO;
import com.move_up.entity.UserEntity;
import com.move_up.entity.WithdrawRequestEntity;
import com.move_up.repository.WithdrawRequestRepository;
import com.move_up.service.IWithdrawRequestService;

@Service
public class WithdrawRequestService extends BaseService implements IWithdrawRequestService {

	@Autowired
	private WithdrawRequestRepository withdrawRequestRepo;

	
	@Override
	public WithdrawRequestDTO findAll() {
		WithdrawRequestDTO withdrawRequestDto = new WithdrawRequestDTO();
		List<WithdrawRequestEntity> withdrawRequestEntities = withdrawRequestRepo.findAll();
		if (!withdrawRequestEntities.isEmpty()) {
			for (WithdrawRequestEntity withdrawRequestEntity : withdrawRequestEntities)
				withdrawRequestDto.getListResult()
						.add(this.converter.toDTO(withdrawRequestEntity, WithdrawRequestDTO.class));
			withdrawRequestDto.setMessage("Load withdraw request list successfully.");
			return withdrawRequestDto;
		}

		return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto, "There is no withdraw request.");
	}
	
	@Override
	public WithdrawRequestDTO findAllWithPaging(int limit, int offset) {
		WithdrawRequestDTO withdrawRequestDto = new WithdrawRequestDTO();
		Pageable pageable = new PageRequest(offset, limit);
		List<WithdrawRequestEntity> withdrawRequestEntities = withdrawRequestRepo.findAll(pageable).getContent();
		if (!withdrawRequestEntities.isEmpty()) {
			for (WithdrawRequestEntity withdrawRequestEntity : withdrawRequestEntities)
				withdrawRequestDto.getListResult()
						.add(this.converter.toDTO(withdrawRequestEntity, WithdrawRequestDTO.class));
			withdrawRequestDto.setMessage("Load withdraw request list successfully.");
			return withdrawRequestDto;
		}

		return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto, "There is no withdraw request.");
	}
	
	@Override
	public WithdrawRequestDTO findAllWithPagingAndSort(int limit, int offset, Sort sort) {
		WithdrawRequestDTO withdrawRequestDto = new WithdrawRequestDTO();
		Pageable pageable = new PageRequest(offset, limit, sort);
		List<WithdrawRequestEntity> withdrawRequestEntities = withdrawRequestRepo.findAll(pageable).getContent();
		if (!withdrawRequestEntities.isEmpty()) {
			for (WithdrawRequestEntity withdrawRequestEntity : withdrawRequestEntities)
				withdrawRequestDto.getListResult()
						.add(this.converter.toDTO(withdrawRequestEntity, WithdrawRequestDTO.class));
			withdrawRequestDto.setMessage("Load withdraw request list successfully.");
			return withdrawRequestDto;
		}

		return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto, "There is no withdraw request.");
	}

	@Override
	public WithdrawRequestDTO findOne(Long id, HttpServletRequest request, String withdrawRequestString) {
		UserEntity userEntity = this.getRequestedUser(request);
		WithdrawRequestDTO withdrawRequestDto = null;
		WithdrawRequestEntity withdrawRequestEntity = withdrawRequestRepo.findOne(id);

		if (withdrawRequestString.contains("requestedRole=user")) {
			if (userEntity == null)
				return (WithdrawRequestDTO) this.ExceptionObject(new WithdrawRequestDTO(),
						"Service does not know user.");

			// check if withdraw request is not requested user's
			if (!withdrawRequestEntity.getUser().getUsername().equals(userEntity.getUsername()))
				return (WithdrawRequestDTO) this.ExceptionObject(new WithdrawRequestDTO(),
						"Bạn không có quyền xem lệnh đặt tiền này.");
		}

		if (withdrawRequestEntity == null)
			return (WithdrawRequestDTO) this.ExceptionObject(new WithdrawRequestDTO(), "Lệnh rút tiền không tồn tại.");

		withdrawRequestDto = this.converter.toDTO(withdrawRequestEntity, WithdrawRequestDTO.class);
		withdrawRequestDto.setMessage("Get withdraw request having id = " + id + " successfully.");
		return withdrawRequestDto;

	}

	@Override
	public WithdrawRequestDTO save(WithdrawRequestDTO withdrawRequestDto, HttpServletRequest request,
			String withdrawRequestString) {
		UserEntity userEntity = this.getRequestedUser(request);

		if (withdrawRequestString.contains("requestedRole=user")) {
			withdrawRequestDto.setStatus("UNPAID");
			withdrawRequestDto.setPaymentDate(new Date());
			// check user
			if (userEntity == null)
				return (WithdrawRequestDTO) this.ExceptionObject(new WithdrawRequestDTO(),
						"Service does not know user.");

			if (!userEntity.getUsername().equals(withdrawRequestDto.getUsername()))
				return (WithdrawRequestDTO) this.ExceptionObject(new WithdrawRequestDTO(),
						"Người thực hiện đặt lệnh rút tiền không khớp với người trong lệnh rút tiền, mời kiểm tra lại.");

			// check if the requested money is equal or greater than user's acccount balance
			if (userEntity.getAccountBalance() < withdrawRequestDto.getAmountOfMoney())
				return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto,
						"Bạn không có đủ số xu tương ứng với số tiền cần rút.");

			// if the requested money not equal or greater than 5000 or not divisible by
			// 5000
			if (withdrawRequestDto.getAmountOfMoney() < 5000)
				return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto,
						"Số xu cần rút phải lớn hơn hoặc bằng 5000 xu.");
			else if (withdrawRequestDto.getAmountOfMoney() % 5000 != 0)
				return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto,
						"Số xu cần rút phải là bội số của 5000.");
		}

		// check money if this request is ordered by admin
		if (withdrawRequestString.contains("requestedRole=admin")) {
			UserEntity userInWithdrawRequest = userRepo.findOne(withdrawRequestDto.getUsername());
			if (userInWithdrawRequest == null)
				return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto,
						"Người dùng không tồn tại trong hệ thống.");
			if (userInWithdrawRequest.getAccountBalance() < withdrawRequestDto.getAmountOfMoney())
				return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto,
						"Người dùng không có đủ số xu tương ứng với số tiền cần rút.");

			userEntity = userInWithdrawRequest;
		}

		// Subtract amount of money in userEntity
		userEntity.setAccountBalance(userEntity.getAccountBalance() - withdrawRequestDto.getAmountOfMoney());
		userRepo.save(userEntity);

		// save withdraw request
		WithdrawRequestEntity withdrawRequestEntity = withdrawRequestRepo
				.save(this.converter.toEntity(withdrawRequestDto, WithdrawRequestEntity.class));
		withdrawRequestDto = this.converter.toDTO(withdrawRequestEntity, WithdrawRequestDTO.class);
		withdrawRequestDto
				.setMessage("Đã đặt lệnh rút tiền thành công, hệ thống sẽ khớp lệnh cho bạn trong thời gian sớm nhất.");
		return withdrawRequestDto;

	}

	@Override
	public WithdrawRequestDTO update(WithdrawRequestDTO withdrawRequestDto, HttpServletRequest request,
			String withdrawRequestString) {

//		if (withdrawRequestRepo.findOne(withdrawRequestDto.getId()) != null) {
//			WithdrawRequestEntity withdrawRequestEntity = withdrawRequestRepo
//					.save(this.converter.toEntity(withdrawRequestDto, WithdrawRequestEntity.class));
//			withdrawRequestDto = this.converter.toDTO(withdrawRequestEntity, WithdrawRequestDTO.class);
//			withdrawRequestDto.setMessage("Update withdraw request successfully.");
//			return withdrawRequestDto;
//		}
//
//		return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto,
//				"This withdraw request id does not exist.");
		return new WithdrawRequestDTO();
	}

	@Override
	public WithdrawRequestDTO delete(Long id, HttpServletRequest request, String withdrawRequestString) {
//		UserEntity userEntity = this.getRequestedUser(request);
//		
//		// check user or admin
//		if (userEntity == null && withdrawRequestString.contains("requestedRole=user"))
//			return (WithdrawRequestDTO) this.ExceptionObject(new WithdrawRequestDTO(), "Service does not know user.");
//
//		WithdrawRequestDTO withdrawRequestDto = new WithdrawRequestDTO();
//		if (withdrawRequestRepo.findOne(id) != null) {
//			withdrawRequestRepo.delete(id);
//			withdrawRequestDto.setMessage("Xóa lệnh rút tiền thành công.");
//			return withdrawRequestDto;
//		}
//
//		return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto,
//				"Lệnh rút tiền này không tồn tại.");
		return new WithdrawRequestDTO();
	}
}
