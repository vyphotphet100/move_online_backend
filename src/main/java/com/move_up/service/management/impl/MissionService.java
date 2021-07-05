package com.move_up.service.management.impl;

import java.util.List;

import com.move_up.dto.UserDTO;
import com.move_up.entity.UserEntity;
import com.move_up.global.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.move_up.dto.MissionDTO;
import com.move_up.entity.MissionEntity;
import com.move_up.repository.MissionRepository;
import com.move_up.service.management.IMissionService;

import javax.servlet.http.HttpServletRequest;

@Service
public class MissionService extends BaseService implements IMissionService{

	@Autowired
	private MissionRepository missionRepo;
	
	@Override
	public MissionDTO findAll() {
		MissionDTO missionDto = new MissionDTO();
		List<MissionEntity> missionEntities = missionRepo.findAll();
		
		if (!missionEntities.isEmpty()) {
			for (MissionEntity missionEntity : missionEntities)
				missionDto.getListResult().add(this.converter.toDTO(missionEntity, MissionDTO.class));
			missionDto.setMessage("Load mission list successfully.");
			return missionDto;
		}
		
		return (MissionDTO)this.ExceptionObject(missionDto, "There is no mission.");
	}

	@Override
	public MissionDTO findOne(Long id) {
		MissionDTO missionDto = new MissionDTO();
		MissionEntity missionEntity = missionRepo.findOne(id);
		
		if (missionEntity != null) {
			missionDto = this.converter.toDTO(missionEntity, MissionDTO.class);
			missionDto.setMessage("Get mission having id = " + id + " successfully.");
			return missionDto;
		}
		
		return (MissionDTO)this.ExceptionObject(missionDto, "Mission does not exist.");
	}

	@Override
	public MissionDTO save(MissionDTO missionDto) {
		MissionEntity missionEntity = missionRepo.save(this.converter.toEntity(missionDto, MissionEntity.class));
		missionDto = this.converter.toDTO(missionEntity, MissionDTO.class);
		missionDto.setMessage("Add mission successfully.");
		return missionDto;
	}

	@Override
	public MissionDTO update(MissionDTO missionDto) {
		if (missionRepo.findOne(missionDto.getId()) != null) {
			MissionEntity missionEntity = missionRepo.save(this.converter.toEntity(missionDto, MissionEntity.class));
			missionDto = this.converter.toDTO(missionEntity, MissionDTO.class);
			missionDto.setMessage("Update mission successfully.");
			return missionDto;
		}
		
		return (MissionDTO)this.ExceptionObject(missionDto, "This mission id does not exist.");
	}

	@Override
	public MissionDTO delete(Long id) {
		MissionDTO missionDto = new MissionDTO();
		if (missionRepo.findOne(id) != null) {
			missionRepo.delete(id);
			missionDto.setMessage("Delete mission successfully.");
			return missionDto;
		}
		
		return (MissionDTO)this.ExceptionObject(missionDto, "This mission id does not exist.");
	}

	@Override
	public MissionDTO doMission(HttpServletRequest request, Long missionId) {
		// check requested user
		UserEntity requestedUserEntity = this.getRequestedUser(request);
		if (requestedUserEntity == null)
			return (MissionDTO)this.ExceptionObject(new MissionDTO(), "Không tồn tại người dùng này.");

		// check requested site
		String requestedSite = (String)request.getHeader("Requested-site");
		if (!requestedSite.contains("do-mission.html"))
			return (MissionDTO)this.ExceptionObject(new MissionDTO(), "Thực hiện nhiệm vụ không thành công.");

		// check if user did mission already
		List<MissionEntity> missionEntities = requestedUserEntity.getMissions();
		for (MissionEntity missionEntity : missionEntities)
			if (missionEntity.getId().equals(missionId))
				return (MissionDTO)this.ExceptionObject(new MissionDTO(), "Bạn đã thực hiện nhiệm vụ này rồi.");

		// get info of mission
		MissionEntity missionEntity = missionRepo.findOne(missionId);
		if (missionEntity == null)
			return (MissionDTO)this.ExceptionObject(new MissionDTO(), "Không tồn tại nhiệm vụ này.");

		// check type of mission
		if (missionEntity.getType().equals("DIEMDANH")) {
			return this.checkIn(requestedUserEntity, missionEntity);
		}
		else if (missionEntity.getType().equals("VIEW-ADS")) {
			return this.viewAds(requestedUserEntity, missionEntity);
		}
		else if (missionEntity.getType().equals("CAPCHA")) {
			return this.confirmCapcha(requestedUserEntity, missionEntity);
		}

		return (MissionDTO)this.ExceptionObject(new MissionDTO(), "Có lỗi xảy ra.");
	}

