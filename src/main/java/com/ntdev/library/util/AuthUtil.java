package com.ntdev.library.util;

import com.ntdev.library.service.RedisService;

import jakarta.servlet.http.HttpServletRequest;

public class AuthUtil {

    public static boolean isLoggedIn(HttpServletRequest request, RedisService redisService, JwtUtil jwtUtil) {
        String token = jwtUtil.getTokenFromCookie(request);
        if (token == null) return false;

        String universityId = jwtUtil.extractUniversityId(token);
        if (!jwtUtil.validateToken(token, universityId)) return false;

        String redisToken = redisService.getToken(universityId);
        return redisToken != null && redisToken.equals(token);
    }
}
