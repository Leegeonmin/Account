//package com.zerobase.account.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zerobase.account.dto.AccountDto;
//import com.zerobase.account.dto.CreateAccount;
//import com.zerobase.account.service.AccountService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(AccountController.class)
//class AccountControllerTest {
//
//    @MockBean
//    private AccountService accountService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("계좌 만들기 성공")
//    void successCreateAccount() {
//        //given
//        given(accountService.createAccount(anyLong(), anyLong()))
//                .willReturn(
//                        AccountDto.builder()
//                                .userId(1L)
//                                .accountNumber("1234567890")
//                                .registerAt(LocalDateTime.now())
//                                .build()
//                );
//        //when
//        //then
//
//        mockMvc.perform(post("/account")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(
//                                new CreateAccount.Request(1000L, 123L)
//                        )))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId").value(1L))
//                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
//                .andDo(print());
//
//    }
//}