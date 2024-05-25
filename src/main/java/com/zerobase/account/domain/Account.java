package com.zerobase.account.domain;


import com.zerobase.account.type.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor

public class Account {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private AccountUser accountUser;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @CreatedDate
    private LocalDateTime registeredAt;
    @LastModifiedDate
    private LocalDateTime unRegisteredAt;


}
