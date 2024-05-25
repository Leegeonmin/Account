package com.zerobase.account.controller;

import com.zerobase.account.dto.AccountDto;
import com.zerobase.account.dto.CreateAccount;
import com.zerobase.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public CreateAccount.Response createAccount(@RequestBody CreateAccount.Request request) {
        AccountDto account = accountService.createAccount(request.getUserId(), request.getAmount());

        return CreateAccount.Response.from(account);
    }
}
