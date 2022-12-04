package com.github.click.nd.rest.generation.service.service.gitlab.factories;

import java.util.Collection;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.models.CommitAction;
import org.gitlab4j.api.models.CommitAction.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GitLabCommitFactoryImpl implements GitLabCommitFactory {
    private final GitLabPathFactory pathFactory;

    private CommitAction createCommitAction(String filePath, String content) {
        return new CommitAction()
            .withAction(Action.CREATE)
            .withFilePath(filePath)
            .withContent(content);
    }

    private CommitAction deleteCommitAction(String filePath) {
        return new CommitAction()
            .withAction(Action.DELETE)
            .withFilePath(filePath);
    }

    @Override
    public CommitAction createEntityCommitAction(String resourceName, String entityCode) {
        return createCommitAction(pathFactory.getEntityPath(resourceName), entityCode);
    }

    @Override
    public CommitAction createRepositoryCommitAction(String resourceName, String repositoryCode) {
        return createCommitAction(pathFactory.getRepositoryPath(resourceName), repositoryCode);
    }

    @Override
    public CommitAction createControllerCommitAction(String resourceName, String controllerCode) {
        return createCommitAction(pathFactory.getControllerPath(resourceName), controllerCode);
    }

    @Override
    public Collection<CommitAction> createOverwriteReadmeCommitActions(String verbose) {
        var removeCommit = deleteCommitAction(pathFactory.getReadmePath());
        var createCommit = createCommitAction(
            pathFactory.getReadmePath(),
            createVerboseReadme(verbose)
        );
        return List.of(removeCommit, createCommit);
    }

    private String createVerboseReadme(String verbose) {
        return """
            # Generated user defined service
                        
            Source code of the generator: https://github.com/click-nd-rest/
                        
            ## Debug information
                        
            """ + verbose;
    }
}
