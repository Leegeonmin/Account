package com.zerobase.account.dto;

import com.zerobase.account.type.CustomErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ErrorResponse {
    private final CustomErrorCode errorCode;
    private final String errorMessage;
    private String fieldError;
}
