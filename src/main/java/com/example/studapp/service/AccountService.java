package com.example.studapp.service;

import com.example.studapp.Dto.AccountDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface AccountService  {
    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccountbyid(Long id);

    AccountDto deposit(Long id,double amount,String timezone);

    AccountDto withdraw(Long id,double amount,String timezone);

    List<AccountDto> getallAccount();

    List<AccountDto> sortBy(String field);

    UserDetails loadUserByUsername(String username);

    Page<AccountDto> findnameWithPagination(int page, int pageSize);

    Page<AccountDto> findnameWithPaginationAndSorting(int page, int pageSize, String field, String sortDir);

    void deleteAccount(Long id);
}

