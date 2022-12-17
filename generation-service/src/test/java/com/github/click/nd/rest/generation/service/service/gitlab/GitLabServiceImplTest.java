package com.github.click.nd.rest.generation.service.service.gitlab;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.click.nd.rest.generation.service.service.generation.generator.ResourceSourceCode;
import com.github.click.nd.rest.generation.service.service.gitlab.factories.GitLabCommitFactory;
import com.github.click.nd.rest.generation.service.util.SecurityUtil;
import java.util.List;
import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.ProjectApi;
import org.gitlab4j.api.RepositoryApi;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.CommitAction;
import org.gitlab4j.api.models.Project;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Local test for gitlab service. To run this test you need to attach personal Gitlab token to the run
 * environment: <a href="https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html">gitlab documentation about where to get it</a>.
 */
@SpringBootTest
public class GitLabServiceImplTest {


    private final ProjectApi projectApi = mock(ProjectApi.class);
    private final RepositoryApi repositoryApi = mock(RepositoryApi.class);
    private final CommitsApi commitsApi = mock(CommitsApi.class);
    private final GitLabCommitFactory commitFactory = mock(GitLabCommitFactory.class);
    private final GitLabService gitLabService =
            new GitLabServiceImpl(projectApi, repositoryApi, commitsApi, commitFactory);

    @Test
    public void successfullyCheckThatDefinitionIsPushed() throws GitLabApiException {
        ReflectionTestUtils.setField(gitLabService, "targetGroup", "targetGroup");
        ReflectionTestUtils.setField(gitLabService, "baseBranch", "baseBranch");

        try (MockedStatic<SecurityUtil> utilMock = mockStatic(SecurityUtil.class)) {
            utilMock.when(SecurityUtil::getUserId).thenReturn("userId");
            when(projectApi.getProject("targetGroup", "userId-apidefinitionid"))
                    .thenReturn(new Project());
            when(repositoryApi.getBranch("targetGroup/userId-apidefinitionid", "1"))
                    .thenReturn(new Branch());

            boolean definitionPushed = gitLabService.isDefinitionPushed("apidefinitionid", 1);

            verify(projectApi).getProject("targetGroup", "userId-apidefinitionid");
            verify(repositoryApi).getBranch("targetGroup/userId-apidefinitionid", "1");
            assertThat(definitionPushed).isTrue();
        }
    }

    @Test
    public void checkThatDefinitionIsNotPushedWhenProjectNotExists() throws GitLabApiException {
        ReflectionTestUtils.setField(gitLabService, "targetGroup", "targetGroup");
        ReflectionTestUtils.setField(gitLabService, "baseBranch", "baseBranch");

        try (MockedStatic<SecurityUtil> utilMock = mockStatic(SecurityUtil.class)) {
            utilMock.when(SecurityUtil::getUserId).thenReturn("userId");
            when(projectApi.getProject("targetGroup", "userId-apidefinitionid"))
                    .thenThrow(new GitLabApiException("404 Project Not Found"));

            boolean definitionPushed = gitLabService.isDefinitionPushed("apidefinitionid", 1);

            verify(projectApi).getProject("targetGroup", "userId-apidefinitionid");
            assertThat(definitionPushed).isFalse();
        }
    }

    @Test
    public void checkThatDefinitionIsNotPushedWhenBranchNotExists() throws GitLabApiException {
        ReflectionTestUtils.setField(gitLabService, "targetGroup", "targetGroup");
        ReflectionTestUtils.setField(gitLabService, "baseBranch", "baseBranch");

        try (MockedStatic<SecurityUtil> utilMock = mockStatic(SecurityUtil.class)) {
            utilMock.when(SecurityUtil::getUserId).thenReturn("userId");
            when(projectApi.getProject("targetGroup", "userId-apidefinitionid"))
                    .thenReturn(new Project());
            when(repositoryApi.getBranch("targetGroup/userId-apidefinitionid", "1"))
                    .thenThrow(new GitLabApiException("404 Branch Not Found"));

            boolean definitionPushed = gitLabService.isDefinitionPushed("apidefinitionid", 1);

            verify(projectApi).getProject("targetGroup", "userId-apidefinitionid");
            verify(repositoryApi).getBranch("targetGroup/userId-apidefinitionid", "1");
            assertThat(definitionPushed).isFalse();
        }
    }

