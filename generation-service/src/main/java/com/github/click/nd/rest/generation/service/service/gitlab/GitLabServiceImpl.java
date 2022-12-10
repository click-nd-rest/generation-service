package com.github.click.nd.rest.generation.service.service.gitlab;

import com.github.click.nd.rest.generation.service.service.generation.generator.ResourceSourceCode;
import com.github.click.nd.rest.generation.service.service.gitlab.factories.GitLabCommitFactory;
import com.github.click.nd.rest.generation.service.util.SecurityUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.ProjectApi;
import org.gitlab4j.api.RepositoryApi;
import org.gitlab4j.api.models.CommitAction;
import org.gitlab4j.api.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class GitLabServiceImpl implements GitLabService {

    private final ProjectApi projectApi;
    private final RepositoryApi repositoryApi;
    private final CommitsApi commitsApi;
    private final GitLabCommitFactory commitFactory;
    @Value("${generation.gitlab.base-group}")
    private String baseGroup;
    @Value("${generation.gitlab.target-group}")
    private String targetGroup;
    @Value("${generation.gitlab.base-repository}")
    private String baseRepository;
    @Value("${generation.gitlab.base-branch}")
    private String baseBranch;

    @Override
    @SneakyThrows
    public boolean isDefinitionPushed(String apiDefinitionId, int hash) {
        String projectName = getProjectName(SecurityUtil.getUserId(), apiDefinitionId);
        String branchName = String.valueOf(hash);
        log.info(
            "isDefinitionPushed(), project name: {}, branch name: {}", projectName, branchName
        );
        return isProjectExists(projectName) && isBranchExists(projectName, branchName);
    }

    @Override
    @SneakyThrows
    public void pushGeneratedCode(
        String apiDefinitionId,
        String apiDefinitionVerbose,
        int hash,
        Collection<ResourceSourceCode> resourceSourceCodes
    ) {
        var projectName = getProjectName(SecurityUtil.getUserId(), apiDefinitionId);
        var project = resolveProject(projectName);

        String branchName = String.valueOf(hash);
        if (!isBranchExists(projectName, branchName)) {
            repositoryApi.createBranch(project, branchName, baseBranch);
        }

        commitsApi.createCommit(project,
            branchName,
            "Adding generated API",
            null,
            "api-generator@clickndrest.com",
            "api-generator",
            createCommitActions(resourceSourceCodes, apiDefinitionVerbose)
        );
    }

    private String getProjectName(String userId, String apiDefinitionId) {
        return userId + "-" + apiDefinitionId;
    }

    private boolean isProjectExists(String project) throws GitLabApiException {
        try {
            projectApi.getProject(targetGroup, project);
            return true;
        } catch (GitLabApiException e) {
            log.info("isProjectExists(), message: {}", e.getMessage());
            if ("404 Project Not Found".equals(e.getMessage())) {
                return false;
            } else {
                throw new GitLabApiException(e);
            }
        }
    }

    private boolean isBranchExists(String project, String branch) throws GitLabApiException {
        String projectPath = targetGroup + "/" + project;
        try {
            repositoryApi.getBranch(projectPath, branch);
            return true;
        } catch (GitLabApiException e) {
            log.info("isBranchExists(), message: {}", e.getMessage());
            if ("404 Project Not Found".equals(e.getMessage())
                || "404 Branch Not Found".equals(e.getMessage())) {
                return false;
            } else {
                throw new GitLabApiException(e);
            }
        }
    }

    private Project resolveProject(String projectName) throws GitLabApiException {
        if (isProjectExists(projectName)) {
            return projectApi.getProject(targetGroup, projectName);
        } else {
            return projectApi.forkProject(
                baseGroup + "/" + baseRepository,
                targetGroup,
                projectName,
                projectName
            );
        }
    }

    private List<CommitAction> createCommitActions(
        Collection<ResourceSourceCode> resourceSourceCodes, String verbose
    ) {
        //Adding 3 actions per resource source code
        List<CommitAction> actions = new ArrayList<>(resourceSourceCodes.size() * 3);
        for (var resourceCode : resourceSourceCodes) {
            String resourceName = resourceCode.getResourceNameUpperCamel();

            actions.add(createEntityCommitAction(resourceName, resourceCode.entityCode()));
            actions.add(createRepositoryCommitAction(resourceName, resourceCode.repositoryCode()));
            actions.add(createControllerCommitAction(resourceName, resourceCode.controllerCode()));
        }
        actions.addAll(createOverwriteReadmeCommitAction(verbose));
        return actions;
    }

    private CommitAction createEntityCommitAction(String resourceName, String entityCode) {
        return commitFactory.createEntityCommitAction(resourceName, entityCode);
    }

    private CommitAction createRepositoryCommitAction(String resourceName, String repositoryCode) {
        return commitFactory.createRepositoryCommitAction(resourceName, repositoryCode);
    }

    private CommitAction createControllerCommitAction(String resourceName, String controllerCode) {
        return commitFactory.createControllerCommitAction(resourceName, controllerCode);
    }

    private Collection<CommitAction> createOverwriteReadmeCommitAction(String verbose) {
        return commitFactory.createOverwriteReadmeCommitActions(verbose);
    }
}
