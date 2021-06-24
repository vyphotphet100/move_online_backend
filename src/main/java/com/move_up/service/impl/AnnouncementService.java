package com.move_up.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.move_up.dto.AnnouncementDTO;
import com.move_up.entity.AnnouncementEntity;
import com.move_up.repository.AnnouncementRepository;
import com.move_up.service.IAnnouncementService;

@Service
public class AnnouncementService extends BaseService implements IAnnouncementService{

	@Autowired
	private AnnouncementRepository announcementRepo;
	
	@Override
	public AnnouncementDTO findAll() {
		AnnouncementDTO announcementDto = new AnnouncementDTO();
		List<AnnouncementEntity> announcementEntities = announcementRepo.findAll();
		
		if (!announcementEntities.isEmpty()) {
			for (AnnouncementEntity announcementEntity : announcementEntities)
				announcementDto.getListResult().add(this.converter.toDTO(announcementEntity, AnnouncementDTO.class));
			announcementDto.setMessage("Load announcement list successfully.");
			return announcementDto;
		}
		
		return (AnnouncementDTO)this.ExceptionObject(announcementDto, "There is no announcement.");
	}

	@Override
	public AnnouncementDTO findOne(Long id) {
		AnnouncementDTO announcementDto = new AnnouncementDTO();
		AnnouncementEntity announcementEntity = announcementRepo.findOne(id);
		
		if (announcementEntity != null) {
			announcementDto = this.converter.toDTO(announcementEntity, AnnouncementDTO.class);
			announcementDto.setMessage("Get announcement having id = " + id + " successfully.");
			return announcementDto;
		}
		
		return (AnnouncementDTO)this.ExceptionObject(announcementDto, "Announcement does not exist.");
	}

	@Override
	public AnnouncementDTO save(AnnouncementDTO announcementDto) {
		if (announcementRepo.findOneByCode(announcementDto.getCode()) == null) {
			AnnouncementEntity announcementEntity = announcementRepo.save(this.converter.toEntity(announcementDto, AnnouncementEntity.class));
			announcementDto = this.converter.toDTO(announcementEntity, AnnouncementDTO.class);
			announcementDto.setMessage("Add announcement successfully.");
			return announcementDto;
		}
		
		return (AnnouncementDTO)this.ExceptionObject(announcementDto, "This announcement code exists already.");
	}

	@Override
	public AnnouncementDTO update(AnnouncementDTO announcementDto) {
		if (announcementRepo.findOne(announcementDto.getId()) != null) {
			AnnouncementEntity announcementEntity = announcementRepo.save(this.converter.toEntity(announcementDto, AnnouncementEntity.class));
			announcementDto = this.converter.toDTO(announcementEntity, AnnouncementDTO.class);
			announcementDto.setMessage("Update announcement successfully.");
			return announcementDto;
		}
		
		return (AnnouncementDTO)this.ExceptionObject(announcementDto, "This announcement id does not exist.");
	}

	@Override
	public AnnouncementDTO delete(Long id) {
		AnnouncementDTO announcementDto = new AnnouncementDTO();
		if (announcementRepo.findOne(id) != null) {
			announcementRepo.delete(id);
			announcementDto.setMessage("Delete announcement successfully.");
			return announcementDto;
		}
		
		return (AnnouncementDTO)this.ExceptionObject(announcementDto, "This announcement id does not exist.");
	}

}