    @Test
    public void successfullyPushGeneratedCodeWhenProjectAndBranchExist() throws GitLabApiException {
        ReflectionTestUtils.setField(gitLabService, "targetGroup", "targetGroup");
        ReflectionTestUtils.setField(gitLabService, "baseBranch", "baseBranch");

        try (MockedStatic<SecurityUtil> utilMock = mockStatic(SecurityUtil.class)) {
            utilMock.when(SecurityUtil::getUserId).thenReturn("userId");
            Project project = new Project();
            when(projectApi.getProject("targetGroup", "userId-apidefinitionid")).thenReturn(project);

            Branch branch = new Branch();
            when(repositoryApi.getBranch("targetGroup/userId-apidefinitionid", "1"))
                    .thenReturn(branch);

            when(commitFactory.createEntityCommitAction("Name", "entity"))
                    .thenReturn(new CommitAction());
            when(commitFactory.createControllerCommitAction("Name", "controller"))
                    .thenReturn(new CommitAction());
            when(commitFactory.createRepositoryCommitAction("Name", "repository"))
                    .thenReturn(new CommitAction());
            when(commitFactory.createOverwriteReadmeCommitActions("verbose"))
                    .thenReturn(List.of(new CommitAction()));

            when(commitsApi.createCommit(
                    any(Project.class),
                    anyString(),
                    anyString(),
                    any(),
                    anyString(),
                    anyString(),
                    anyList()
            )).thenReturn(new Commit());

            gitLabService.pushGeneratedCode(
                    "apidefinitionid",
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

            verify(projectApi, times(2)).getProject("targetGroup", "userId-apidefinitionid");
            verify(repositoryApi).getBranch("targetGroup/userId-apidefinitionid", "1");
            verify(commitsApi).createCommit(
                    any(Project.class),
                    anyString(),
                    anyString(),
                    any(),
                    anyString(),
                    anyString(),
                    anyList()
            );

            verify(commitFactory).createEntityCommitAction("Name", "entity");
            verify(commitFactory).createControllerCommitAction("Name", "controller");
            verify(commitFactory).createRepositoryCommitAction("Name", "repository");
            verify(commitFactory).createOverwriteReadmeCommitActions("verbose");
        }
    }

    @Test
    public void successfullyPushGeneratedCodeWhenProjectNotExists() throws GitLabApiException {
        ReflectionTestUtils.setField(gitLabService, "targetGroup", "targetGroup");
        ReflectionTestUtils.setField(gitLabService, "baseGroup", "baseGroup");
        ReflectionTestUtils.setField(gitLabService, "baseBranch", "baseBranch");
        ReflectionTestUtils.setField(gitLabService, "baseRepository", "baseRepository");

        try (MockedStatic<SecurityUtil> utilMock = mockStatic(SecurityUtil.class)) {
            utilMock.when(SecurityUtil::getUserId).thenReturn("userId");
            when(projectApi.getProject("targetGroup", "userId-apidefinitionid"))
                    .thenThrow(new GitLabApiException("404 Project Not Found"));
            Project project = new Project();
            when(projectApi
                    .forkProject(
                            "baseGroup/baseRepository",
                            "targetGroup",
                            "userId-apidefinitionid",
                            "userId-apidefinitionid"))
                    .thenReturn(project);

            when(repositoryApi.getBranch("targetGroup/userId-apidefinitionid", "1"))
                    .thenThrow(new GitLabApiException("404 Branch Not Found"));
            when(repositoryApi.createBranch(project, "1", "baseBranch"))
                    .thenReturn(new Branch());

            when(commitFactory.createEntityCommitAction("Name", "entity"))
                    .thenReturn(new CommitAction());
            when(commitFactory.createControllerCommitAction("Name", "controller"))
                    .thenReturn(new CommitAction());
            when(commitFactory.createRepositoryCommitAction("Name", "repository"))
                    .thenReturn(new CommitAction());
            when(commitFactory.createOverwriteReadmeCommitActions("verbose"))
                    .thenReturn(List.of(new CommitAction()));

            when(commitsApi.createCommit(
                    any(Project.class),
                    anyString(),
                    anyString(),
                    any(),
                    anyString(),
                    anyString(),
                    anyList()
            )).thenReturn(new Commit());

            gitLabService.pushGeneratedCode(
                    "apidefinitionid",
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

            verify(projectApi).getProject("targetGroup", "userId-apidefinitionid");
            verify(projectApi).forkProject(
                    "baseGroup/baseRepository",
                    "targetGroup",
                    "userId-apidefinitionid",
                    "userId-apidefinitionid");
            verify(repositoryApi).getBranch("targetGroup/userId-apidefinitionid", "1");
            verify(repositoryApi).createBranch(project, "1", "baseBranch");
            verify(commitsApi).createCommit(
                    any(Project.class),
                    anyString(),
                    anyString(),
                    any(),
                    anyString(),
                    anyString(),
                    anyList()
            );

            verify(commitFactory).createEntityCommitAction("Name", "entity");
            verify(commitFactory).createControllerCommitAction("Name", "controller");
            verify(commitFactory).createRepositoryCommitAction("Name", "repository");
            verify(commitFactory).createOverwriteReadmeCommitActions("verbose");
        }
    }
}
