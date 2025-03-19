package com.example.studapp.controller;
import com.example.studapp.Dto.AccountDto;
import com.example.studapp.Model.Depositrequest;
import com.example.studapp.Model.PaginationRequest;
import com.example.studapp.Model.Sortrequest;
import com.example.studapp.Model.Withdrawrequest;
import com.example.studapp.jwt.Jwtcls;
import com.example.studapp.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Account Controller - Manages user account-related operations
 * @author Manjari
 */
@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/api/user")
public class Accountcontroller {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Jwtcls jwtcls;
    /**
     * Constructor for dependency injection
     */
    @Autowired
    public  Accountcontroller(AccountService accountService) {
        this.accountService = accountService;
    }


    /**
     * Create a new user account (Admin only)
     * @param accountDto - Account details
     * @param token - Authorization token
     * @return Created account details
     * @author Manjari
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto,@RequestHeader("Authorization") String token)
    {
        String adminUsername = jwtcls.extractUsername(token.substring(7));
        accountDto.setCreatedBy(adminUsername);
        log.info("Received request to create account: {}", accountDto.getUsername());
       accountDto.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        log.info("Account created successfully with ID: {}", accountDto.getId());
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    /**
     * Fetch account details by ID (Admin only)
     * @param requestBody - Contains account ID
     * @return Account details
     * @author Manjari
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/id")
    public ResponseEntity<AccountDto>  getAccountbyid(@RequestBody Map<String, Long> requestBody)
    {
        Long id = requestBody.get("id");
        log.info("Fetching account details for ID: {}", id);
        AccountDto accountDto = accountService.getAccountbyid(id);
        return ResponseEntity.ok(accountDto);
    }


    /**
     * Deposit money into an account (User only)
     * @param request - Deposit request
     * @return Updated account details
     * @author Manjari
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/deposit")
    public ResponseEntity<AccountDto> deposit(@RequestBody Depositrequest request)
    {
        Long id = Long.valueOf(request.getId());
        Double amount = Double.valueOf(request.getAmount());
        String timezone=request.getTimezone();
        log.info("Depositing {} to account ID: {}", amount, id);
        AccountDto accountDto = accountService.deposit(id, amount, timezone);
        return ResponseEntity.ok(accountDto);
    }

    /**
     * Withdraw money from an account (User only)
     * @param request - Withdrawal request
     * @return Updated account details
     * @author Manjari
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/withdraw")
    public ResponseEntity<AccountDto> withdraw(@RequestBody Withdrawrequest request)
    {
        Long id = Long.valueOf(request.getId());
        Double amount = Double.valueOf(request.getAmount());
        String timezone=request.getTimezone();
        log.info("Withdrawing {} from account ID: {}", amount, id);
        AccountDto accountDto = accountService.withdraw(id, amount,timezone);
        return ResponseEntity.ok(accountDto);
    }

    /**
     * Sort accounts based on a specific field (User/Admin)
     * @return Sorted list of accounts
     * @author Manjari
     */
    @PreAuthorize("haRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<AccountDto>> getallAccount()
    {
        List<AccountDto> accountDto = accountService.getallAccount();
        return ResponseEntity.ok(accountDto);
    }

    /**
     * Sort accounts based on a specific field (User/Admin)
     * @param sortrequest - Field to sort by
     * @return Sorted list of accounts
     * @author Manjari
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/sort")
    public ResponseEntity<List<AccountDto>> sortBy(@RequestBody Sortrequest sortrequest )
    {
        return ResponseEntity.ok( accountService.sortBy(sortrequest.getField()));
    }

    /**
     * Get paginated list of accounts (User/Admin)
     * @param request - Pagination details
     * @return Paginated accounts
     * @author Manjari
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<Page<AccountDto>> findEmployeeWithPagination(@RequestBody PaginationRequest request)
    {
        return ResponseEntity.ok(accountService.findnameWithPagination(request.getOffset(), request.getPageSize()));
    }

    /**
     * Get paginated & sorted accounts (User/Admin)
     * @param paginationRequest - Pagination & sorting details
     * @return Paginated & sorted accounts
     * @author Manjari
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/paginationSorting")
    public ResponseEntity<Page<AccountDto>> findnameWithPaginationAndSorting(
            @RequestBody PaginationRequest paginationRequest)
    {
        log.info("Fetching paginated and sorted accounts - Page: {}, PageSize: {}, SortBy: {}, Order: {}",
        paginationRequest.getOffset(), paginationRequest.getPageSize(),
        paginationRequest.getField(), paginationRequest.getSortDir());
        return ResponseEntity.ok(accountService.findnameWithPaginationAndSorting(
                paginationRequest.getOffset(),
                paginationRequest.getPageSize(),
                paginationRequest.getField(),
                paginationRequest.getSortDir()));
    }

    /**
     * Delete an account by ID (Admin only)
     * @param requestBody - Contains account ID
     * @return Success message
     * @author Manjari
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/id")
    public ResponseEntity<String> deleteAccount(@RequestBody Map<String, Long> requestBody) {
        Long id = requestBody.get("id");
        log.info("Deleting account ID: {}", id);
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account is deleted");
    }
}