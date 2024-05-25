package com.zerobase.account.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class AccountUser {
    @Id
    @GeneratedValue
    private Long id;

    private String username ;
    @CreatedDate
    private LocalDateTime createdAt;
}
