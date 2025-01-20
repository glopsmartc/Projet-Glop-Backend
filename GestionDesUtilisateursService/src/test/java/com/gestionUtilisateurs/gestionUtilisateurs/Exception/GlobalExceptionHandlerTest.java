package com.gestionUtilisateurs.gestionUtilisateurs.Exception;

import com.gestionUtilisateurs.gestionUtilisateurs.exception.GlobalExceptionHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleBadCredentialsException() {
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");
        ProblemDetail problemDetail = globalExceptionHandler.handleSecurityException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), problemDetail.getStatus());
        assertEquals("The username or password is incorrect", problemDetail.getProperties().get("description"));
    }

    @Test
    void testHandleAccountStatusException() {
        AccountStatusException exception = new LockedException("Account locked");
        ProblemDetail problemDetail = globalExceptionHandler.handleSecurityException(exception);

        assertEquals(HttpStatus.FORBIDDEN.value(), problemDetail.getStatus());
        assertEquals("The account is locked", problemDetail.getProperties().get("description"));
    }

    @Test
    void testHandleAccessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        ProblemDetail problemDetail = globalExceptionHandler.handleSecurityException(exception);

        assertEquals(HttpStatus.FORBIDDEN.value(), problemDetail.getStatus());
        assertEquals("You are not authorized to access this resource", problemDetail.getProperties().get("description"));
    }

    @Test
    void testHandleSignatureException() {
        SignatureException exception = new SignatureException("Invalid signature");
        ProblemDetail problemDetail = globalExceptionHandler.handleSecurityException(exception);

        assertEquals(HttpStatus.FORBIDDEN.value(), problemDetail.getStatus());
        assertEquals("The JWT signature is invalid", problemDetail.getProperties().get("description"));
    }

    @Test
    void testHandleExpiredJwtException() {
        Header<?> header = new DefaultHeader<>();
        Claims claims = new DefaultClaims();
        ExpiredJwtException exception = new ExpiredJwtException(header, claims, "Token expired");

        ProblemDetail problemDetail = globalExceptionHandler.handleSecurityException(exception);

        assertEquals(HttpStatus.FORBIDDEN.value(), problemDetail.getStatus());
        assertEquals("The JWT token has expired", problemDetail.getProperties().get("description"));
    }

    @Test
    void testHandleGenericException() {
        Exception exception = new Exception("Unknown error");
        ProblemDetail problemDetail = globalExceptionHandler.handleSecurityException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
        assertEquals("Unknown internal server error.", problemDetail.getProperties().get("description"));
    }
}