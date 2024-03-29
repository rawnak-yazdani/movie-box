package io.welldev.model.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class AppAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
        ArrayList<String> errors = new ArrayList<>();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        Map<String, List<String>> data = new HashMap<>();
        errors.add("Username or Password is Wrong. Otherwise the user is needed to sign up.");
        data.put(
                "message",
                errors
        );

        response.getOutputStream()
                .println(new ObjectMapper().writeValueAsString(data));
    }
}
