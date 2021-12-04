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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountServiceTest {
    @Resource
    AccountService accountService;

    @Test
    public void 계좌개셜_성공() throws Exception {
        // given
        Integer userId = 4;

        // when
        Account account = accountService.create(userId, 1000000, 3000000);

        // then
        assertThat(account.getAmount()).isEqualTo(0);
        assertThat(account.getUser().getId()).isEqualTo(userId);
        assertThat(account.getStatus()).isEqualTo(Account.Status.ENABLED);
        assertThat(account.getNumber()).isEqualTo("123-12-12353");
    }

    @Test
    public void 계좌개설_유저없음() throws Exception {
        // given

        // when
        // then
        assertThrows(UnauthorizedException.class, () -> accountService.create(0, 1000000, 3000000));
    }

    @Test
    public void 계좌개설_유저비활성화() throws Exception {
        // given
        Integer userId = 1;

        // when

        // then
        assertThrows(UnauthorizedException.class, () -> accountService.create(userId, 1000000, 3000000));
    }

    @Test
    public void 계좌개설_데이터검증실패() throws Exception {
        // given
        Integer userId = 4;

        // when

        // then
        assertThrows(IllegalStateException.class, () -> accountService.create(userId, 10000001, 3000000));
    }

    @Test
    public void 계좌개설_오늘한계() throws Exception {
        // given
        Integer userId = 2;

        // when

        // then
        assertThrows(IllegalStateException.class, () -> accountService.create(userId, 1000000, 3000000));
    }

    @Test
    public void 계좌개설_이미3개의계좌() throws Exception {
        // given
        Integer userId = 3;

        // when

        // then
        assertThrows(IllegalStateException.class, () -> accountService.create(userId, 1000000, 3000000));
    }

}