package com.github.click.nd.rest.generation.service.service.gitlab.factories;

public interface PathFactory {
    String getEntityPath(String resourceName);
    String getRepositoryPath(String resourceName);
    String getControllerPath(String resourceName);
    String getReadmePath();
}
