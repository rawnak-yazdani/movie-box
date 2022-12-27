package io.welldev.model.constants;

public class Constants {
    public static class API {
        public static final String CONTEXT_PATH = "/";

        public static final String AUTH_REFRESH = "/token";
        public static final String USERS = "/users";

        public static final String ADD_A_USER_BY_ADMIN = "/users";

        public static final String ADMIN = "/admin";

        public static final String SHOW_ALL_MOVIES = "/movies";

        public static final String SHOW_A_MOVIE = "/movies/{id}";

        public static final String ADD_A_MOVIE_BY_ADMIN = "/movies";

        public static final String UPDATE_A_MOVIE_BY_ADMIN = "/movies/{id}";

        public static final String DELETE_A_MOVIE_BY_ADMIN = "/movies/{id}";

        public static final String DELETE_A_USER_BY_ADMIN = "/users/{id}";

        public static final String SHOW_A_USER = "/{username}";

        public static final String UPDATE_USER_WATCHLIST = "/{username}/watchlist";

        public static final String DELETE_FROM_USER_WATCHLIST = "/{username}/watchlist";

        public static final String DELETE_USER = "/{username}";

        public static final String LOGOUT_A_USER = "/logout";
    }

    public static class AppStrings {
        public static final String HEADERS_JSON = "Accept=application/json";

        public static final String PRODUCES_JSON = "application/json";

        public static final String USER_ROLE = "user";

        public static final String ADMIN_ROLE = "admin";

        public static final String TOKEN_EXPIRE_TIME = "TOKEN_EXPIRE_TIME";

        public static final String TOKEN_SECRET_KEY = "TOKEN_SECRET_KEY";

        public static final String AUTHORITIES = "authorities";

        public static final String AUTHORITY = "authority";

        public static final String ACCESS_TOKEN = "ACCESS_TOKEN";

        public static final String REFRESH_TOKEN = "REFRESH_TOKEN";

        public static final String ISSUED_AT = "issued at";

        public static final String EXPIRE_AT_ACCESS_TOKEN = "access token will expire at";

        public static final String EXPIRE_AT_REFRESH_TOKEN = "refresh token will expire at";

        public static final String AUTHORIZATION = "Authorization";

        public static final String COOKIE = "Cookie";

        public static final String RENEW_AUTH = "Renew-auth";

        public static final String STATUS = "status";

        public static final String ERROR = "error";

        public static final String MESSAGE = "message";
    }
}
