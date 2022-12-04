package com.github.click.nd.rest.generation.service.exception.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.click.nd.rest.generation.service.exception.impl.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilterExceptionHandler extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public FilterExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
            response.getWriter().write(convertObjectToJson(exceptionResponse));
        }
    }

    private String convertObjectToJson(ExceptionResponse exceptionResponse) throws JsonProcessingException {
        return objectMapper.writeValueAsString(exceptionResponse);
    }
}
