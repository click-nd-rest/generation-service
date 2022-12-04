package com.github.click.nd.rest.generation.service.service.gitlab.factories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GitLabPathFactoryImpl implements GitLabPathFactory {

    @Value("${generation.gitlab.root-package-path}")
    private String rootPackagePath;

    @Override
    public String getEntityPath(String resourceName) {
        return rootPackagePath + "/entity/" + resourceName + ".java";
    }

    @Override
    public String getRepositoryPath(String resourceName) {
        return rootPackagePath + "/repository/" + resourceName + "Repository.java";
    }

    @Override
    public String getControllerPath(String resourceName) {
        return rootPackagePath + "/controller/" + resourceName + "Controller.java";
    }

    @Override
    public String getReadmePath() {
        return "README.md";
    }
}
