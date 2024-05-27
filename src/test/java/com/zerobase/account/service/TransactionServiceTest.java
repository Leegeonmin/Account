package com.zerobase.account.service;

import com.zerobase.account.domain.Account;
import com.zerobase.account.domain.AccountUser;
import com.zerobase.account.domain.Transaction;
import com.zerobase.account.dto.TransactionDto;
import com.zerobase.account.exception.AccountException;
import com.zerobase.account.repository.AccountRepository;
import com.zerobase.account.repository.AccountUserRepository;
import com.zerobase.account.repository.TransactionRepository;
import com.zerobase.account.type.CustomErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.zerobase.account.type.AccountStatus.IN_USE;
import static com.zerobase.account.type.AccountStatus.UNREGISTERED;
import static com.zerobase.account.type.CustomErrorCode.TRANSACTION_NOT_FOUND;
import static com.zerobase.account.type.TransactionResultType.SUCCESS;
import static com.zerobase.account.type.TransactionType.USE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountUserRepository accountUserRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("잔액 사용 성공")
    void successUseBalance() {
        AccountUser user = AccountUser.builder()
                .id(1L)
                .username("lee")
                .build();
        Account account = Account.builder()
                .id(1L)
                .balance(10000L)
                .accountUser(user)
                .accountNumber("1234567890")
                .accountStatus(IN_USE)
                .build();
        //given
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account));
        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(2000L)
                .transactionResultType(SUCCESS)
                .build();
        given(transactionRepository.save(any(Transaction.class)))
                .willReturn(transaction);

        //when
        TransactionDto transactionDto = transactionService.useBalance(1L, "1234567800", 2000L);

        //then
        assertEquals("1234567890", transactionDto.getAccountNumber());
        assertEquals(SUCCESS, transactionDto.getTransactionResultType());

    }


    @Test
    @DisplayName("잔액 사용 실패 (사용자가 없는 경우)")
    void successUseBalance_USER_NOT_FOUND() {
        //given
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        //then
        AccountException accountException = assertThrows(AccountException.class,
                () -> transactionService.useBalance(1L, "1234567800", 2000L));

        assertEquals(CustomErrorCode.USER_NOT_FOUND, accountException.getErrorCode());
    }

    @Test
    @DisplayName("잔액 사용 실패 (사용자 아이디와 계좌 소유주가 다른 경우)")
    void successUseBalance_MATCH_USER_DIFFERENT() {
        //given
        AccountUser user = AccountUser.builder()
                .id(1L)
                .username("lee")
                .build();
        AccountUser user1 = AccountUser.builder()
                .id(10L)
                .username("kim")
                .build();
        Account account = Account.builder()
                .id(2L)
                .balance(0L)
                .accountUser(user1)
                .accountNumber("1234567890")
                .accountStatus(IN_USE)
                .build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account));
        //when
        //then
        AccountException accountException = assertThrows(AccountException.class,
                () -> transactionService.useBalance(1L, "1234567800", 1000L));

        assertEquals(CustomErrorCode.USER_UNMATCH, accountException.getErrorCode());
    }

    @Test
    @DisplayName("잔액 사용 실패 (계좌가 이미 해지 상태인 경우)")
    void successUseBalance_ALREADY_UNREGISTERED() {
        //given
        AccountUser user = AccountUser.builder()
                .id(1L)
                .username("lee")
                .build();
        Account account = Account.builder()
                .id(2L)
                .balance(0L)
                .accountUser(user)
                .accountNumber("1234567890")
                .accountStatus(UNREGISTERED)
                .build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account));
        //when
        //then
        AccountException accountException = assertThrows(AccountException.class,
                () -> transactionService.useBalance(1L, "1234567800", 1000L));

        assertEquals(CustomErrorCode.ALREADY_UNREGISTERED, accountException.getErrorCode());
    }

    @Test
    @DisplayName("잔액 사용 실패 (거래금액이 잔액보다 큰 경우)")
    void successUseBalance_TRANSACTION_FEE_OVER_ACCOUNT_BALANCE() {
        //given
        AccountUser user = AccountUser.builder()
                .id(1L)
                .username("lee")
                .build();
        Account account = Account.builder()
                .id(2L)
                .balance(9000L)
                .accountUser(user)
                .accountNumber("1234567890")
                .accountStatus(IN_USE)
                .build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account));
        //when
        //then
        AccountException accountException = assertThrows(AccountException.class,
                () -> transactionService.useBalance(1L, "1234567800", 9100L));

        assertEquals(CustomErrorCode.TRANSACTION_FEE_OVER_ACCOUNT_BALANCE, accountException.getErrorCode());
    }

    @Test
    @DisplayName("잔액 사용 실패 (거래금액이 너무 작거나 큰 경우)")
    void successUseBalance_AMOUNT_TOO_BIG_OR_TOO_SMALL() {
        //given
        AccountUser user = AccountUser.builder()
                .id(1L)
                .username("lee")
                .build();
        Account account = Account.builder()
                .id(2L)
                .balance(5000L)
                .accountUser(user)
                .accountNumber("1234567890")
                .accountStatus(IN_USE)
                .build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account));
        //when
        //then
        AccountException accountException = assertThrows(AccountException.class,
                () -> transactionService.useBalance(1L, "1234567800", 500L));

        assertEquals(CustomErrorCode.AMOUNT_TOO_BIG_OR_TOO_SMALL, accountException.getErrorCode());
    }


    @Test
    @DisplayName("거래 확인 성공")
    void successgetTransaction() {

        //given
        given(transactionRepository.findByTransactionId(anyString()))
                .willReturn(Optional.of(Transaction.builder()
                        .transactionId("transactionId")
                        .transactionType(USE)
                        .account(Account.builder()
                                .accountUser(AccountUser.builder()
                                        .username("lee")
                                        .id(1l)
                                        .build())
                                .accountStatus(IN_USE)
                                .accountNumber("1234567890")
                                .balance(1000L)
                                .build()
                        )
                        .transactionResultType(SUCCESS)
                        .amount(1000L)
                        .build()));
        //when
        TransactionDto transactionDto = transactionService.getTransaction("transactionId");

        //then
        assertEquals("transactionId", transactionDto.getTransactionId());

    }

    @Test
    @DisplayName("거래 확인 실패(해당 transaction ID가 없는 경우")
    void failgetTransaction() {

        //given
        given(transactionRepository.findByTransactionId(anyString()))
                .willReturn(Optional.empty());
        //when
        AccountException accountException = assertThrows(AccountException.class, () ->
                transactionService.getTransaction("1234567800"));
        //then
        assertEquals(TRANSACTION_NOT_FOUND, accountException.getErrorCode());

    }

}