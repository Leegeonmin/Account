package com.zerobase.account.dto;

import com.zerobase.account.domain.Transaction;
import com.zerobase.account.type.TransactionResultType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Builder
@Getter

public class TransactionDto {
    private final String accountNumber;
    private final TransactionResultType transactionResultType;
    private final String transactionId;
    private final Long amount;
    private final LocalDateTime transactedAt;

    public static TransactionDto fromEntity(Transaction transaction){
        return TransactionDto.builder()
                .accountNumber(transaction.getAccount().getAccountNumber())
                .transactionResultType(transaction.getTransactionResultType())
                .transactionId(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .transactedAt(transaction.getTransactedAt())
                .build();
    }
}
