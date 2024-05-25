package com.zerobase.account.dto;

import com.zerobase.account.domain.Account;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AccountDto {
    private Long userId;
    private String accountNumber;
    private LocalDateTime registerAt;

    public static AccountDto fromEntity(Account account) {
        return AccountDto.builder()
                .userId(account.getAccountUser().getId())
                .accountNumber(account.getAccountNumber())
                .registerAt(account.getRegisteredAt())
                .build();
    }
}
