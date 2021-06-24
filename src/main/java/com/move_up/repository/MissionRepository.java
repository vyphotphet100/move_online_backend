package com.move_up.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.move_up.entity.MissionEntity;

public interface MissionRepository extends JpaRepository<MissionEntity, Long>{

}
