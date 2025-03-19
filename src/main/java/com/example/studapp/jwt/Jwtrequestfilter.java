package com.example.studapp.jwt;
import java.io.IOException;
import com.example.studapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This class filters incoming requests to validate JWT tokens and set authentication.
 * @author Manjari
 */
@Component
public class Jwtrequestfilter extends OncePerRequestFilter{

    @Autowired
    private Jwtcls jwtcls;

    @Autowired
    private AccountService userDetailsService;

    /**
     * This method checks every request for a JWT token in the "Authorization" header.
     * If the token is valid, it sets the user as authenticated in the system.
     * @author Manjari
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        final String authHeader = request.getHeader("Authorization");
        String username=null;
        String jwt=null;

        if(authHeader != null && authHeader.startsWith("Bearer "))
        {
            jwt = authHeader.substring(7);  // Extract the token
            username = jwtcls.extractUsername(jwt);
        }

        // Validate token and set authentication if user is not already authenticated
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the token
            if (jwtcls.validateToken(jwt, userDetails.getUsername())) {
                // Create an authentication token
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }

        }
        filterChain.doFilter(request, response);
    }

}