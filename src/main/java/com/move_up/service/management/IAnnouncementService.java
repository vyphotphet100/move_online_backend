package com.move_up.service.management;

import com.move_up.dto.AnnouncementDTO;

public interface IAnnouncementService extends IBaseService{

	AnnouncementDTO findAll();
	AnnouncementDTO findOne(Long id);
	AnnouncementDTO save(AnnouncementDTO announcementDto);
	AnnouncementDTO update(AnnouncementDTO announcementDto);
	AnnouncementDTO delete(Long id);
}
