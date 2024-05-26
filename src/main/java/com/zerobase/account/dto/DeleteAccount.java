package com.zerobase.account.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public class DeleteAccount {

    @Getter
    @RequiredArgsConstructor
    @Builder
    @Valid
    public static class Request{
        @NotNull @Min(1)
        private final Long userId;

        @NotBlank @Size(min = 10, max = 10)
        private final String accountNumber;
    }


    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class Response{
        private final Long userId;
        private final String accountNumber;
        private final LocalDateTime unregisteredAt;

        public static Response from(AccountDto dto){
            return Response.builder()
                    .userId(dto.getUserId())
                    .accountNumber(dto.getAccountNumber())
                    .unregisteredAt(LocalDateTime.now())
                    .build();
        }
    }
}
