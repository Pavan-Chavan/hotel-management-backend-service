package com.teams.security.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.teams.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${teams.app.jwtSecret}")
    private String jwtSecret;

    @Value("${teams.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private static List<String> allowedUrls = Arrays.asList(
           "/api/auth/","/swagger-ui/","/swagger-ui/","/swagger-resources",
            "/swagger-resources/","/v2/api-docs","/swagger-ui.html/");

//     "/swagger-ui/","/swagger-ui/","/swagger-resources",
//             "/swagger-resources/","/v2/api-docs","/swagger-ui.html/",

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) throws Exception {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            throw new MalformedJwtException("Your JWT token is invalid, Please log in again !");
        } catch (ExpiredJwtException e) {
            logger.error("Your session is expired: {}", e.getMessage());
            throw new Exception("Your session is expired, Please log in again");
        } catch (UnsupportedJwtException e) {
            logger.error("Your session is unsupported: {}", e.getMessage());
            throw new UnsupportedJwtException("Your session is unsupported, Please log in again");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            throw new IllegalArgumentException("Your session is empty, Please login again");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("Something went wrong, Please logout and login again !");
        }
    }

    public static boolean isAllowedUrl(HttpServletRequest request) {
        if (allowedUrls.stream().anyMatch(request.getRequestURI()::contains)) {
            return true;
        }
        return false;
    }
}
