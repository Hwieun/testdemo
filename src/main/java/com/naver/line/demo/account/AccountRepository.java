package com.naver.line.demo.account;

import com.naver.line.demo.account.entity.Account;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

//    @Query("select a.number from Account a order by a.number desc limit 1")
    public Account findFirstByOrderByIdDesc();

    public Integer findCountByUserId(Integer userId);
}
