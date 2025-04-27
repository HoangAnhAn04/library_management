package com.ntdev.library.middleware;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ntdev.library.enums.StatusCode;
import com.ntdev.library.helper.ResponseHandler;
import com.ntdev.library.service.RedisService;
import com.ntdev.library.util.JwtUtil;
import com.ntdev.library.validator.PathValidator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Middleware extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    public Middleware(JwtUtil jwtUtil, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String token = jwtUtil.getTokenFromCookie(request);
        String userId = jwtUtil.extractUserId(token);
        String universityId = jwtUtil.extractUniversityId(token);
        String role = jwtUtil.extractRole(token);
        String[] pathPart = path.split("/");
        String resourceId = pathPart[pathPart.length - 1];

        if (PathValidator.isExcluded(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token == null) {
            ResponseHandler.writeErrorResponse(response, path, StatusCode.TOKEN_NOT_FOUND, "Token not found");
            return;
        }

        if (!jwtUtil.validateToken(token, universityId)) {
            ResponseHandler.writeErrorResponse(response, path, StatusCode.TOKEN_INVALID_OR_EXPIRED,
                    "Token is invalid or expired");
            return;
        }

        String redisToken = redisService.getToken(Long.valueOf(universityId));
        if (redisToken == null || !redisToken.equals(token)) {
            ResponseHandler.writeErrorResponse(response, path, StatusCode.TOKEN_INVALID_OR_EXPIRED,
                    "Token is invalid or expired");
            return;
        }

        if (PathValidator.isAdminOnly(path) && !role.equals("ADMIN")) {
            ResponseHandler.writeErrorResponse(response, path, StatusCode.FORBIDDEN, "Access denied");
            return;
        }

        if (PathValidator.isAdminAndOwnerOnly(path) && !PathValidator.isOwner(universityId, resourceId)
                && !role.equals("ADMIN")) {
            ResponseHandler.writeErrorResponse(response, path, StatusCode.FORBIDDEN, "Access denied");
            return;
        }

        request.setAttribute("role", role);
        request.setAttribute("userId", Long.valueOf(userId));
        request.setAttribute("universityId", Long.valueOf(universityId));

        filterChain.doFilter(request, response);
    }
}
