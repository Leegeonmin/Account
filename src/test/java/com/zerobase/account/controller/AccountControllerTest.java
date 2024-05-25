package com.zerobase.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.account.dto.AccountDto;
import com.zerobase.account.dto.CreateAccount;
import com.zerobase.account.exception.AccountException;
import com.zerobase.account.service.AccountService;
import com.zerobase.account.type.CustomErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("계좌 만들기 성공")
    void successCreateAccount() throws Exception {
        //given
        given(accountService.createAccount(anyLong(), anyLong()))
                .willReturn(
                        AccountDto.builder()
                                .userId(1L)
                                .accountNumber("1234567890")
                                .registerAt(LocalDateTime.now())
                                .build()
                );
        //when
        //then
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateAccount.Request(1000L, 123L)
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andDo(print());

    }


    @Test
    @DisplayName("계좌 만들기 실패 (사용자ID 없음)")
    void failCreateAccountByNotFoundUser() throws Exception {
        //given
        given(accountService.createAccount(anyLong(), anyLong()))
                .willThrow(new AccountException(CustomErrorCode.USER_NOT_FOUND));
        //when
        //then
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateAccount.Request(1000L, 123L)
                        )))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"))
                .andExpect(jsonPath("$.errorMessage").value("ID와 일치하는 사용자가 없습니다."))
                .andDo(print());

    }

    @Test
    @DisplayName("계좌 만들기 실패 (사용자ID 없음)")
    void failCreateAccountByALREADY_OVER_10_ACCOUNT() throws Exception {
        //given
        given(accountService.createAccount(anyLong(), anyLong()))
                .willThrow(new AccountException(CustomErrorCode.ALREADY_OVER_10_ACCOUNT));
        //when
        //then
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateAccount.Request(1000L, 123L)
                        )))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorCode").value("ALREADY_OVER_10_ACCOUNT"))
                .andExpect(jsonPath("$.errorMessage").value("이미 10개가 넘는 계좌를 가지고 있습니다."))
                .andDo(print());

    }
}