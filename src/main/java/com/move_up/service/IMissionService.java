package com.move_up.service;

import com.move_up.dto.MissionDTO;

public interface IMissionService extends IBaseService{

	MissionDTO findAll();
	MissionDTO findOne(Long id);
	MissionDTO save(MissionDTO missionDto);
	MissionDTO update(MissionDTO missionDto);
	MissionDTO delete(Long id);
}
