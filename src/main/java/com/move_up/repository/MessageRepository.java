package com.move_up.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.move_up.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long>{

}
