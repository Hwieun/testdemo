package com.naver.line.demo.account;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountRepositoryTest {
    @Resource
    AccountRepository accountRepository;

    @Test
    public void find() throws Exception {
        // given

        // when
        String number = accountRepository.findFirstByOrderByIdDesc().getNumber();

        // then
        System.out.println("number = " + number);
    }

}