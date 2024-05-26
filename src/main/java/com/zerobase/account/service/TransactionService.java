package com.zerobase.account.service;

import com.zerobase.account.domain.Account;
import com.zerobase.account.domain.AccountUser;
import com.zerobase.account.domain.Transaction;
import com.zerobase.account.dto.TransactionDto;
import com.zerobase.account.exception.AccountException;
import com.zerobase.account.repository.AccountRepository;
import com.zerobase.account.repository.AccountUserRepository;
import com.zerobase.account.repository.TransactionRepository;
import com.zerobase.account.type.AccountStatus;
import com.zerobase.account.type.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.zerobase.account.type.TransactionResultType.FAIL;
import static com.zerobase.account.type.TransactionResultType.SUCCESS;
import static com.zerobase.account.type.TransactionType.USE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountUserRepository accountUserRepository;
    private final AccountRepository accountRepository;


    @Transactional(readOnly = false)
    public TransactionDto useBalance(Long userId, String accountNumber, Long balance) {
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(CustomErrorCode.USER_NOT_FOUND));
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(CustomErrorCode.ACCOUNT_NOT_FOUND));

        validateUseBalance(accountUser, balance, account);

        // 계좌에서 돈 빼고
        // Transaction 생성
        account.useBalance(balance);
        Transaction transaction = transactionRepository.save(Transaction
                .builder()
                .transactionType(USE)
                .transactionResultType(SUCCESS)
                .account(account)
                .amount(balance)
                .transactionId(UUID.randomUUID().toString().replace("-", ""))
                .balanceSnapshot(account.getBalance())
                .transactedAt(LocalDateTime.now())
                .build());

        return TransactionDto.fromEntity(transaction);
    }

    private static void validateUseBalance(AccountUser accountUser, Long balance, Account account) {
        if (!Objects.equals(accountUser.getId(), account.getAccountUser().getId())) {
            throw new AccountException(CustomErrorCode.USER_UNMATCH);
        }
        if (account.getAccountStatus() == AccountStatus.UNREGISTERED) {
            throw new AccountException(CustomErrorCode.ALREADY_UNREGISTERED);
        }

        if (account.getBalance() < balance) {
            throw new AccountException(CustomErrorCode.TRANSACTION_FEE_OVER_ACCOUNT_BALANCE);
        }
        long min = 1000L;
        long max = 1000000000L;
        if (balance < min || balance > max) {
            throw new AccountException(CustomErrorCode.AMOUNT_TOO_BIG_OR_TOO_SMALL);
        }
    }

    @Transactional(readOnly = false)
    public void saveFailedUseTransaction(String accountNumber, Long balance) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException((CustomErrorCode.ACCOUNT_NOT_FOUND)));
        transactionRepository.save(
                Transaction.builder()
                        .transactionType(USE)
                        .transactionResultType(FAIL)
                        .account(account)
                        .amount(balance)
                        .balanceSnapshot(account.getBalance())
                        .transactionId(UUID.randomUUID().toString().replace("-", ""))
                        .transactedAt(LocalDateTime.now())
                        .build());
    }

    public TransactionDto getTransaction(String transactionId) {
        System.out.println(transactionId + " 끼이야");
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new AccountException(CustomErrorCode.TRANSACTION_NOT_FOUND));
        System.out.println(transaction.toString());
        return TransactionDto.fromEntity(transaction);

    }
}
