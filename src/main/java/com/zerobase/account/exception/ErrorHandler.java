package com.zerobase.account.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.zerobase.account.type.CustomErrorCode;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus
    @ExceptionHandler(AccountException.class)
    public ErrorResponse handleAccountException(AccountException e) {
        log.error("{} is occurred", e.getErrorCode());
        return new ErrorResponse(e.getErrorCode(), e.getMessage());
    }


    //요청 데이터 오류
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException is occurred", e);

        BindingResult bindingResult = e.getBindingResult();
        FieldError error = bindingResult.getFieldError();
        String fieldError = String.format("Field: %s, Error: %s", error.getField(), error.getDefaultMessage());

        return new ErrorResponse(CustomErrorCode.INVALID_REQUEST,
                CustomErrorCode.INVALID_REQUEST.getDescription(),
                fieldError);
    }

}
