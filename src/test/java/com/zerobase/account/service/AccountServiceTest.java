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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.zerobase.account.type.AccountStatus.IN_USE;
import static com.zerobase.account.type.AccountStatus.UNREGISTERED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        AccountUser user = AccountUser.builder().id(1L).username("Lee").build();
        given(accountUserRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(accountRepository.existsAccountByAccountNumber(anyString())).willReturn(false);
        given(accountRepository.save(any())).willReturn(Account.builder().accountUser(user).accountNumber("1234567890").build());
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
        AccountUser user = AccountUser.builder().id(1L).username("Lee").build();
        given(accountUserRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        AccountException accountException = assertThrows(AccountException.class, () -> accountService.createAccount(1L, 1000L));
        //then
        assertEquals(accountException.getErrorCode(), CustomErrorCode.USER_NOT_FOUND);

    }


    @Test
    @DisplayName("계좌 생성 실패 - 계좌가 10개가 넘음")
    public void createAccount_ALREADY_OVER_10_ACCOUNT() {
        //given
        AccountUser user = AccountUser.builder().id(1L).username("Lee").build();
        given(accountUserRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(accountRepository.countByAccountUser(any())).willReturn(10);
        //when
        AccountException accountException = assertThrows(AccountException.class, () -> accountService.createAccount(1L, 1000L));
        //then
        assertEquals(accountException.getErrorCode(), CustomErrorCode.ALREADY_OVER_10_ACCOUNT);

    }

    @Test
    @DisplayName("계좌 해지 성공")
    void deleteAccountSuccess() {
        //given
        AccountUser user = AccountUser.builder().id(1L).username("Lee").build();
        Account account = Account.builder().accountUser(user).accountStatus(IN_USE).balance(0L).accountNumber("1234567890").build();

        given(accountUserRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(accountRepository.findByAccountNumber(anyString())).willReturn(Optional.of(account));
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        //when
        AccountDto accountDto = accountService.deleteAccount(1L, "1234007890");

        //then
        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals(1L, accountDto.getUserId());
        assertEquals("1234567890", captor.getValue().getAccountNumber());
    }

    @Test
    @DisplayName("계좌 해지 실패 (사용자가 없는 경우) ")
    void deleteAccountFail_USER_NOT_FOUND() {
        //given
        AccountUser user = AccountUser.builder().id(1L).username("Lee").build();
        given(accountUserRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        AccountException accountException = assertThrows(AccountException.class, () -> accountService.deleteAccount(1L, "1234007890"));
        //then
        assertEquals(CustomErrorCode.USER_NOT_FOUND, accountException.getErrorCode());
    }

    @Test
    @DisplayName("계좌 해지 실패 (사용자 아이디와 계좌 소유주가 다른 경우) ")
    void deleteAccountFail_MATCH_USER_DIFFERENT() {
        //given
        AccountUser user = AccountUser.builder().id(1L).username("Lee").build();
        given(accountUserRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(Account.builder()
                        .accountNumber("1234567890")
                        .accountUser(AccountUser.builder()
                                .id(10L)
                                .build())
                        .build()));

        //when
        AccountException accountException = assertThrows(AccountException.class, () -> accountService.deleteAccount(1L, "1234007890"));
        //then
        assertEquals(CustomErrorCode.MATCH_USER_DIFFERENT, accountException.getErrorCode());
    }

    @Test
    @DisplayName("계좌 해지 실패 (계좌가 이미 해지 상태인 경우) ")
    void deleteAccountFail_ALREADY_UNREGISTERED() {
        //given
        AccountUser user = AccountUser.builder().id(1L).username("Lee").build();
        given(accountUserRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(Account.builder()
                        .accountNumber("1234567890")
                        .accountUser(user)
                        .accountStatus(UNREGISTERED)
                        .build()));

        //when
        AccountException accountException = assertThrows(AccountException.class, () -> accountService.deleteAccount(1L, "1234007890"));
        //then
        assertEquals(CustomErrorCode.ALREADY_UNREGISTERED, accountException.getErrorCode());
    }

    @Test
    @DisplayName("계좌 해지 실패 (잔액이 있는 경우) ")
    void deleteAccountFail_BALANCE_EXISTED() {
        //given
        AccountUser user = AccountUser.builder().id(1L).username("Lee").build();
        given(accountUserRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(Account.builder()
                        .accountNumber("1234567890")
                        .accountUser(user)
                        .balance(10L)
                        .build()));

        //when
        AccountException accountException = assertThrows(AccountException.class, () -> accountService.deleteAccount(1L, "1234007890"));
        //then
        assertEquals(CustomErrorCode.BALANCE_EXISTED, accountException.getErrorCode());
    }
}