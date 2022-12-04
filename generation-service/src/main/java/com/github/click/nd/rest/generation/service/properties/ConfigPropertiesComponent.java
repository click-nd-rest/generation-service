package com.github.click.nd.rest.generation.service.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
public class ConfigPropertiesComponent {
    @Value("${generation.gitlab.group}")
    private String generationGitlabGroup;
}
