package com.example.studapp.jwt;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


/**
 * @author Manjari
 * @description This class handles JWT generation and validation
 */

@Component
public class  Jwtcls
{
    private static final String secretKey="2hfiKJ28d8+9dklh2h5Hhr0GJZl4jklHpT8=";
    /**
     * Generates a JWT token for the given username.
     * @param username The username for token is generated.
     * @return A JWT token as a String.
     * @author Manjari
     */
    public String generateToken(String username)
    {

        JwtBuilder jwt= Jwts.builder()
                .subject(username)// Setting subject (username)
                .issuedAt(new Date()) // Token issued time
                .expiration(new Date(System.currentTimeMillis()+1000*60*10))// Token expiry (20 mins)
                .signWith(SignatureAlgorithm.HS256,generateJwtSecretKey());// Signing with the secret key
        return jwt.compact();// Converts the JWT into URL string

    }
    public SecretKey generateJwtSecretKey()
    {
        // Convert the static word to a byte array
        byte[] keyBytes = secretKey.getBytes();

        // Ensure the key length is compatible with the algorithm
        byte[] keyBytesPadded = new byte[32];
        System.arraycopy(keyBytes, 0, keyBytesPadded, 0, Math.min(keyBytes.length, 32));

        // Generate the SecretKey using the static word
        return Keys.hmacShaKeyFor(keyBytesPadded);
    }

    /**
     * Validates the token by checking if the username matches and the token is not expired.
     * @param token The JWT token to validate.
     * @return True if the token is valid, otherwise false.
     * @author Manjari
     */
    public boolean validateToken(String token,String username)
    {
        return (username.equals(getUsername(token)) && !isTokenExpired(token));
    }
    /**
     * Extracts the role of the user from the JWT token.
     * @param token The JWT token.
     * @return The role as a String.
     * @author Manjari
     */
    public String extractRole(String token)
    {
        return (String) getClaims(token).get("role"); // Extract role from token
    }

    /**
     * Checks if the token has expired.
     * @param token The JWT token.
     * @return True if the token has expired, otherwise false.
     * @author Manjari
     */
    private boolean isTokenExpired(String token)
    {
        return getClaims(token).getExpiration().before(new Date());
    }

    /**
     * Retrieves the username from the JWT token.
     * @param token The JWT token.
     * @return The username as a String.
     * @author Manjari
     */
    private String getUsername(String token)

    {
        return getClaims(token).getSubject();
    }

    /**
     * Extracts the username from the token (alternative method).
     * @param token The JWT token.
     * @return The extracted username.
     * @author Manjari
     */
    public String extractUsername(String token)
    {
        return getClaims(token).getSubject();
    }

    /**
     * Parses the token and retrieves all claims.
     * @param token The JWT token.
     * @return The claims contained in the token.
     * @author Manjari
     */
    public Claims getClaims(String token)
    {
        return Jwts.parser().verifyWith(generateJwtSecretKey()).build()
                .parseSignedClaims(token).getPayload();
    }
}
