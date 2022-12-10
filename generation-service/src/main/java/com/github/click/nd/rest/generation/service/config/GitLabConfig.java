package com.github.click.nd.rest.generation.service.config;

import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.ProjectApi;
import org.gitlab4j.api.RepositoryApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitLabConfig {
    @Bean
    public GitLabApi gitLabApi(
        @Value("${generation.gitlab.host}") String serverName,
        @Value("${generation.gitlab.token}") String personalAccessToken
    ) {
        return new GitLabApi(serverName, personalAccessToken);
    }

    @Bean
    public ProjectApi projectApi(GitLabApi gitLabApi) {
        return gitLabApi.getProjectApi();
    }

    @Bean
    public RepositoryApi repositoryApi(GitLabApi gitLabApi) {
        return gitLabApi.getRepositoryApi();
    }

    @Bean
    public CommitsApi commitsApi(GitLabApi gitLabApi) {
        return gitLabApi.getCommitsApi();
    }
}
