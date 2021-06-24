package com.move_up.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.move_up.entity.AnnouncementEntity;

public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long>{
	AnnouncementEntity findOneByCode(String code);
}
