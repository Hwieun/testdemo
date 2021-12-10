package com.naver.line.demo.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.naver.line.demo.account.entity.Account;
import com.naver.line.demo.account.entity.BalanceTransaction;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class TransactionDto {
    private Long id;
    @JsonProperty("account_id")
    private Long accountId;
    private String type;
    private Long amount;
    @JsonProperty("before_balance_amount")
    private Long beforeBalanceAmount;
    private String note;
    @JsonProperty("created_at")
    private LocalDate createdAt;

    public TransactionDto(BalanceTransaction source) {
        BeanUtils.copyProperties(source, this);
        accountId = source.getAccount().getId();
        type = source.getType().toString();
    }
}
