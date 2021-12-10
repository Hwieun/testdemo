package com.naver.line.demo.account.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
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
    private LocalDate createdAt;

    public enum Type {
        WITHDRAW, DEPOSIT
    }

    public BalanceTransaction(Integer userId, Account account, Long amount, String note) {
        this.userId = userId;
        this.account = account;
        this.amount = amount;
        this.note = note;
    }

    public static BalanceTransaction withdraw(Integer userId, Account account, Long amount, String note) {
        BalanceTransaction balanceTransaction = new BalanceTransaction(userId, account, amount, note);
        balanceTransaction.beforeBalanceAmount = account.getAmount();
        balanceTransaction.type = Type.WITHDRAW;
        return balanceTransaction;
    }

    public static BalanceTransaction deposit(Integer userId, Account account, Long amount, String note) {
        BalanceTransaction balanceTransaction = new BalanceTransaction(userId, account, amount, note);
        balanceTransaction.beforeBalanceAmount = account.getAmount();
        balanceTransaction.type = Type.DEPOSIT;
        return balanceTransaction;

    }
}

