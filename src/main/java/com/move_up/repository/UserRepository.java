package com.move_up.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.move_up.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String>{
	UserEntity findOneByUsernameAndPassword(String username, String password);
	UserEntity findOneByTokenCode(String tokenCode);
	List<UserEntity> findAllByReferrerUserUsername(String referrerUsername);
}
