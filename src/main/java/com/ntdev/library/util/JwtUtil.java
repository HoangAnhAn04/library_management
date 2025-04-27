package com.ntdev.library.util;

import com.ntdev.library.config.JwtConfig;
import com.ntdev.library.enums.StatusCode;
import com.ntdev.library.exception.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.util.Arrays;
import java.util.Date;

@Component
public class JwtUtil {

    private final JwtConfig jwtConfig;

    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
    }

    public String generateToken(Long userId, Long universityId, String role) {
        return Jwts.builder()
                .subject(String.valueOf(universityId))
                .claim("userId", String.valueOf(userId))
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationMs()))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String extractUniversityId(String token) {
        try {
            return getClaims(token).getSubject();
        } catch (CustomException e) {
            return null;
        }
    }

    public String extractRole(String token) {
        try {
            return getClaims(token).get("role", String.class);
        } catch (CustomException e) {
            return null;
        }
    }

    public String extractUserId(String token) {
        try {
            return getClaims(token).get("userId", String.class);
        } catch (CustomException e) {
            return null;
        }
    }

    public boolean validateToken(String token, String universityId) {
        try {
            Claims claims = getClaims(token);
            return universityId.equals(claims.getSubject()) && !claims.getExpiration().before(new Date());
        } catch (CustomException e) {
            return false;
        }
    }

    public void setCookie(String token, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("session_token", token)
                .path("/")
                .httpOnly(true)
                // .secure(true)
                .sameSite("None")
                // .domain("library.thiendev.id.vn")
                .maxAge((int) (jwtConfig.getExpirationMs() / 1000))
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }

    public void deleteCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(jwtConfig.getCookieName(), "")
                .path("/")
                .httpOnly(true)
                // .secure(true) // phải true nếu ban đầu cookie secure
                // .domain(jwtConfig.getDomain()) // thêm domain
                .sameSite("None") // thêm SameSite nếu ban đầu có
                .maxAge(0) // 0 để trình duyệt xoá
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }

    public String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> jwtConfig.getCookieName().equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            throw new CustomException(StatusCode.TOKEN_INVALID_OR_EXPIRED);
        } catch (Exception e) {
            throw new CustomException(StatusCode.TOKEN_INVALID_OR_EXPIRED);
        }
    }
}
