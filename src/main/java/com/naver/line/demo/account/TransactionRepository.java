package com.naver.line.demo.account;

import com.naver.line.demo.account.entity.BalanceTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<BalanceTransaction, Long> {

    @Query("select t from BalanceTransaction t where t.account.id = ?1 and t.createdAt >= ?2 and t.createdAt < ?3 order by id desc")
    List<BalanceTransaction> findByCreatedAtAndAccountId(Long accountId, LocalDate from, LocalDate to, Pageable pageable);

    @Query("select sum(t.amount) from BalanceTransaction t where t.account.id = ?1 and t.createdAt >= CURRENT_DATE and t.createdAt < CURRENT_DATE + 1")
    Long sumAmountByCreatedAtAndAccountId(Long accountId);
}
