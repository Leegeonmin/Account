package com.zerobase.account.dto;

import com.zerobase.account.type.TransactionResultType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class UseTransaction {


    @Getter
    @AllArgsConstructor
    public static class Request{
        private final Long userId;
        private final String accountNumber;
        private final Long balance;
    }

    @Builder
    @Getter

    public static class Response{
        private final String accountNumber;
        private final TransactionResultType transactionResultType;
        private final String transactionId;
        private final Long amount;
        private final LocalDateTime transactedAt;

        public static Response from(TransactionDto dto){
            return Response.builder()
                    .accountNumber(dto.getAccountNumber())
                    .transactionResultType(dto.getTransactionResultType())
                    .transactionId(dto.getTransactionId())
                    .amount(dto.getAmount())
                    .transactedAt(dto.getTransactedAt())
                    .build();
        }
    }
}
