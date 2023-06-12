package com.bagrov.springpmhw.videorent.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JWTTokenUtil {

    public static final long JWT_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000;
    public final String secret = "anyWord";
    private final ObjectMapper objectMapper;

    public JWTTokenUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String generateToken(UserDetails payload) {
        return Jwts.builder()
                .setSubject(payload.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        return getStringValueFromTokenByKey(token, "username");
    }

    public String getRoleFromToken(String token) {
        return getStringValueFromTokenByKey(token, "user_role");
    }

    private boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private String getStringValueFromTokenByKey(String token, String key) {
        String claim = getClaimsFromToken(token, Claims::getSubject);
        JsonNode claimJSON = null;
        try {
            claimJSON = objectMapper.readTree(claim);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (claimJSON != null) {
            return claimJSON.get(key).asText();
        } else {
            return null;
        }
    }
}
