package com.exam.constants;

public class ExamPortalConstant {

    public static final String LOCAL_BASE_PATH = "http://localhost:8080";
    public static final String COMMON_PATH = "/api";

    public static final String USER_BASE_PATH = COMMON_PATH + "/users";

    public static final String USER_PARAMETER = "userId";

    public static final String USER_EXCEPTION = "User";

    public static final String EXCEPTION_FIELD = "id";

    public static final String AUTH_BASE_PATH = COMMON_PATH + "/auth";

    public static final String LOGIN = "/login";

    public static final String TOKEN_PARAMETER = "token";

    public static final String CHANGE_PASSWORD = "/change-password";

    public static final String ROLES = "/roles";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_PREFIX = "Bearer ";

    public static final String VERIFY_USER = "/verify";

    public static final String ROLE_BASE_PAT = COMMON_PATH + "/roles";

    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "10";

    public static final String USER_SORT_FIELD = "createdAt";

    public static final String[] PUBLIC_URLS = {
            AUTH_BASE_PATH + LOGIN,
            USER_BASE_PATH + VERIFY_USER +"/**",
            "/v3/api-docs",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**"
    };

}
