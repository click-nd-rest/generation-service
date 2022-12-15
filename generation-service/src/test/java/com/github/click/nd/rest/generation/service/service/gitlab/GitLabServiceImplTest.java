package com.github.click.nd.rest.generation.service.service.gitlab;

import com.github.click.nd.rest.generation.service.service.generation.BaseCodeGeneratorTest;
import com.github.click.nd.rest.generation.service.service.generation.generator.ResourceSourceCode;
import com.github.click.nd.rest.generation.service.service.gitlab.factories.GitLabCommitFactory;
import com.github.click.nd.rest.generation.service.util.SecurityUtil;
import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.ProjectApi;
import org.gitlab4j.api.RepositoryApi;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.CommitAction;
import org.gitlab4j.api.models.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Local test for gitlab service. To run this test you need to attach personal Gitlab token to the run
 * environment: <a href="https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html">gitlab documentation about where to get it</a>.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityUtil.class)
public class GitLabServiceImplTest extends BaseCodeGeneratorTest {


    private final ProjectApi projectApi = Mockito.mock(ProjectApi.class);
    private final RepositoryApi repositoryApi = Mockito.mock(RepositoryApi.class);
    private final CommitsApi commitsApi = Mockito.mock(CommitsApi.class);
    private final GitLabCommitFactory commitFactory = Mockito.mock(GitLabCommitFactory.class);
    private final GitLabService gitLabService = new GitLabServiceImpl(projectApi, repositoryApi, commitsApi, commitFactory);

    @BeforeAll
    static void setUp() {
        PowerMockito.mockStatic(SecurityUtil.class);
        when(SecurityUtil.getUserId()).thenReturn("userId");
    }

    @Test
    public void successfullyCheckThatDefinitionIsPushed() throws GitLabApiException {
        ReflectionTestUtils.setField(gitLabService, "targetGroup", "targetGroup");
        ReflectionTestUtils.setField(gitLabService, "baseBranch", "baseBranch");

        when(projectApi.getProject("targetGroup", "userId-apiDefinitionId")).thenReturn(new Project());
        when(repositoryApi.getBranch("targetGroup/userId-apiDefinitionId", "1")).thenReturn(new Branch());

        boolean definitionPushed = gitLabService.isDefinitionPushed("apiDefinitionId", 1);

        verify(projectApi).getProject("targetGroup", "userId-apiDefinitionId");
        verify(repositoryApi).getBranch("targetGroup/userId-apiDefinitionId", "1");
        assertThat(definitionPushed).isTrue();
    }

    @Test
    public void checkThatDefinitionIsNotPushedWhenProjectNotExists() throws GitLabApiException {
        ReflectionTestUtils.setField(gitLabService, "targetGroup", "targetGroup");
        ReflectionTestUtils.setField(gitLabService, "baseBranch", "baseBranch");

        when(projectApi.getProject("targetGroup", "userId-apiDefinitionId")).thenThrow(new GitLabApiException("404 Project Not Found"));

        boolean definitionPushed = gitLabService.isDefinitionPushed("apiDefinitionId", 1);

        verify(projectApi).getProject("targetGroup", "userId-apiDefinitionId");
        assertThat(definitionPushed).isFalse();
    }

    @Test
    public void checkThatDefinitionIsNotPushedWhenBranchNotExists() throws GitLabApiException {
        ReflectionTestUtils.setField(gitLabService, "targetGroup", "targetGroup");
        ReflectionTestUtils.setField(gitLabService, "baseBranch", "baseBranch");

        when(projectApi.getProject("targetGroup", "userId-apiDefinitionId")).thenReturn(new Project());
        when(repositoryApi.getBranch("targetGroup/userId-apiDefinitionId", "1")).thenThrow(new GitLabApiException("404 Branch Not Found"));

        boolean definitionPushed = gitLabService.isDefinitionPushed("apiDefinitionId", 1);

        verify(projectApi.getProject("targetGroup", "userId-apiDefinitionId"));
        verify(repositoryApi).getBranch("targetGroup/userId-apiDefinitionId", "1");
        assertThat(definitionPushed).isFalse();
    }

