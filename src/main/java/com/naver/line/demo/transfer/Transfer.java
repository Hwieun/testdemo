package com.naver.line.demo.transfer;

import com.naver.line.demo.account.entity.Account;
import com.naver.line.demo.account.entity.BalanceTransaction;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transfers")
@NoArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    //    @Column(name = "withdraw_id")
//    private Long withdrawId;
    @OneToOne
    @JoinColumn(name = "withdraw_id")
    private BalanceTransaction withdraw;

    //    @Column(name = "deposit_id")
//    private Long depositId;
    @OneToOne
    @JoinColumn(name = "deposit_id")
    private BalanceTransaction deposit;

    @Column
    private Long amount;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Transfer(Integer userId, BalanceTransaction withdraw, BalanceTransaction deposit, Long amount) {
        this.userId = userId;
        this.withdraw = withdraw;
        this.deposit = deposit;
        this.amount = amount;
    }
}
