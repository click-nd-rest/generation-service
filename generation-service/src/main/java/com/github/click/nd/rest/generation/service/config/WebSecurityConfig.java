package com.github.click.nd.rest.generation.service.config;

import com.github.click.nd.rest.generation.service.security.FilterConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain customizer(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
            .antMatchers("/v1/api-definition").hasRole("user")
            .anyRequest().authenticated()
            .and()
            .apply(new FilterConfigurer());
        return httpSecurity.build();
    }
}
