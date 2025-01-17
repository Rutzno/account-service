package com.diarpy.accountservice.exception;

import com.diarpy.accountservice.service.SecurityEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.6
 */

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    public static final Logger LOG = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
    @Autowired
    private SecurityEventService securityEventService;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        Authentication auth = (Authentication) request.getUserPrincipal();
        if (auth != null) {
            LOG.warn("User: {} attempted to access the protected URL: {}", auth.getName(), request.getRequestURI());
            securityEventService.save("ACCESS_DENIED", auth.getName(), request.getRequestURI(), request.getRequestURI());
        }
//        response.sendRedirect(request.getContextPath() + "/accessDenied");
        HttpStatus status = HttpStatus.FORBIDDEN;
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Access Denied!");
        responseBody.put("status", status.value());
        responseBody.put("error", "Forbidden");
        responseBody.put("path", request.getRequestURI());
        responseBody.put("timestamp", LocalDateTime.now().toString());

        String jsonResponse = new ObjectMapper().writeValueAsString(responseBody);
        response.getWriter().write(jsonResponse);
    }
}