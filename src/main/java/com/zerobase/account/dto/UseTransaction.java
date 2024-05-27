package com.zerobase.account.dto;

import com.zerobase.account.aop.AccountLockIdInterface;
import com.zerobase.account.type.TransactionResultType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

public class UseTransaction {

    @Valid
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    public static class Request implements AccountLockIdInterface {
        @NotNull @Min(1)
        private  Long userId;
        @NotBlank @Size(min=10, max = 10)
        private  String accountNumber;
        @NotNull @Min(1)
        private  Long balance;
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
