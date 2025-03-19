package com.example.studapp.controller;

import com.example.studapp.jwt.Jwtcls;
import com.example.studapp.Model.Authenticaterequest;
import com.example.studapp.repository.Authenticateresponse;
import com.example.studapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Authenticationcontroller {

    @Autowired
    private Jwtcls jwtcls;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountService accountService;  // ✅ Use Interface Instead of Implementation

    // Authenticate user
    @PostMapping("/authenticate")
    public ResponseEntity<Authenticateresponse> authenticate(@RequestBody Authenticaterequest authenticaterequest) {

        try {
            // Authenticate user credentials
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticaterequest.getUsername(), authenticaterequest.getPassword())
            );

            // Load user details
            final UserDetails userDetails = accountService.loadUserByUsername(authenticaterequest.getUsername());
            String jwt = jwtcls.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new Authenticateresponse(jwt, null));

        } catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Authenticateresponse(null, "Login failed: Invalid credentials"));
        }
    }
}
