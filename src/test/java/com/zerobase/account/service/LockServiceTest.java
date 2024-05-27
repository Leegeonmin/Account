package com.zerobase.account.service;

import com.zerobase.account.exception.AccountException;
import com.zerobase.account.type.CustomErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LockServiceTest {
    @Mock
    private RedissonClient reddisonClienct;

    @Mock
    private RLock rLock;

    @InjectMocks
    private LockService lockService;

    @Test
    void successGetLock() throws InterruptedException {
        //given
        given(reddisonClienct.getLock(anyString()))
                .willReturn(rLock);

        given(rLock.tryLock(anyLong(), anyLong(), any()))
                .willReturn(true);
        //then
        assertDoesNotThrow(()-> lockService.lock("123"));
    }

    @Test
    void failGetLock() throws InterruptedException {
        //given
        given(reddisonClienct.getLock(anyString()))
                .willReturn(rLock);

        given(rLock.tryLock(anyLong(), anyLong(), any()))
                .willReturn(false);
        //when
        AccountException accountException = assertThrows(AccountException.class,
                () -> lockService.lock("123"));

        //then
        assertEquals(CustomErrorCode.ACCOUNT_TRANSACTION_LOCK, accountException.getErrorCode());

    }
}