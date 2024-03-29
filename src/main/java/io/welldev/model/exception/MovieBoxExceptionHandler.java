package io.welldev.model.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.welldev.model.constants.Constants.*;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class MovieBoxExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * This will handle exception in the request
     * @Status 400
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(prepareErrorJSON(status, ex), status);
    }

    /**
     * This exception is thrown when an argument annotated with @Valid failed validation
     * @Status 400
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        JSONObject jsonObjectOfErrors = new JSONObject();

        try {
            List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

            jsonObjectOfErrors.put(AppStrings.STATUS, status.value());
            jsonObjectOfErrors.put(AppStrings.ERROR, status.getReasonPhrase());
            jsonObjectOfErrors.put(AppStrings.MESSAGE, errors);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(jsonObjectOfErrors.toString(), status);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Object> itemNotFoundException(ItemNotFoundException ex) {
        return new ResponseEntity<>(prepareErrorJSON(HttpStatus.NOT_FOUND, ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();

        ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

        Map<String, List<String>> result = new HashMap<>();

        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> constraintViolationException(ResponseStatusException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        Map<String, List<String>> result = new HashMap<>();

        errors.add(ex.getReason());
        result.put("errors", errors);

        return new ResponseEntity<>(result, ex.getStatus());
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> jwtExpiredException(ResponseStatusException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        Map<String, List<String>> result = new HashMap<>();

        errors.add(ex.getReason());
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }

    private String prepareErrorJSON(HttpStatus status, Exception ex) {
        JSONObject jsonObjectOfErrors = new JSONObject();

        try {
            jsonObjectOfErrors.put(AppStrings.STATUS, status.value());
            jsonObjectOfErrors.put(AppStrings.ERROR, status.getReasonPhrase());
//            jsonObjectOfErrors.put("message", ex.getMessage().split(":")[0]);
            jsonObjectOfErrors.put(AppStrings.MESSAGE, ex.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObjectOfErrors.toString();
    }
}