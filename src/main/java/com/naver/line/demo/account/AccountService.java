package com.naver.line.demo.account;

import com.naver.line.demo.account.entity.Account;
import com.naver.line.demo.account.entity.BalanceTransaction;
import com.naver.line.demo.common.exceptions.ForbiddenException;
import com.naver.line.demo.common.exceptions.NotFoundException;
import com.naver.line.demo.transfer.Transfer;
import com.naver.line.demo.transfer.TransferDto;
import com.naver.line.demo.transfer.TransferRepository;
import com.naver.line.demo.user.UserService;
import com.naver.line.demo.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserService userService;
    @Autowired
    TransferRepository transferRepository;

    public Account create(Integer userId, Integer transferLimit, Integer dailyTransferLimit) {
        User user = userService.validateForCreateAccount(userId);

        String number = accountRepository.findFirstByOrderByIdDesc().getNumber();
        Long numberLong = Long.parseLong(number.replaceAll("-", "")) + 1;

        Account account = Account.builder().user(user).transferLimit(transferLimit).dailyTransferLimit(dailyTransferLimit)
                .number(numberLong).build();
        return accountRepository.save(account);
    }

    public Account deactivate(Integer userId, Long accountId) {
        Account account = getAccount(userId, accountId);
        account.deactivate();
        return account;
    }

    public Account update(Integer userId, Long accountId, Integer transferLimit, Integer dailyTransferLimit) {
        Account account = getAccount(userId, accountId);
        account.update(transferLimit, dailyTransferLimit);
        return account;
    }

    public List<BalanceTransaction> getTransactionList(Integer userId, Long accountId, Pageable pageable, LocalDate from, LocalDate to) {
        Account account = getAccount(userId, accountId);
        if(account.getStatus().equals(Account.Status.DISABLED))
            throw new IllegalStateException("계좌가 비활성화 상태입니다.");
        if (to.isBefore(from)) to = LocalDate.now();

        to = to.plusDays(1);

        return transactionRepository.findByCreatedAtAndAccountId(accountId, from, to, pageable);
    }

    public Account getAccount(Integer userId, Long accountId) {
        Optional<Account> optional = accountRepository.findById(accountId);
        Account account = optional.get();
        if (!account.getUser().getId().equals(userId)) throw new ForbiddenException("소유자가 아닙니다.");
        return account;
    }

    public BalanceTransaction transfer(Integer userId, TransferDto transferDto) {
        transferDto.validate();

        Account sender = accountRepository.findByNumber(transferDto.getSenderAccountNumber());
        Account receiver = accountRepository.findByNumber(transferDto.getReceiverAccountNumber());
        if(sender == null || receiver == null) throw new NotFoundException("계좌가 존재하지 않습니다.");

        if (!sender.getUser().getId().equals(userId)) throw new ForbiddenException("소유자가 아닙니다.");
        sender.validateStatus();
        receiver.validateStatus();

        Long senderDailyTransfer = Optional.ofNullable(transactionRepository.sumAmountByCreatedAtAndAccountId(sender.getId())).orElse(0L);
        Long receiverDailyTransfer = Optional.ofNullable(transactionRepository.sumAmountByCreatedAtAndAccountId(receiver.getId())).orElse(0L);

        BalanceTransaction withdraw = transactionRepository.save(
                BalanceTransaction.withdraw(userId, sender, transferDto.getAmount(), transferDto.getSenderNote() != null ? transferDto.getSenderNote() : receiver.getUser().getName())
        );
        BalanceTransaction deposit = transactionRepository.save(
                BalanceTransaction.deposit(userId, receiver, transferDto.getAmount(), transferDto.getReceiverNote() != null ? transferDto.getReceiverNote() : sender.getUser().getName())
        );

        sender.withdraw(senderDailyTransfer, transferDto.getAmount());
        receiver.deposit(receiverDailyTransfer, transferDto.getAmount());

        Transfer transfer = new Transfer(userId, withdraw, deposit, transferDto.getAmount());
        transferRepository.save(transfer);

        return withdraw;
    }

}

