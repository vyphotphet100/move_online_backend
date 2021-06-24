package com.move_up.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.move_up.dto.MissionDTO;
import com.move_up.entity.MissionEntity;
import com.move_up.repository.MissionRepository;
import com.move_up.service.IMissionService;

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

}
