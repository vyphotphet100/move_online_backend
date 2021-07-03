package com.move_up.service.management.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.move_up.service.management.IMomoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.move_up.dto.MomoDTO;
import com.move_up.entity.MomoEntity;
import com.move_up.entity.UserEntity;
import com.move_up.repository.MomoRepository;

@Service
public class MomoService extends BaseService implements IMomoService {

	@Autowired
	private MomoRepository momoRepo;

	@Override
	public MomoDTO findAll() {
		MomoDTO momoDto = new MomoDTO();
		List<MomoEntity> momoEntities = momoRepo.findAll();

		if (!momoEntities.isEmpty()) {
			for (MomoEntity momoEntity : momoEntities)
				momoDto.getListResult().add(this.converter.toDTO(momoEntity, MomoDTO.class));
			momoDto.setMessage("Load momo list successfully.");
			return momoDto;
		}

		return (MomoDTO) this.ExceptionObject(momoDto, "There is no momo.");
	}

	@Override
	public MomoDTO findOne(String phoneNumber, HttpServletRequest request) {
		UserEntity requestedUserEntity = this.getRequestedUser(request);
		if (requestedUserEntity == null)
			return (MomoDTO) this.ExceptionObject(new MomoDTO(), "Không tồn tại người dùng này.");

		if (!requestedUserEntity.getMomo().getPhoneNumber().equals(phoneNumber))
			return (MomoDTO) this.ExceptionObject(new MomoDTO(), "Access denied.");

		MomoDTO momoDto = new MomoDTO();
		MomoEntity momoEntity = momoRepo.findOne(phoneNumber);

		if (momoEntity != null) {
			momoDto = this.converter.toDTO(momoEntity, MomoDTO.class);
			momoDto.setMessage("Get momo having phone number = " + phoneNumber + " successfully.");
			return momoDto;
		}

		return (MomoDTO) this.ExceptionObject(momoDto, "Momo does not exist.");
	}

	@Override
	public MomoDTO save(MomoDTO momoDto, HttpServletRequest request) {
		MomoEntity momoEntity = momoRepo.findOne(momoDto.getPhoneNumber());

		UserEntity requestedUserEntity = this.getRequestedUser(request);
		if (requestedUserEntity == null)
			return (MomoDTO) this.ExceptionObject(new MomoDTO(), "Không tồn tại người dùng này.");

		if (momoEntity != null) {
			if (!momoEntity.getPhoneNumber().equals(requestedUserEntity.getMomo().getPhoneNumber()))
				return (MomoDTO) this.ExceptionObject(momoDto, "Số điện thoại này đã có người sử dụng.");
		}

		// save momo number at momo table
		momoEntity = momoRepo.save(this.converter.toEntity(momoDto, MomoEntity.class));
		momoDto = this.converter.toDTO(momoEntity, MomoDTO.class);
		momoDto.setMessage("Đã lưu thông tin thanh toán.");

		// save momo number at user table and delete old momo number of this user
		MomoEntity oldMomoEntity = requestedUserEntity.getMomo();
		requestedUserEntity.setMomo(momoEntity);
		userRepo.save(requestedUserEntity);
		if (oldMomoEntity != null && !oldMomoEntity.getPhoneNumber().equals(momoEntity.getPhoneNumber()))
			momoRepo.delete(oldMomoEntity);

		return momoDto;
	}

	@Override
	public MomoDTO update(MomoDTO momoDto) {
		if (momoRepo.findOne(momoDto.getPhoneNumber()) != null) {
			MomoEntity momoEntity = momoRepo.save(this.converter.toEntity(momoDto, MomoEntity.class));
			momoDto = this.converter.toDTO(momoEntity, MomoDTO.class);
			momoDto.setMessage("Update momo successfully.");
			return momoDto;
		}

		return (MomoDTO) this.ExceptionObject(momoDto, "This momo phone number does not exist.");
	}

	@Override
	public MomoDTO delete(String phoneNumber) {
		MomoDTO momoDto = new MomoDTO();
		if (momoRepo.findOne(phoneNumber) != null) {
			momoRepo.delete(phoneNumber);
			momoDto.setMessage("Delete momo successfully.");
			return momoDto;
		}

		return (MomoDTO) this.ExceptionObject(momoDto, "This momo phone number does not exist.");
	}

}
