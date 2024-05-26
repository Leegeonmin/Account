package com.zerobase.account.controller;

import com.zerobase.account.dto.TransactionDto;
import com.zerobase.account.dto.UseTransaction;
import com.zerobase.account.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/use")
    public UseTransaction.Response useBalance(
            @RequestBody @Valid
            UseTransaction.Request request
    ){
        TransactionDto transaction = transactionService.useBalance(request.getUserId(), request.getAccountNumber()
                ,request.getBalance());

        return UseTransaction.Response.from(transaction);
    }
}
