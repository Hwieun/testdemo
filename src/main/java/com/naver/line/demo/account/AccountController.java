package com.naver.line.demo.account;

import com.naver.line.demo.account.dto.AccountDto;
import com.naver.line.demo.account.entity.Account;
import com.naver.line.demo.utils.ApiUtils;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
