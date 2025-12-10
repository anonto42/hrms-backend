package com.hrmf.hrms_backend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class FileUploadInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (request.getContentType() != null &&
                request.getContentType().startsWith("multipart/form-data")) {

            long contentLength = request.getContentLengthLong();
            long maxSize = 10 * 1024 * 1024; // 10MB

            if (contentLength > maxSize) {
                response.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"error\":\"File size exceeds maximum limit of 10MB\"}");
                return false;
            }
        }
        return true;
    }
}