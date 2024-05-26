package com.zerobase.account.domain;


import com.zerobase.account.type.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

import static com.zerobase.account.type.AccountStatus.UNREGISTERED;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private AccountUser accountUser;
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private Long balance;

    @CreatedDate
    private LocalDateTime registeredAt;
    @LastModifiedDate
    private LocalDateTime unRegisteredAt;

    public void deleteAccount(){
        this.setAccountStatus(UNREGISTERED);
        this.setUnRegisteredAt(LocalDateTime.now());
    }
}
