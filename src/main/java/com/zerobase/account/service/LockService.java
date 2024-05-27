package com.zerobase.account.service;

import com.zerobase.account.exception.AccountException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

import static com.zerobase.account.type.CustomErrorCode.ACCOUNT_TRANSACTION_LOCK;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class LockService {
    private final RedissonClient reddisonClient;


    public void lock(String acountNumber){
        RLock lock = reddisonClient.getLock(getLockKey(acountNumber));
        log.debug("Trying lock for accountNumber : {}", acountNumber);

        try{
            boolean isLock = lock.tryLock(1, 15, TimeUnit.SECONDS);
            if(!isLock){
                log.error("=====Lock acquisition failed======");
                throw new AccountException(ACCOUNT_TRANSACTION_LOCK);
            }
        }catch (AccountException e){
            throw e;
        } catch(Exception e){
            log.error("Redis lock failed", e);
        }
    }

    public void unlock(String accountNumber){
        log.debug("unlock for accountNumber : {}", accountNumber);
        reddisonClient.getLock(getLockKey(accountNumber)).unlock();
    }
    private static String getLockKey(String accountNumber) {
        return "ACLK" + accountNumber;
    }
}
