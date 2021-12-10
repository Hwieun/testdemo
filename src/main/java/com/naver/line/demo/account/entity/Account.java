package com.naver.line.demo.account.entity;


import com.naver.line.demo.common.Constants;
import com.naver.line.demo.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "accounts")
@NoArgsConstructor
public class Account extends Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @Column(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String number;

    @Column
    private Long amount;

    @Enumerated
    @Column
    private Status status;

    @Column(name = "transfer_limit")
    private Integer transferLimit;

    @Column(name = "daily_transfer_limit")
    private Integer dailyTransferLimit;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum Status {
        ENABLED, DISABLED,
    }

    @Builder
    public Account(User user, Long number, Integer transferLimit, Integer dailyTransferLimit) {
        if (transferLimit > 5000000 || transferLimit < 0 || dailyTransferLimit < 0 || dailyTransferLimit > 10000000)
            throw new IllegalStateException("wrong data");
        this.user = user;
        this.amount = 0L;
        this.transferLimit = transferLimit;
        this.dailyTransferLimit = dailyTransferLimit;
        this.status = Status.ENABLED;
        this.number = new StringBuilder(String.valueOf(number)).insert(3, "-").insert(6, "-").toString();
    }

    public void deactivate() {
        if (status.equals(Status.DISABLED) || amount > 0)
            throw new IllegalStateException("계좌가 비활성화 상태입니다.");
        this.status = Status.DISABLED;
    }

    public void update(Integer transferLimit, Integer dailyTransferLimit) {
        validateStatus();
        this.transferLimit = transferLimit;
        this.dailyTransferLimit = dailyTransferLimit;
    }

    public void withdraw(long dailyTotalTransferAmount, long amount) {
//        validateStatus();
        if(this.amount < amount) throw new IllegalArgumentException("출금액이 출금 계좌의 잔액보다 많습니다.");
        if(this.transferLimit < amount) throw new IllegalArgumentException("이체 금액이  1회 이체 한도를 초과하였습니다.");
        if(this.dailyTransferLimit < dailyTotalTransferAmount + amount)
            throw new IllegalArgumentException("당일 총 이체 금액과 이체 금액의 합이  1일 이체 한도를 초과하였습니다.");
        this.amount -= amount;
    }

    public void deposit(long dailyTotalTransferAmount, long amount) {
//        validateStatus();
        this.amount += amount;
    }

    public void validateStatus() {
        if (status.equals(Status.DISABLED))
            throw new IllegalStateException("계좌가 비활성화 상태입니다.");
    }

}
