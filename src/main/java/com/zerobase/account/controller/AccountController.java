package com.zerobase.account.controller;

import com.zerobase.account.dto.AccountDto;
import com.zerobase.account.dto.AccountListInfo;
import com.zerobase.account.dto.CreateAccount;
import com.zerobase.account.dto.DeleteAccount;
import com.zerobase.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public CreateAccount.Response createAccount(@RequestBody @Valid CreateAccount.Request request) {
        AccountDto account = accountService.createAccount(request.getUserId(), request.getAmount());

        return CreateAccount.Response.from(account);
    }

    @DeleteMapping
    public DeleteAccount.Response deleteAccount(@RequestBody @Valid DeleteAccount.Request request){
        AccountDto account = accountService.deleteAccount(request.getUserId(), request.getAccountNumber());

        return DeleteAccount.Response.from(account);
    }

    @GetMapping("/{userId}")
    public List<AccountListInfo> getAccountByUserId(@PathVariable(name = "userId") Long userId){
        List<AccountDto> accounts = accountService.getAccount(userId);

        return accounts.stream().map(
                accountDto -> AccountListInfo.builder()
                        .accountNumber(accountDto.getAccountNumber())
                        .balance(accountDto.getBalance())
                        .build())
                .collect(Collectors.toList());
    }
}
