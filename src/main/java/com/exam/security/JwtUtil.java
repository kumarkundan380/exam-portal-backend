package com.exam.security;

import com.exam.exception.ExamPortalException;
import com.exam.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JwtUtil {

    @Value("${exam.portal.jwt.secret}")
    private String secretKey;

    @Value("${exam.portal.jwt.expiration.time}")
    private Long expirationTime;

    @Autowired
    private UserService userService;

    // Generate Token
    public String generateToken(String subject) {
        log.info("generateToken method invoking");
        return Jwts.builder()
                .setSubject(subject)
                .setIssuer("Exam-Portal")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expirationTime+ TimeUnit.MILLISECONDS.toMillis(expirationTime)))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }

    // Read Claims
    public Claims getClaims(String token) {
        log.info("getClaims method invoking");
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            log.error("Invalid JWT signature");
            throw new ExamPortalException("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
            throw new ExamPortalException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
            throw new ExamPortalException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
            throw new ExamPortalException("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid");
            throw new ExamPortalException("JWT token compact of handler are invalid");
        }
    }

    // Get Expire Date
    public Date getExpiryDate(String token) {
        return getClaims(token).getExpiration();
    }

    // Get UserName
    public String getUserNameFromJwtToken(String token) {
        return getClaims(token).getSubject();
    }

    // Validate Expiration Date
    public boolean isTokenExpire(String token) {
        return getExpiryDate(token).before(new Date(System.currentTimeMillis()));
    }

    // Validate UserName in token and database, Expiration Time
    public boolean validateToken(String token, String userName) {
        return (getUserNameFromJwtToken(token).equals(userService.getUserByUsername(userName).getUserName()) && !isTokenExpire(token));
    }
}
