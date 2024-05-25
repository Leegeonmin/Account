package com.zerobase.account.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class CreateAccount {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request{

        @NotNull @Min(1)
        private final Long userId;

        @NotNull @Min(1)
        private final Long amount;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response{
        private final Long userId;
        private final String accountNumber;
        private final LocalDateTime registeredAt;

        public static Response from(AccountDto accountDto) {
            return Response.builder()
                    .userId(accountDto.getUserId())
                    .accountNumber(accountDto.getAccountNumber())
                    .registeredAt(accountDto.getRegisterAt())
                    .build();
        }
    }
}
