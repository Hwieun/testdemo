package com.naver.line.demo.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.naver.line.demo.account.entity.Account;
import com.naver.line.demo.user.entities.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.annotation.AnnotationBeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class AccountDto {

    private Long id;

    @JsonProperty(value = "user_id")
    private Integer userId;

    private String number;

    private Long amount;

    private Account.Status status;

    @JsonProperty(value = "transfer_limit")
    private Integer transferLimit;

    @JsonProperty(value = "daily_transfer_limit")
    private Integer dailyTransferLimit;

    @JsonProperty(value = "created_at")
    private LocalDateTime createdAt;

    @JsonProperty(value = "updated_at")
    private LocalDateTime updatedAt;

    public AccountDto(Account source) {
        copyProperties(source, this);
        userId = source.getUser().getId();
    }
}
