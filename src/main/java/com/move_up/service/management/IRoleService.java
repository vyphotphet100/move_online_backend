package com.move_up.service.management;

import com.move_up.dto.RoleDTO;

public interface IRoleService extends IBaseService{

	RoleDTO findAll();
	RoleDTO findOne(String code);
	RoleDTO save(RoleDTO roleDto);
	RoleDTO update(RoleDTO roleDto);
	RoleDTO delete(String code);
}
