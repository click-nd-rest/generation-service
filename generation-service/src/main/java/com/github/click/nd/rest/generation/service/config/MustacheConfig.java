package com.github.click.nd.rest.generation.service.config;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MustacheConfig {
    @Bean
    public MustacheFactory mustacheFactory() {
        return new DefaultMustacheFactory();
    }
}
