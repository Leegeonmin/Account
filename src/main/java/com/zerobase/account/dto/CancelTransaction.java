package com.zerobase.account.dto;


import com.zerobase.account.type.TransactionResultType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class CancelTransaction {
    @AllArgsConstructor
    @Valid
    @Getter
    public static class Request{
        @NotBlank
        private final String transactionId;
        @NotBlank @Size(min = 10, max = 10)
        private final String accountNumber;
        @NotNull @Min(1)
        private final Long amount;
    }

    @AllArgsConstructor
    @Builder
    @Getter
    public static class Response{
        private String accountNumber;
        private TransactionResultType transactionResultType;
        private String transactionId;
        private Long cancelAmount;
        private LocalDateTime transactedAt;

        public static Response from(TransactionDto dto){
            return Response.builder()
                    .accountNumber(dto.getAccountNumber())
                    .transactionResultType(dto.getTransactionResultType())
                    .transactionId(dto.getTransactionId())
                    .cancelAmount(dto.getAmount())
                    .transactedAt(dto.getTransactedAt())
                    .build();
        }
    }
}
