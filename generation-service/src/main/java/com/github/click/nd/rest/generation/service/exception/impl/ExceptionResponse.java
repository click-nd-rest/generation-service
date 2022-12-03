package com.github.click.nd.rest.generation.service.exception.impl;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(RuntimeException exception, HttpStatus status) {

}
