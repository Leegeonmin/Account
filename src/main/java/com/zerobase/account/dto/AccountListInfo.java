package com.zerobase.account.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class AccountListInfo {
    private String accountNumber;
    private Long balance;
}
