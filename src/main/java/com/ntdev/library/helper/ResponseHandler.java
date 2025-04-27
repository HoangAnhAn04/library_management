package com.ntdev.library.helper;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntdev.library.dto.response.ApiResponse;
import com.ntdev.library.enums.StatusCode;

import jakarta.servlet.http.HttpServletResponse;

public class ResponseHandler {

    public static void writeErrorResponse(HttpServletResponse response, String path, StatusCode statusCode,
            String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .success(false)
                .status(statusCode.getCode())
                .message(message)
                .details(null)
                .path(path)
                .build();
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}