    @Test
    public void successfullyPushGeneratedCodeWhenProjectAndBranchExist() throws GitLabApiException {
        ReflectionTestUtils.setField(gitLabService, "targetGroup", "targetGroup");
        ReflectionTestUtils.setField(gitLabService, "baseBranch", "baseBranch");

        Project project = new Project();
        when(projectApi.getProject("targetGroup", "userId-apiDefinitionId")).thenReturn(project);

        Branch branch = new Branch();
        when(repositoryApi.getBranch("targetGroup/userId-apiDefinitionId", "1")).thenReturn(branch);

        when(commitFactory.createEntityCommitAction("Name", "entitiy")).thenReturn(new CommitAction());
        when(commitFactory.createControllerCommitAction("Name", "controller")).thenReturn(new CommitAction());
        when(commitFactory.createRepositoryCommitAction("Name", "repository")).thenReturn(new CommitAction());
        when(commitFactory.createOverwriteReadmeCommitActions("verbose")).thenReturn(List.of(new CommitAction()));

        doNothing().when(commitsApi.createCommit(
                project,
                "1",
                "Adding generated API",
                null,
                "api-generator@clickndrest.com",
                "api-generator",
                List.of(any(CommitAction.class), any(CommitAction.class), any(CommitAction.class))
        ));

        gitLabService.pushGeneratedCode(
                "apiDefinitionId",
                "verbose",
                1,
                List.of(
                        new ResourceSourceCode(
                                "name",
                                "entity",
                                "repository",
                                "controller"
                        )
                )
        );

        verify(projectApi.getProject("targetGroup", "userId-apiDefinitionId"));
        verify(repositoryApi.getBranch("targetGroup/userId-apiDefinitionId", "1"));
        verify(repositoryApi.createBranch(project, "1", "baseBranch"));

        verify(commitFactory.createEntityCommitAction("Name", "entitiy"));
        verify(commitFactory.createControllerCommitAction("Name", "controller"));
        verify(commitFactory.createRepositoryCommitAction("Name", "repository"));
        verify(commitFactory.createOverwriteReadmeCommitActions("verbose"));
    }

    @Test
    public void successfullyPushGeneratedCodeWhenProjectNotExists() throws GitLabApiException {
        ReflectionTestUtils.setField(gitLabService, "targetGroup", "targetGroup");
        ReflectionTestUtils.setField(gitLabService, "baseBranch", "baseBranch");
        ReflectionTestUtils.setField(gitLabService, "baseRepository", "baseRepository");

        when(projectApi.getProject("targetGroup", "userId-apiDefinitionId")).thenThrow(new GitLabApiException("404 Project Not Found"));
        Project project = new Project();
        when(projectApi
                .forkProject(
                        "baseGroup/baseRepository",
                        "targetGroup",
                        "userId-apiDefinitionId",
                        "userId-apiDefinitionId"))
                .thenReturn(project);

        when(repositoryApi.getBranch("targetGroup/userId-apiDefinitionId", "1")).thenThrow(new GitLabApiException("404 Branch Not Found"));
        doNothing().when(repositoryApi.createBranch(project, "1", "baseBranch"));

        when(commitFactory.createEntityCommitAction("Name", "entitiy")).thenReturn(new CommitAction());
        when(commitFactory.createControllerCommitAction("Name", "controller")).thenReturn(new CommitAction());
        when(commitFactory.createRepositoryCommitAction("Name", "repository")).thenReturn(new CommitAction());
        when(commitFactory.createOverwriteReadmeCommitActions("verbose")).thenReturn(List.of(new CommitAction()));

        doNothing().when(commitsApi.createCommit(
                project,
                "1",
                "Adding generated API",
                null,
                "api-generator@clickndrest.com",
                "api-generator",
                anyList()
        ));

        gitLabService.pushGeneratedCode(
                "apiDefinitionId",
                "verbose",
                1,
                List.of(
                        new ResourceSourceCode(
                                "name",
                                "entity",
                                "repository",
                                "controller"
                        )
                )
        );

        verify(projectApi.getProject("targetGroup", "userId-apiDefinitionId"));
        verify(repositoryApi.getBranch("targetGroup/userId-apiDefinitionId", "1"));
        verify(repositoryApi.createBranch(project, "1", "baseBranch"));

        verify(commitFactory.createEntityCommitAction("Name", "entitiy"));
        verify(commitFactory.createControllerCommitAction("Name", "controller"));
        verify(commitFactory.createRepositoryCommitAction("Name", "repository"));
        verify(commitFactory.createOverwriteReadmeCommitActions("verbose"));
    }
}
