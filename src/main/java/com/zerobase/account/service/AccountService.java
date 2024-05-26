package com.zerobase.account.service;

import com.zerobase.account.domain.Account;
import com.zerobase.account.domain.AccountUser;
import com.zerobase.account.dto.AccountDto;
import com.zerobase.account.exception.AccountException;
import com.zerobase.account.repository.AccountRepository;
import com.zerobase.account.repository.AccountUserRepository;
import com.zerobase.account.type.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.zerobase.account.type.CustomErrorCode.*;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;


    @Transactional(readOnly = false)
    public AccountDto createAccount(Long userId, Long amount) {
        AccountUser accountUser = getAccountUser(userId);

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

    private AccountUser getAccountUser(Long userId) {
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(USER_NOT_FOUND));
        return accountUser;
    }

    private String makeNewAccountNumber() {
        // 시작 숫자가 1부터인 10자리 랜덤 정수 생성
        long min = 1000000000L; // 10자리 정수의 최소값
        long max = 9999999999L; // 10자리 정수의 최대값
        while (true) {
            String newAccountNum = String.valueOf(ThreadLocalRandom.current().nextLong(min, max + 1));
            if (!accountRepository.existsAccountByAccountNumber(newAccountNum)) {
                return newAccountNum;
            }
        }
    }

    public void validateCreateAccount(AccountUser accountUser) {
        if (accountRepository.countByAccountUser(accountUser) >= 10) {
            throw new AccountException(ALREADY_OVER_10_ACCOUNT);
        }
    }

    @Transactional(readOnly = false)
    public AccountDto deleteAccount(Long userId, String accountNumber) {
        AccountUser accountUser = getAccountUser(userId);
        Account account = getAccount(accountNumber);

        validateDeleteAccount(accountUser, account);

        account.deleteAccount();
        accountRepository.save(account);

        return AccountDto.fromEntity(account);
    }

    private static void validateDeleteAccount(AccountUser accountUser, Account account) {
        if (!accountUser.getId().equals(account.getAccountUser().getId())) {
            throw new AccountException(MATCH_USER_DIFFERENT);
        }

        if (account.getAccountStatus() == AccountStatus.UNREGISTERED) {
            throw new AccountException(ALREADY_UNREGISTERED);
        }

        if (account.getBalance() > 0) {
            throw new AccountException(BALANCE_EXISTED);
        }
    }

    private Account getAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));
        return account;
    }


    public List<AccountDto> getAccount(Long userId) {
        AccountUser user = accountUserRepository.findById(userId)
                .orElseThrow(()-> new AccountException(USER_NOT_FOUND));
        List<Account> accounts = accountRepository.findByAccountUser(user);
        return accounts.stream()
                .map(AccountDto::fromEntity)
                .collect(Collectors.toList());
    }
}
