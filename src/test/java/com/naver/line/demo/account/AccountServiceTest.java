package com.naver.line.demo.account;

import com.naver.line.demo.account.entity.Account;
import com.naver.line.demo.account.entity.BalanceTransaction;
import com.naver.line.demo.common.exceptions.ForbiddenException;
import com.naver.line.demo.common.exceptions.NotFoundException;
import com.naver.line.demo.common.exceptions.UnauthorizedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountServiceTest {
    @Resource
    AccountService accountService;

    @Nested
    @DisplayName("계좌 이체 한도 수정")
    public class Update {
        @Test
        public void 성공() throws Exception {
            // given
            Integer userId = 2;
            Long accountId = 6L;
            int transferLimit = 1000000;
            int dailyTransferLimit = 3000000;

            // when
            Account account = accountService.update(userId, accountId, transferLimit, dailyTransferLimit);

            // then
            assertThat(account.getAmount()).isEqualTo(0);
            assertThat(account.getUser().getId()).isEqualTo(userId);
            assertThat(account.getStatus()).isEqualTo(Account.Status.ENABLED);
            assertThat(account.getNumber()).isEqualTo("123-12-12346");
            assertThat(account.getTransferLimit()).isEqualTo(transferLimit);
            assertThat(account.getDailyTransferLimit()).isEqualTo(dailyTransferLimit);
        }

        @Test
        public void 계좌없음() throws Exception {
            // given
            Integer userId = 2;
            Long accountId = 13L;
            int transferLimit = 1000000;
            int dailyTransferLimit = 3000000;

            // when
            assertThrows(NoSuchElementException.class, () -> accountService.update(userId, accountId, transferLimit, dailyTransferLimit));

            // then
        }

        @Test
        public void 소유자가아님() throws Exception {
            // given
            Integer userId = 4;
            Long accountId = 6L;
            int transferLimit = 1000000;
            int dailyTransferLimit = 3000000;

            // when
            // then
            assertThrows(ForbiddenException.class, () -> accountService.update(userId, accountId, transferLimit, dailyTransferLimit));
        }

        @Test
        public void 계좌비활성화() throws Exception {
            // given
            Integer userId = 2;
            Long accountId = 5L;
            int transferLimit = 1000000;
            int dailyTransferLimit = 3000000;

            // when
            // then
            assertThrows(IllegalStateException.class, () -> accountService.update(userId, accountId, transferLimit, dailyTransferLimit));
        }

        @Test
        public void 데이터검증실패() throws Exception {
            // given
            Integer userId = 2;
            Long accountId = 5L;
            int transferLimit = 5000001;
            int dailyTransferLimit = 3000000;

            // when
            // then
            assertThrows(IllegalStateException.class, () -> accountService.update(userId, accountId, transferLimit, dailyTransferLimit));
        }
    }

    @Nested
    @DisplayName("계좌 개셜")
    public class Create {
        @Test
        public void 성공() throws Exception {
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
        public void 유저없음() throws Exception {
            // given

            // when
            // then
            assertThrows(UnauthorizedException.class, () -> accountService.create(0, 1000000, 3000000));
        }

        @Test
        public void 유저비활성화() throws Exception {
            // given
            Integer userId = 1;

            // when

            // then
            assertThrows(UnauthorizedException.class, () -> accountService.create(userId, 1000000, 3000000));
        }

        @Test
        public void 데이터검증실패() throws Exception {
            // given
            Integer userId = 4;

            // when

            // then
            assertThrows(IllegalStateException.class, () -> accountService.create(userId, 10000001, 3000000));
        }

        @Test
        public void 오늘한계() throws Exception {
            // given
            Integer userId = 2;

            // when

            // then
            assertThrows(IllegalStateException.class, () -> accountService.create(userId, 1000000, 3000000));
        }

        @Test
        public void 이미3개의계좌() throws Exception {
            // given
            Integer userId = 3;

            // when

            // then
            assertThrows(IllegalStateException.class, () -> accountService.create(userId, 1000000, 3000000));
        }
    }

    @Nested
    @DisplayName("거래 내역 조회")
    public class Get {
        @Test
        public void 성공() throws Exception {
            // given
            Integer userId = 2;
            Long accountId = 8L;
            Long page = 0L;
            Integer size = 5;
            LocalDate from = LocalDate.now().minusDays(1);
            LocalDate to = LocalDate.now().plusDays(1);
            PageRequest of = PageRequest.of(Math.toIntExact(page), size);
            // when
//            List<BalanceTransaction> transactionList = accountService.getTransactionList(userId, accountId, page, size, from, to);
            List<BalanceTransaction> transactionList = accountService.getTransactionList(userId, accountId, of, from, to);

            // then
            assertNotNull(transactionList);
            assertThat(transactionList.size()).isEqualTo(2);

            BalanceTransaction ts = transactionList.get(1);
            assertThat(ts.getAccount().getId()).isEqualTo(accountId);
            assertThat(ts.getId()).isEqualTo(2);
            assertThat(ts.getType()).isEqualTo(BalanceTransaction.Type.WITHDRAW);
            assertThat(ts.getBeforeBalanceAmount()).isEqualTo(30000);
            assertThat(ts.getNote()).isEqualTo("withdraw");
            assertThat(ts.getAmount()).isEqualTo(5000);
            assertThat(ts.getCreatedAt()).isEqualTo(from);
        }

        @Test
        public void 계좌없음() throws Exception {
            // given
            Integer userId = 2;
            Long accountId = 13L;
            Long page = 0L;
            Integer size = 5;
            LocalDate from = LocalDate.now().minusDays(1);
            LocalDate to = LocalDate.now().plusDays(1);
            PageRequest of = PageRequest.of(Math.toIntExact(page), size);
            // when
            // then
            assertThrows(NoSuchElementException.class, () -> accountService.getTransactionList(userId, accountId, of, from, to));
        }

        @Test
        public void 소유자아님() throws Exception {
            // given
            Integer userId = 3;
            Long accountId = 8L;
            Long page = 0L;
            Integer size = 5;
            LocalDate from = LocalDate.now().minusDays(1);
            LocalDate to = LocalDate.now().plusDays(1);
            PageRequest of = PageRequest.of(Math.toIntExact(page), size);

            // when
            // then
            assertThrows(ForbiddenException.class, () -> accountService.getTransactionList(userId, accountId, of, from, to));
        }

        @Test
        public void 비활성화() throws Exception {
            // given
            Integer userId = 2;
            Long accountId = 5L;
            Long page = 0L;
            Integer size = 5;
            LocalDate from = LocalDate.now().minusDays(1);
            LocalDate to = LocalDate.now().plusDays(1);
            PageRequest of = PageRequest.of(Math.toIntExact(page), size);

            // when
            // then
            assertThrows(IllegalStateException.class, () -> accountService.getTransactionList(userId, accountId, of, from, to));
        }

        @Test
        public void 데이터유효하지않음_성공() throws Exception {
            // given
            Integer userId = 2;
            Long accountId = 8L;
            Long page = -1L;
            Integer size = 6;
            LocalDate from = LocalDate.now();
            LocalDate to = LocalDate.now().minusDays(1);
            PageRequest of = PageRequest.of(Math.toIntExact(page), size);

            List<BalanceTransaction> transactionList = accountService.getTransactionList(userId, accountId, of, from, to);

            // then
            assertNotNull(transactionList);
            assertThat(transactionList.size()).isEqualTo(1);
            BalanceTransaction ts = transactionList.get(0);
            assertThat(ts.getAccount().getId()).isEqualTo(accountId);
            assertThat(ts.getId()).isEqualTo(1);
            assertThat(ts.getType()).isEqualTo(BalanceTransaction.Type.WITHDRAW);
            assertThat(ts.getBeforeBalanceAmount()).isEqualTo(25000);
            assertThat(ts.getNote()).isEqualTo("withdraw");
            assertThat(ts.getAmount()).isEqualTo(5000);
            assertThat(ts.getCreatedAt()).isEqualTo(from);
        }

    }
}