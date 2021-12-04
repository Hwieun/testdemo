package com.naver.line.demo.account;

import com.naver.line.demo.account.entity.Account;
import com.naver.line.demo.common.exceptions.ForbiddenException;
import com.naver.line.demo.common.exceptions.NotFoundException;
import com.naver.line.demo.common.exceptions.UnauthorizedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountDeactivate {
    @Resource
    AccountService accountService;

    @Test
    public void 계좌_비활성화() throws Exception {
        // given
        Long accountId = 2L;
        Integer userId = 3;

        // when
        Account account = accountService.deactivate(userId, accountId);

        // then
        assertThat(account.getStatus()).isEqualTo(Account.Status.DISABLED);
    }

    @Test
    public void 계좌비활성화_없는계좌() throws Exception {
        // given
        Long accountId = 13L;
        Integer userId = 3;

        // when

        // then
        assertThrows(NoSuchElementException.class, () -> accountService.deactivate(userId, accountId));
    }


    @Test
    public void 계좌비활성화_소유자가아닌경우() throws Exception {
        // given
        Long accountId = 2L;
        Integer userId = 2;

        // when

        // then
        assertThrows(ForbiddenException.class, () -> accountService.deactivate(userId, accountId));
    }

    @Test
    public void 계좌비활성화_이미비활성화() throws Exception {
        // given
        Long accountId = 5L;
        Integer userId = 2;

        // when

        // then
        assertThrows(IllegalStateException.class, () -> accountService.deactivate(userId, accountId));
    }

    @Test
    public void 계좌비활성화_잔액이0이아닌경우() throws Exception {
        // given
        Long accountId = 1L;
        Integer userId = 2;

        // when

        // then
        assertThrows(IllegalStateException.class, () -> accountService.deactivate(userId, accountId));
    }

}