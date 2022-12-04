package com.github.click.nd.rest.generation.service.service.gitlab;

import com.github.click.nd.rest.generation.service.service.generation.generator.ResourceSourceCode;
import com.github.click.nd.rest.generation.service.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.ProjectApi;
import org.gitlab4j.api.RepositoryApi;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.CommitAction;
import org.gitlab4j.api.models.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GitLabServiceImpl implements GitLabService {

    @Value("${generation.gitlab.group}")
    private String groupName;
    @Value("${generation.gitlab.base}")
    private String baseRepository;
    @Value("${generation.gitlab.base-branch}")
    private String baseBranch;
    private final ProjectApi projectApi;
    private final RepositoryApi repositoryApi;
    private final CommitsApi commitsApi;

    @Override
    @SneakyThrows
    public boolean isDefinitionPushed(String apiDefinitionId, int hash) {
        String userId = SecurityUtils.getUserId();
        String projectName = String.format("%s-%s", userId, apiDefinitionId);
        String branchName = String.valueOf(hash);
        return checkProjectExists(projectName) && checkBranchExists(projectName, branchName);
    }

    @Override
    @SneakyThrows
    public void pushGeneratedCode(String apiDefinitionId, int hash, Collection<ResourceSourceCode> resourceSourceCodes) {
        String userId = SecurityUtils.getUserId();
        String projectName = String.format("%s-%s", userId, hash);
        String branchName = String.valueOf(hash);
        Project project = projectApi.getProject(groupName, projectName);
        if (!checkProjectExists(projectName)) {
            project = projectApi.forkProject(baseRepository, groupName);
        }
        if (!checkBranchExists(projectName, branchName)) {
            repositoryApi.createBranch(project, branchName, baseBranch);
        }

        List<CommitAction> actions = createCommitActions(resourceSourceCodes);

        commitsApi.createCommit(project,
                branchName,
                "Adding generated API",
                null,
                "api-generator@clickndrest.com",
                "api-generator",
                actions);
    }

    private List<CommitAction> createCommitActions(Collection<ResourceSourceCode> resourceSourceCodes) {
        List<CommitAction> actions = new ArrayList<>();
        for (var resourceCode : resourceSourceCodes) {
            String name = resourceCode.resourceName();

            CommitAction entityCreation = new CommitAction()
                    .withAction(CommitAction.Action.CREATE)
                    .withFilePath(String.format("com/github/click/nd/rest/generated/service/entity/%s.java", name))
                    .withContent(resourceCode.entityCode());
            actions.add(entityCreation);

            CommitAction controllerCreation = new CommitAction()
                    .withAction(CommitAction.Action.CREATE)
                    .withFilePath(String.format("com/github/click/nd/rest/generated/service/controller/%sController.java", name))
                    .withContent(resourceCode.controllerCode());
            actions.add(controllerCreation);

            CommitAction repositoryCreation = new CommitAction()
                    .withAction(CommitAction.Action.CREATE)
                    .withFilePath(String.format("com/github/click/nd/rest/generated/service/repository/%sRepository.java", name))
                    .withContent(resourceCode.repositoryCode());
            actions.add(repositoryCreation);
        }
        return actions;
    }

    private boolean checkProjectExists(String project) throws GitLabApiException {
        return projectApi.getProject(groupName, project) != null;
    }

    private boolean checkBranchExists(String project, String branch) throws GitLabApiException {
        String projectPath = String.format("%s/%s", groupName, project);
        return repositoryApi.getBranch(projectPath, branch) != null;
    }
}
