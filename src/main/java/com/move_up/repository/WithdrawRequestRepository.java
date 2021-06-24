package com.move_up.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.move_up.entity.WithdrawRequestEntity;

public interface WithdrawRequestRepository extends JpaRepository<WithdrawRequestEntity, Long>, PagingAndSortingRepository<WithdrawRequestEntity, Long>{

}
