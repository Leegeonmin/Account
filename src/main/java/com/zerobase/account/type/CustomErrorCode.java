package com.zerobase.account.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    INVALID_REQUEST("요청이 잘못되었습니다."),
    ALREADY_OVER_10_ACCOUNT("이미 10개가 넘는 계좌를 가지고 있습니다."),
    USER_NOT_FOUND("ID와 일치하는 사용자가 없습니다.");

    private final String description;
}
