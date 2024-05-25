package com.zerobase.account.service;

import com.zerobase.account.domain.Account;
import com.zerobase.account.domain.AccountUser;
import com.zerobase.account.dto.AccountDto;
import com.zerobase.account.exception.AccountException;
import com.zerobase.account.repository.AccountRepository;
import com.zerobase.account.repository.AccountUserRepository;
import com.zerobase.account.type.CustomErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountUserRepository accountUserRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("계좌 생성 성공")
    void createAccountSuccess() {
        //given
        AccountUser user = AccountUser.builder()
                .id(1L)
                .username("Lee")
                .build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.existsAccountByAccountNumber(anyString()))
                .willReturn(false);
        given(accountRepository.save(any()))
                .willReturn(
                        Account.builder()
                                .accountUser(user)
                                .accountNumber("1234567890")
                                .build()
                );
        //when
        AccountDto accountDto = accountService.createAccount(1L, 1000L);

        //then
        assertEquals(accountDto.getUserId(), user.getId());
        assertEquals(accountDto.getAccountNumber(), "1234567890");

    }

    @Test
    @DisplayName("계좌 생성 실패 - 유저 없음")
    public void createAccount_UserNotFound() {
        //given
        AccountUser user = AccountUser.builder()
                .id(1L)
                .username("Lee")
                .build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        AccountException accountException = assertThrows(AccountException.class,
                () -> accountService.createAccount(1L, 1000L));
        //then
        assertEquals(accountException.getErrorCode(), CustomErrorCode.USER_NOT_FOUND);

    }


    @Test
    @DisplayName("계좌 생성 실패 - 계좌가 10개가 넘음")
    public void createAccount_ALREADY_OVER_10_ACCOUNT() {
        //given
        AccountUser user = AccountUser.builder()
                .id(1L)
                .username("Lee")
                .build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.countByAccountUser(any()))
                .willReturn(10);
        //when
        AccountException accountException = assertThrows(AccountException.class,
                () -> accountService.createAccount(1L, 1000L));
        //then
        assertEquals(accountException.getErrorCode(), CustomErrorCode.ALREADY_OVER_10_ACCOUNT);

    }

}