package com.zerobase.account.controller;

import com.zerobase.account.dto.TransactionDto;
import com.zerobase.account.dto.TransactionInfo;
import com.zerobase.account.dto.UseTransaction;
import com.zerobase.account.exception.AccountException;
import com.zerobase.account.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/use")
    public UseTransaction.Response useBalance(
            @RequestBody @Valid
            UseTransaction.Request request
    ) {
        try {
            TransactionDto transaction = transactionService.useBalance(request.getUserId(), request.getAccountNumber()
                    , request.getBalance());

            return UseTransaction.Response.from(transaction);
        } catch (AccountException e) {
            log.error("Failed to use balance");
            transactionService.saveFailedUseTransaction(request.getAccountNumber(), request.getBalance());
            throw e;
        }

    }

    @GetMapping()
    public TransactionInfo getTransaction(@RequestParam(name = "transactionId") String transactionId) {
        System.out.println("끼야홋" + transactionId);
        TransactionDto transaction = transactionService.getTransaction(transactionId);
        return TransactionInfo.from(transaction);
    }
}
