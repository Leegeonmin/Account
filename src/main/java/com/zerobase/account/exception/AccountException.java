package com.zerobase.account.exception;


import com.zerobase.account.type.CustomErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AccountException extends  RuntimeException {
    private final CustomErrorCode errorCode;
    private final String message;
    public AccountException(CustomErrorCode customErrorCodec){
        this.errorCode = customErrorCodec;
        this.message = customErrorCodec.getDescription();
    }
}
