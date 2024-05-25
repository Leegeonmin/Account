package com.zerobase.account.service;

import com.zerobase.account.domain.Account;
import com.zerobase.account.domain.AccountUser;
import com.zerobase.account.dto.AccountDto;
import com.zerobase.account.exception.AccountException;
import com.zerobase.account.repository.AccountRepository;
import com.zerobase.account.repository.AccountUserRepository;
import com.zerobase.account.type.AccountStatus;
import com.zerobase.account.type.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;


    @Transactional(readOnly = false)
    public AccountDto createAccount(Long userId, Long amount) {
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(CustomErrorCode.USER_NOT_FOUND));

        validateCreateAccount(accountUser);
        String newAccountNumber = makeNewAccountNumber();
        Account account = accountRepository.save(
                 Account.builder()
                        .accountNumber(newAccountNumber)
                        .accountUser(accountUser)
                        .accountStatus(AccountStatus.IN_USE)
                        .balance(amount)
                        .registeredAt(LocalDateTime.now())
                        .build()
        );

        return AccountDto.fromEntity(account);

    }

    private String makeNewAccountNumber() {
        // 시작 숫자가 1부터인 10자리 랜덤 정수 생성
        long min = 1000000000L; // 10자리 정수의 최소값
        long max = 9999999999L; // 10자리 정수의 최대값
        while(true){
            String newAccountNum = String.valueOf(ThreadLocalRandom.current().nextLong(min, max + 1));
            if(!accountRepository.existsAccountByAccountNumber(newAccountNum)){
                return newAccountNum;
            }
        }
    }

    public void validateCreateAccount(AccountUser accountUser) {
        if (accountRepository.countByAccountUser(accountUser) >= 10) {
            throw new AccountException(CustomErrorCode.ALREADY_OVER_10_ACCOUNT);
        }
    }
}
