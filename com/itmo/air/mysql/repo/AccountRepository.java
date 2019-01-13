package com.itmo.air.mysql.repo;

import com.itmo.air.mysql.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Page<Account> findByUserId(Long userId, Pageable pageable);

    Optional<Account> findByNicName(String nicName);
}