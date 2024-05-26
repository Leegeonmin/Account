package com.zerobase.account.dto;

import com.zerobase.account.type.TransactionResultType;
import com.zerobase.account.type.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionInfo {
    private final String accountNumber;
    private final TransactionType transactionType;
    private final TransactionResultType transactionResultType;
    private final String transactionId;
    private final Long amount;
    private final LocalDateTime transactedAt;

    public static TransactionInfo from(TransactionDto dto){
        return TransactionInfo.builder()
                .accountNumber(dto.getAccountNumber())
                .transactionType(dto.getTransactionType())
                .transactionResultType(dto.getTransactionResultType())
                .transactionId(dto.getTransactionId())
                .amount(dto.getAmount())
                .transactedAt(dto.getTransactedAt())
                .build();
    }
}
