package com.github.click.nd.rest.generation.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.click.nd.rest.generation.service.exception.handlers.FilterExceptionHandler;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class FilterConfigurer extends
        SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        UserIdAuthenticationFilter filter = new UserIdAuthenticationFilter();
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        FilterExceptionHandler exceptionHandlerFilter = new FilterExceptionHandler(
                new ObjectMapper());
        exceptionHandlerFilter.afterPropertiesSet();
        httpSecurity.addFilterBefore(exceptionHandlerFilter, UserIdAuthenticationFilter.class);
    }
}
