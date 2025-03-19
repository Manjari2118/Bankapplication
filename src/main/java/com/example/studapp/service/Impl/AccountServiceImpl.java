package com.example.studapp.service.Impl;
import com.example.studapp.Dto.AccountDto;
import com.example.studapp.Model.Account;
import com.example.studapp.repository.Accountrepository;
import com.example.studapp.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @description Implementation of AccountService with Spring Security authentication
 * @author Manjari
 */

@Service
public class AccountServiceImpl implements AccountService, UserDetailsService {

    @Autowired
    private ModelMapper modelMapper;

    private Accountrepository accountrepository;

    // Constructor-based Dependency Injection
    public AccountServiceImpl(Accountrepository accountrepository) {

        this.accountrepository = accountrepository;
    }

    /**
     * Creates a new user account.
     * @param accountDto DTO containing account details.
     * @return AccountDto with saved account details.
     * @author Manjari
     */
    public AccountDto createAccount(AccountDto accountDto)
    {
        Account account = modelMapper.map(accountDto, Account.class);// Convert DTO to Entity
        Account savedAccount = accountrepository.save(account);
        return modelMapper.map(savedAccount, AccountDto.class);  // Convert Entity to DTO
    }

    /**
     * Fetches account details by ID.
     * @param id Account ID.
     * @return AccountDto containing account details.
     * @author Manjari
     */
    public AccountDto getAccountbyid(Long id)
    {
        Account account =accountrepository
        .findById(id)
        .orElseThrow(()->new RuntimeException("Account not exists"));
        return modelMapper.map(account, AccountDto.class);
    }

    /**
     * Deposits a given amount into the account and updates the timestamp.
     * @param id Account ID.
     * @param amount Amount to deposit.
     * @return Updated AccountDto after deposit.
     * @author Manjari
     */
    public AccountDto deposit(Long id, double amount,String timezone) {
        Account account = accountrepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account not exists"));

        double total = account.getBalance() + amount;
        account.setBalance(total);

        Account savedAccount = accountrepository.save(account);

        // Convert UTC to local time before returning
        AccountDto accountDto = modelMapper.map(savedAccount, AccountDto.class);
        accountDto.setCreatedAt(savedAccount.getCreatedAt(), ZoneId.of("Asia/Kolkata"));
        accountDto.setUpdatedAt(savedAccount.getUpdatedAt(), ZoneId.of("Asia/Kolkata"));

        return accountDto;
    }

    /**
     * Withdraws a given amount from a user account.
     * @param id Account ID.
     * @param amount Amount to withdraw.
     * @return Updated AccountDto after withdrawal.
     * @throws RuntimeException if the balance is insufficient.
     * @author Manjari
     */
    public AccountDto withdraw(Long id, double amount,String timezone) {

        Account account = accountrepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account not exists"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient amount");
        }

        double total = account.getBalance() - amount;
        account.setBalance(total);

        Account savedAccount = accountrepository.save(account);

        // Convert UTC to local time before returning
        AccountDto accountDto = modelMapper.map(savedAccount, AccountDto.class);
        accountDto.setCreatedAt(savedAccount.getCreatedAt(), ZoneId.of("Asia/Kolkata"));
        accountDto.setUpdatedAt(savedAccount.getUpdatedAt(), ZoneId.of("Asia/Kolkata"));
        return accountDto;
    }

    /**
     * Fetches all accounts.
     * @return List of AccountDto objects containing all accounts.
     * @author Manjari
     */
    public List<AccountDto> getallAccount()
    {
        List<Account> accounts = accountrepository.findAll();
        return accounts.stream()
                .map(account -> modelMapper.map(account, AccountDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Sorts accounts by the specified field.
     * @param field The field to sort by.
     * @return List of sorted AccountDto objects.
     * @author Manjari
     */
    public List<AccountDto> sortBy(String field)
    {
        return accountrepository.findAll(Sort.by(Sort.Direction.ASC, field)).stream()
                .map(emp -> modelMapper.map(emp, AccountDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Fetches accounts with pagination.
     * @param offset Page number.
     * @param pageSize Number of records per page.
     * @return Paginated accounts.
     * @author Manjari
     */
    public Page<AccountDto> findnameWithPagination(int offset, int pageSize)
    {
        Pageable pageable = PageRequest.of(offset, pageSize);
        return accountrepository.findAll(pageable).map(emp -> modelMapper.map(emp, AccountDto.class));
    }

    /**
     * Fetches accounts with pagination and sorting.
     * @param offset Page number.
     * @param pageSize Number of records per page.
     * @param field The field to sort by.
     * @param sortDir Sort direction (asc/desc).
     * @return Paginated and sorted accounts.
     * @author Manjari
     */
    public Page<AccountDto> findnameWithPaginationAndSorting(int offset, int pageSize, String field, String sortDir)
    {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(field).ascending() : Sort.by(field).descending();
        Pageable pageable = PageRequest.of(offset, pageSize, sort);
        return accountrepository.findAll(pageable)
                .map(emp -> modelMapper.map(emp, AccountDto.class));
    }

    /**
     * Loads user details for authentication.
     * @param username The username of the account.
     * @return UserDetails containing user authentication info.
     * @throws UsernameNotFoundException if the user is not found.
     * @author Manjari
     */
    @Override
    public  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Account account = accountrepository.findByUsername(username);
              if(account==null){
                  throw new UsernameNotFoundException("User not found");
              }
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        return new User(
                account.getUsername(),
                account.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(account.getRole()))
        );
    }

    /**
     * Deletes an account by ID.
     * @param id Account ID.
     * @author Manjari
     */
    public void deleteAccount(Long id)
    {
        Account account =accountrepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Account not exists"));
        accountrepository.deleteById(id);
    }
}
