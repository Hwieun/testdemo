package com.naver.line.demo.account;

import com.naver.line.demo.account.entity.BalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<BalanceTransaction, Long> {

}
