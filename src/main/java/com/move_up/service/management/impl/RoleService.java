package com.move_up.service.management.impl;

import java.util.List;

import com.move_up.service.management.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.move_up.dto.RoleDTO;
import com.move_up.entity.RoleEntity;
import com.move_up.repository.RoleRepository;

@Service
public class RoleService extends BaseService implements IRoleService {

	@Autowired
	private RoleRepository roleRepo;
	
	@Override
	public RoleDTO findAll() {
		RoleDTO roleDto = new RoleDTO();
		List<RoleEntity> roleEntities = roleRepo.findAll();
		
		if (!roleEntities.isEmpty()) {
			for (RoleEntity roleEntity : roleEntities)
				roleDto.getListResult().add(this.converter.toDTO(roleEntity, RoleDTO.class));
			roleDto.setMessage("Load role list successfully.");
			return roleDto;
		}
		
		return (RoleDTO)this.ExceptionObject(roleDto, "There is no role.");
	}

	@Override
	public RoleDTO findOne(String code) {
		RoleDTO roleDto = new RoleDTO();
		RoleEntity roleEntity = roleRepo.findById(code).get();
		
		if (roleEntity != null) {
			roleDto = this.converter.toDTO(roleEntity, RoleDTO.class);
			roleDto.setMessage("Get role having code = " + code + " successfully.");
			return roleDto;
		}
		
		return (RoleDTO)this.ExceptionObject(roleDto, "Role does not exist.");
	}

	@Override
	public RoleDTO save(RoleDTO roleDto) {
		if (roleRepo.findById(roleDto.getCode()) == null) {
			RoleEntity roleEntity = roleRepo.save(this.converter.toEntity(roleDto, RoleEntity.class));
			roleDto = this.converter.toDTO(roleEntity, RoleDTO.class);
			roleDto.setMessage("Add role successfully.");
			return roleDto;
		}
		
		return (RoleDTO)this.ExceptionObject(roleDto, "This role code exists already.");
	}

	@Override
	public RoleDTO update(RoleDTO roleDto) {
		if (roleRepo.findById(roleDto.getCode()) != null) {
			RoleEntity roleEntity = roleRepo.save(this.converter.toEntity(roleDto, RoleEntity.class));
			roleDto = this.converter.toDTO(roleEntity, RoleDTO.class);
			roleDto.setMessage("Update role successfully.");
			return roleDto;
		}
		
		return (RoleDTO)this.ExceptionObject(roleDto, "This role code does not exist.");
	}

	@Override
	public RoleDTO delete(String code) {
		RoleDTO roleDto = new RoleDTO();
		if (roleRepo.findById(code) != null) {
			roleRepo.deleteById(code);
			roleDto.setMessage("Delete role successfully.");
			return roleDto;
		}
		
		return (RoleDTO)this.ExceptionObject(roleDto, "This role code does not exist.");
	}

}
