package com.naver.line.demo.account;

import com.naver.line.demo.account.entity.Account;
import com.naver.line.demo.account.entity.BalanceTransaction;
import com.naver.line.demo.common.exceptions.ForbiddenException;
import com.naver.line.demo.common.exceptions.NotFoundException;
import com.naver.line.demo.user.UserService;
import com.naver.line.demo.user.entities.User;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Account create(Integer userId, Integer transferLimit, Integer dailyTransferLimit) {
        User user = userService.validateForCreateAccount(userId);

        String number = accountRepository.findFirstByOrderByIdDesc().getNumber();
        Long numberLong = Long.valueOf(number.replaceAll("-", "")) + 1;
        Account account = Account.builder().user(user).transferLimit(transferLimit).dailyTransferLimit(dailyTransferLimit)
                .number(numberLong).build();

        return account;
    }

    public Account deactivate(Integer userId, Long accountId) {
        Optional<Account> optional = accountRepository.findById(accountId);
        Account account = optional.get();
        if(!account.getUser().getId().equals(userId)) throw new ForbiddenException();

        account.deactivate();
        return account;
    }

    public Account update(Integer userId, Long accountId, Integer transferLimit, Integer dailyTransferLimit) {
        Optional<Account> optional = accountRepository.findById(accountId);
        Account account = optional.get();
        if(!account.getUser().getId().equals(userId)) throw new ForbiddenException();
        account.update(transferLimit, dailyTransferLimit);
        return account;
    }

    public List<BalanceTransaction> getTransactionList(Integer userId, Long accountId, Long page, Integer size, LocalDate from, LocalDate to) {
        return null;
    }
}
