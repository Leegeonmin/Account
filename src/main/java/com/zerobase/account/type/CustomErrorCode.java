package com.zerobase.account.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    AMOUNT_TOO_BIG_OR_TOO_SMALL("거래금액이 너무 작거나 너무 큽니다"),
    TRANSACTION_FEE_OVER_ACCOUNT_BALANCE("거래금액이 잔액보다 큽니다"),
    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다"),
    BALANCE_EXISTED("잔액이 남아있어서 해지할 수 없습니다"),
    ALREADY_UNREGISTERED("이미 해지된 계좌입니다"),
    USER_UNMATCH("사용자 아이디와 계좌 소유주가 다릅니다"),
    ACCOUNT_NOT_FOUND("계좌를 찾을 수 없습니다"),
    INVALID_REQUEST("요청이 잘못되었습니다."),
    ALREADY_OVER_10_ACCOUNT("이미 10개가 넘는 계좌를 가지고 있습니다."),
    USER_NOT_FOUND("ID와 일치하는 사용자가 없습니다.");

    private final String description;
}
