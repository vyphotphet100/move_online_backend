package com.move_up.service.management.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.move_up.service.management.IWithdrawRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.move_up.dto.WithdrawRequestDTO;
import com.move_up.entity.UserEntity;
import com.move_up.entity.WithdrawRequestEntity;
import com.move_up.repository.WithdrawRequestRepository;

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
		WithdrawRequestEntity withdrawRequestEntity = withdrawRequestRepo.findById(id).get();

		if (withdrawRequestString.contains("requestedRole=user")) {
			if (userEntity == null)
				return (WithdrawRequestDTO) this.ExceptionObject(new WithdrawRequestDTO(),
						"Service does not know user.");

			// check if withdraw request is not requested user's
			if (!withdrawRequestEntity.getUser().getUsername().equals(userEntity.getUsername()))
				return (WithdrawRequestDTO) this.ExceptionObject(new WithdrawRequestDTO(),
						"B???n kh??ng c?? quy???n xem l???nh ?????t ti???n n??y.");
		}

		if (withdrawRequestEntity == null)
			return (WithdrawRequestDTO) this.ExceptionObject(new WithdrawRequestDTO(), "L???nh r??t ti???n kh??ng t???n t???i.");

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
						"Ng?????i th???c hi???n ?????t l???nh r??t ti???n kh??ng kh???p v???i ng?????i trong l???nh r??t ti???n, m???i ki???m tra l???i.");

			// check if the requested money is equal or greater than user's acccount balance
			if (userEntity.getAccountBalance() < withdrawRequestDto.getAmountOfMoney())
				return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto,
						"B???n kh??ng c?? ????? s??? xu t????ng ???ng v???i s??? ti???n c???n r??t.");

			// if the requested money not equal or greater than 5000 or not divisible by
			// 5000
			if (withdrawRequestDto.getAmountOfMoney() < 5000)
				return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto,
						"S??? xu c???n r??t ph???i l???n h??n ho???c b???ng 5000 xu.");
			else if (withdrawRequestDto.getAmountOfMoney() % 5000 != 0)
				return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto,
						"S??? xu c???n r??t ph???i l?? b???i s??? c???a 5000.");
		}

		// check money if this request is ordered by admin
		if (withdrawRequestString.contains("requestedRole=admin")) {
			UserEntity userInWithdrawRequest = userRepo.findById(withdrawRequestDto.getUsername()).get();
			if (userInWithdrawRequest == null)
				return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto,
						"Ng?????i d??ng kh??ng t???n t???i trong h??? th???ng.");
			if (userInWithdrawRequest.getAccountBalance() < withdrawRequestDto.getAmountOfMoney())
				return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto,
						"Ng?????i d??ng kh??ng c?? ????? s??? xu t????ng ???ng v???i s??? ti???n c???n r??t.");

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
				.setMessage("???? ?????t l???nh r??t ti???n th??nh c??ng, h??? th???ng s??? kh???p l???nh cho b???n trong th???i gian s???m nh???t.");
		return withdrawRequestDto;

	}

	@Override
	public WithdrawRequestDTO update(WithdrawRequestDTO withdrawRequestDto, HttpServletRequest request,
			String withdrawRequestString) {

//		if (withdrawRequestRepo.findById(withdrawRequestDto.getId()) != null) {
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
//		if (withdrawRequestRepo.findById(id) != null) {
//			withdrawRequestRepo.delete(id);
//			withdrawRequestDto.setMessage("X??a l???nh r??t ti???n th??nh c??ng.");
//			return withdrawRequestDto;
//		}
//
//		return (WithdrawRequestDTO) this.ExceptionObject(withdrawRequestDto,
//				"L???nh r??t ti???n n??y kh??ng t???n t???i.");
		return new WithdrawRequestDTO();
	}
}
