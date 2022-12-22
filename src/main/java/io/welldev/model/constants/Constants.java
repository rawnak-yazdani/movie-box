package io.welldev.model.constants;

public class Constants {
    public static class API {
        public static final String CONTEXT_PATH = "/";

        public static final String USERS = "/users";

        public static final String ADD_A_USER_BY_ADMIN = "/users";

        public static final String ADMIN = "/admin";

        public static final String SHOW_ALL_MOVIES = "/movies";

        public static final String ADD_A_MOVIE_BY_ADMIN = "/movies";

        public static final String UPDATE_A_MOVIE_BY_ADMIN = "/movies/{id}";

        public static final String DELETE_A_MOVIE_BY_ADMIN = "/movies/{id}";

        public static final String SHOW_A_USER = "/{username}";

        public static final String UPDATE_USER_WATCHLIST = "/{username}/watchlist";

        public static final String DELETE_FROM_USER_WATCHLIST = "/{username}/watchlist";

        public static final String LOGOUT_A_USER = "/logout";
    }

    public static class Strings {
        public static final String HEADERS_JSON = "Accept=application/json";

        public static final String PRODUCES_JSON = "application/json";

        public static final String USER_ROLE = "user";

        public static final String ADMIN_ROLE = "admin";
    }
}
