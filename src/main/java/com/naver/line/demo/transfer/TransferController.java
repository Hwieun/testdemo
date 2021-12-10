package com.naver.line.demo.transfer;

import com.naver.line.demo.account.AccountService;
import com.naver.line.demo.account.dto.TransactionDto;
import com.naver.line.demo.account.entity.BalanceTransaction;
import com.naver.line.demo.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    @Autowired
    AccountService accountService;

    /**
     * 4. 이체
     */
    @PostMapping("/withdraw")
    public ApiUtils.ApiResult<TransactionDto> transfer(@RequestHeader("X-USER-ID") Integer userId, @RequestBody TransferDto dto) {
        return ApiUtils.success(new TransactionDto(accountService.transfer(userId, dto)));
    }

}
