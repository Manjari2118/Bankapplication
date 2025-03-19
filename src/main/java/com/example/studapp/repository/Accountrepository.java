package com.example.studapp.repository;

import com.example.studapp.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Accountrepository extends JpaRepository<Account,Long> {

    Account findByUsername(String username);
}
