package com.hrmFirm.common.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AppConstants {

    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_NUMBER = 0;

    // File Upload
    public static final long MAX_UPLOAD_SIZE = 10 * 1024 * 1024; // 10 MB
    public static final String[] ALLOWED_FILE_TYPES = {
            "image/jpeg",
            "image/png",
            "application/pdf"
    };

    // Security
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    // Date & Time
    public static final String DEFAULT_TIMEZONE = "UTC";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

}
