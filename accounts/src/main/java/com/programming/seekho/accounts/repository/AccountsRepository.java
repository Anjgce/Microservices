package com.programming.seekho.accounts.repository;

import com.programming.seekho.accounts.entity.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    Optional<Accounts> findByCustomerId(Long customerId);

    @Transactional // This annotation is used if operation is not successful then it should be rollback
    @Modifying // This annotation is used in case of modifying the data and this annotation comes under @Transactional
    void deleteByCustomerId(Long customerId);
}
