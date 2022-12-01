package com.github.click.nd.rest.base.exception.impl;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(RuntimeException exception, HttpStatus status) {

}
