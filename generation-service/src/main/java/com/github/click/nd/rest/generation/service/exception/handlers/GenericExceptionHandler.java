package com.github.click.nd.rest.generation.service.exception.handlers;

import com.github.click.nd.rest.generation.service.exception.impl.ExceptionResponse;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleRuntimeException(RuntimeException ex) {
        return new ExceptionResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
