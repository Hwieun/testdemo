package com.naver.line.demo.transfer;

import com.naver.line.demo.account.AccountRepository;
import com.naver.line.demo.account.AccountService;
import com.naver.line.demo.account.entity.Account;
import com.naver.line.demo.account.entity.BalanceTransaction;
import com.naver.line.demo.common.exceptions.ForbiddenException;
import com.naver.line.demo.common.exceptions.NotFoundException;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransferServiceTest {

    @Resource
    AccountService transferService;
    @Resource
    AccountRepository accountRepository;

    @Test
    public void 성공() throws Exception {
        // given
        String sender = "123-12-12351";
        String receiver = "123-12-12346";
        String senderStr = "withdraw";
        Integer userId = 3;
        TransferDto transferDto = new TransferDto();
        transferDto.setSenderAccountNumber(sender);
        transferDto.setReceiverAccountNumber(receiver);
        transferDto.setAmount(10000L);
        transferDto.setSenderNote(senderStr);

        // when
        BalanceTransaction transfer = transferService.transfer(userId, transferDto);

        // then
        assertThat(transfer.getAccount().getId()).isEqualTo(11L);
        assertThat(transfer.getId()).isEqualTo(7);
        assertThat(transfer.getType()).isEqualTo(BalanceTransaction.Type.WITHDRAW);
        assertThat(transfer.getBeforeBalanceAmount()).isEqualTo(100000);
        assertThat(transfer.getNote()).isEqualTo(senderStr);
        assertThat(transfer.getAmount()).isEqualTo(10000);
        assertThat(transfer.getCreatedAt()).isEqualTo(LocalDate.now());

        Account account = accountRepository.findByNumber(receiver);
        assertThat(account.getAmount()).isEqualTo(10000);
        assertThat(account.getId()).isEqualTo(6);
        assertThat(account.getUser().getId()).isEqualTo(2);
    }

    @Test
    public void 데이터검증실패() throws Exception {
        // given
        String sender = "123-1212351";
        String receiver = "123-1212346";
        String senderStr = "데이터검증실패";
        Integer userId = 3;
        TransferDto transferDto = new TransferDto();
        transferDto.setSenderAccountNumber(sender);
        transferDto.setReceiverAccountNumber(receiver);
        transferDto.setAmount(1L);
        transferDto.setSenderNote(senderStr);
        // when
        assertThrows(IllegalArgumentException.class, () -> transferService.transfer(userId, transferDto));
    }

    @Test
    public void 둘중하나계좌없음() throws Exception {
        // given
        String sender = "123-12-12351";
        String receiver = "123-12-12353";
        String senderStr = "둘중하나계좌없음";
        Integer userId = 3;
        TransferDto transferDto = new TransferDto();
        transferDto.setSenderAccountNumber(sender);
        transferDto.setReceiverAccountNumber(receiver);
        transferDto.setAmount(10000L);
        transferDto.setSenderNote(senderStr);

        // when
        assertThrows(NotFoundException.class, () -> transferService.transfer(userId, transferDto));
    }

    @Test
    public void 출금계좌의소유자아님() throws Exception {
        // given
        String sender = "123-12-12351";
        String receiver = "123-12-12346";
        String senderStr = "출금계좌의소유자아님";
        Integer userId = 2;
        TransferDto transferDto = new TransferDto();
        transferDto.setSenderAccountNumber(sender);
        transferDto.setReceiverAccountNumber(receiver);
        transferDto.setAmount(10000L);
        transferDto.setSenderNote(senderStr);

        // when
        assertThrows(ForbiddenException.class, () -> transferService.transfer(userId, transferDto));
    }

    @Test
    public void 둘중하나비활성화() throws Exception {
        // given
        String sender = "123-12-12351";
        String receiver = "123-12-12345";
        String senderStr = "둘중하나비활성화";
        Integer userId = 3;
        TransferDto transferDto = new TransferDto();
        transferDto.setSenderAccountNumber(sender);
        transferDto.setReceiverAccountNumber(receiver);
        transferDto.setAmount(10000L);
        transferDto.setSenderNote(senderStr);

        // when
        assertThrows(IllegalStateException.class, () -> transferService.transfer(userId, transferDto));
    }

    @Test
    public void 출금계좌잔액부족() throws Exception {
        // given
        String sender = "123-12-12341";
        String receiver = "123-12-12346";
        String senderStr = "출금계좌잔액부족";
        Integer userId = 2;
        TransferDto transferDto = new TransferDto();
        transferDto.setSenderAccountNumber(sender);
        transferDto.setReceiverAccountNumber(receiver);
        transferDto.setAmount(10000L);
        transferDto.setSenderNote(senderStr);

        // when
        assertThrows(IllegalArgumentException.class, () -> transferService.transfer(userId, transferDto));
    }

    @Test
    public void 이체한도초과() throws Exception {
        // given
        String sender = "123-12-12351";
        String receiver = "123-12-12346";
        String senderStr = "이체한도초과";
        Integer userId = 3;
        TransferDto transferDto = new TransferDto();
        transferDto.setSenderAccountNumber(sender);
        transferDto.setReceiverAccountNumber(receiver);
        transferDto.setAmount(40000L);
        transferDto.setSenderNote(senderStr);

        // when
        assertThrows(IllegalArgumentException.class, () -> transferService.transfer(userId, transferDto));
    }

}