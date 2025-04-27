package com.ntdev.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/info")
    public Map<String, Object> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Long universityId = Long.valueOf(request.getAttribute("universityId").toString());
        String role = (String) request.getAttribute("role");

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("universityId", universityId);
        response.put("role", role);

        return response;
    }
}
