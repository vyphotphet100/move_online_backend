package com.move_up.service.management;

import com.move_up.dto.MissionDTO;
import com.move_up.entity.MissionEntity;
import com.move_up.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;

public interface IMissionService extends IBaseService{

	MissionDTO findAll();
	MissionDTO findOne(Long id);
	MissionDTO save(MissionDTO missionDto);
	MissionDTO update(MissionDTO missionDto);
	MissionDTO delete(Long id);

	// do mission
	MissionDTO doMission(HttpServletRequest request, Long missionId);
	MissionDTO checkIn(UserEntity requestedUserEntity, MissionEntity missionEntity);
	MissionDTO viewAds(UserEntity requestedUserEntity, MissionEntity missionEntity);
}
