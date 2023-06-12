package com.bagrov.springpmhw.videorent.constants;

public interface SecurityConstants {

    String[] RESOURCES_WHITE_LIST = {
            "/resources/**",
            "/static/**",
            "/js/**",
            "/css/**",
            "/",
            "/swagger-ui/**",
            "/webjars/bootstrap/5.3.0/**",
            "/webjars/bootstrap/5.3.0/css/**",
            "/webjars/bootstrap/5.3.0/js/**",
            "/v3/api-docs/**"
    };

    String[] FILMS_WHITE_LIST = {
            "/films"
    };

    String[] FILMS_PERMISSIONS_LIST = {
            "/films/addFilm"
    };

    String[] FILMS_REST_PERMISSIONS_LIST = {
            "/api/films/addFilm"
    };

    String[] DIRECTORS_WHITE_LIST = {
            "/directors"
    };

    String[] DIRECTORS_PERMISSIONS_LIST = {
            "/directors/addDirector"
    };

    String[] DIRECTORS_REST_PERMISSIONS_LIST = {
            "/api/directors/addDirector"
    };

    String[] USERS_WHITE_LIST = {
            "/login",
            "/users/registration",
            "/users/remember-password"
    };

    String[] ADMIN_PERMISSION_LIST = {
            "/users/allUsers"
    };

    String[] USERS_PERMISSION_LIST = {
            "/users/profile/**",
            "/users/edit"
    };

    String[] USERS_REST_WHITE_LIST = {
            "/api/users/auth"
    };
}