	@Override
	public MissionDTO checkIn(UserEntity requestedUserEntity, MissionEntity missionEntity) {
		requestedUserEntity.getMissions().add(missionEntity);
		requestedUserEntity.setNumOfCoinGiftBox(requestedUserEntity.getNumOfCoinGiftBox() + missionEntity.getNumOfCoinGiftBox());
		requestedUserEntity.setNumOfTimeGiftBox(requestedUserEntity.getNumOfTimeGiftBox() + missionEntity.getNumOfTimeGiftBox());
		requestedUserEntity.setNumOfStar(requestedUserEntity.getNumOfStar() + missionEntity.getNumOfStar());
		requestedUserEntity = userRepo.save(requestedUserEntity);

		MissionDTO missionDto = this.converter.toDTO(missionEntity, MissionDTO.class);
		missionDto.setMessage("Điểm danh thành công.");
		return missionDto;
	}

	@Override
	public MissionDTO viewAds(UserEntity requestedUserEntity, MissionEntity missionEntity) {
		String userAttr = Global.userAttributes.get(requestedUserEntity.getUsername());
		if (!userAttr.contains("view-ads"))
			return (MissionDTO)this.ExceptionObject(new MissionDTO(), "Bạn chưa thực hiện nhiệm vụ này.");
		if (userAttr.contains("view-ads:counting;")) {
			Global.userAttributes.put(requestedUserEntity.getUsername(), userAttr.replace("view-ads:counting;", "view-ads:stopped;"));
			return (MissionDTO)this.ExceptionObject(new MissionDTO(), "Bạn chưa thực hiện xong nhiệm vụ này. Hãy thực hiện lại.");
		}

		// add gift for user
		requestedUserEntity.getMissions().add(missionEntity);
		requestedUserEntity.setNumOfCoinGiftBox(requestedUserEntity.getNumOfCoinGiftBox() + missionEntity.getNumOfCoinGiftBox());
		requestedUserEntity.setNumOfTimeGiftBox(requestedUserEntity.getNumOfTimeGiftBox() + missionEntity.getNumOfTimeGiftBox());
		requestedUserEntity.setNumOfStar(requestedUserEntity.getNumOfStar() + missionEntity.getNumOfStar());
		requestedUserEntity = userRepo.save(requestedUserEntity);

		MissionDTO missionDto = this.converter.toDTO(missionEntity, MissionDTO.class);
		missionDto.setMessage("Thực hiện nhiệm vụ thành công.");
		return missionDto;
	}

	@Override
	public MissionDTO confirmCapcha(UserEntity requestedUserEntity, MissionEntity missionEntity) {
		// add gift for user
		requestedUserEntity.getMissions().add(missionEntity);
		requestedUserEntity.setNumOfCoinGiftBox(requestedUserEntity.getNumOfCoinGiftBox() + missionEntity.getNumOfCoinGiftBox());
		requestedUserEntity.setNumOfTimeGiftBox(requestedUserEntity.getNumOfTimeGiftBox() + missionEntity.getNumOfTimeGiftBox());
		requestedUserEntity.setNumOfStar(requestedUserEntity.getNumOfStar() + missionEntity.getNumOfStar());
		requestedUserEntity = userRepo.save(requestedUserEntity);

		MissionDTO missionDto = this.converter.toDTO(missionEntity, MissionDTO.class);
		missionDto.setMessage("Thực hiện nhiệm vụ thành công.");
		return missionDto;
	}

}
