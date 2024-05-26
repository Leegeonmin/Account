package com.zerobase.account.repository;

import com.zerobase.account.domain.Account;
import com.zerobase.account.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository  extends JpaRepository<Account, Long> {
    Integer countByAccountUser(AccountUser accountUser);
    boolean existsAccountByAccountNumber(String accountNumber);
    Optional<Account> findByAccountNumber(String accountNumber);
}
