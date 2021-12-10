package com.naver.line.demo.account;

import com.naver.line.demo.account.dto.AccountDto;
import com.naver.line.demo.account.dto.TransactionDto;
import com.naver.line.demo.account.entity.Account;
import com.naver.line.demo.account.entity.BalanceTransaction;
import com.naver.line.demo.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    /**
     * 1. 계좌 개설
     */
    @PostMapping
    public ApiUtils.ApiResult<AccountDto> createAccount(@RequestHeader("X-USER-ID") Integer userId, @RequestBody AccountDto dto) {
        Account account = accountService.create(userId, dto.getTransferLimit(), dto.getDailyTransferLimit());
        return ApiUtils.success(new AccountDto(account));
    }

    /**
     * 2. 계좌 비활성화
     */
    @DeleteMapping("/{id}")
    public ApiUtils.ApiResult<AccountDto> deactivate(@RequestHeader("X-USER-ID") Integer userId, @PathVariable Long id) {
        Account account = accountService.deactivate(userId, id);

        return ApiUtils.success(new AccountDto(account));
    }

    /**
     * 3. 계좌 이체 한도 수정
     */
    @PutMapping("/{id}/transfer-limit")
    public ApiUtils.ApiResult<AccountDto> update(@RequestHeader("X-USER-ID") Integer userId, @PathVariable Long id, @RequestBody AccountDto dto) {
        Account account = accountService.update(userId, id, dto.getTransferLimit(), dto.getDailyTransferLimit());

        return ApiUtils.success(new AccountDto(account));
    }


    /**
     * 5. 계좌 입출금 내역
     */
    @GetMapping("/{id}/transactions")
    public ApiUtils.ApiResult<List<TransactionDto>> get(@RequestHeader("X-USER-ID") Integer userId, @PathVariable Long id, @PageableDefault(value = 5) Pageable pageable
            , @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(name = "from_date") LocalDate from, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(name = "to_date") LocalDate to) {
        List<TransactionDto> transactionList = accountService.getTransactionList(userId, id, pageable, from, to).stream().map(TransactionDto::new).collect(Collectors.toList());
        return ApiUtils.success(transactionList);
    }
}
