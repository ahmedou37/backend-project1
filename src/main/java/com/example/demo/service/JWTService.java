package com.example.demo.service;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
    
    private String secretKey="sadfadsfaaaaaaaaaaasdfffffffffffffweeeeeeefjaaaaaasfdjjjjkkm";

    public String generateToken(String username   , Collection<? extends GrantedAuthority> authorities) {
    Map<String, Object> claims = new HashMap<>();


    // Extract role names from authorities and add to claims
        List<String> roles = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        
        claims.put("roles", roles);


    return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date(System.currentTimeMillis()))//Stores the exact time the token was created.
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) //After this date, the token is considered invalid → 401 Unauthorized
            .signWith(getKey())
            .compact();
    }


    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject/*claims -> claims.getSubject()*/);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    






    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        
        @SuppressWarnings("unchecked")
        List<String> roles = claims.get("roles", List.class);
        
        if (roles == null) {
            return new ArrayList<>();
        }
        
        return roles;
    }

    public List<GrantedAuthority> extractAuthorities(String token) {
        List<String> roles = extractRoles(token);
        
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role))
            .collect(Collectors.toList());
    }
}