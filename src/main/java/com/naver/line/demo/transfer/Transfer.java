package com.naver.line.demo.transfer;

import com.naver.line.demo.account.entity.Account;
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

    @Column(name = "withdraw_id")
    private Long withdrawId;

    @Column(name = "deposit_id")
    private Long depositIt;

    @Column
    private Long amount;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

}
