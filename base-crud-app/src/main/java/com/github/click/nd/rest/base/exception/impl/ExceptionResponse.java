package com.github.click.nd.rest.base.exception.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ExceptionResponse {

    private final String responseMessage;
    private final HttpStatus responseStatus;
}
