package com.naver.line.demo.account.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "balance_transactions")
@NoArgsConstructor
public class BalanceTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated
    @Column
    private Type type;

    @Column
    private Long amount;

    @Column(name = "before_balance_amount")
    private Long beforeBalanceAmount;

    @Column
    private String note;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum Type {
        WITHDRAW, DEPOSIT
    }
}

