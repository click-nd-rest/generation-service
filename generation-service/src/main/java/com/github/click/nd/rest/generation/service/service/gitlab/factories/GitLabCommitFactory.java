package com.github.click.nd.rest.generation.service.service.gitlab.factories;

import java.util.Collection;
import org.gitlab4j.api.models.CommitAction;

public interface GitLabCommitFactory {
    CommitAction createEntityCommitAction(String resourceName, String entityCode);

    CommitAction createRepositoryCommitAction(String resourceName, String entityCode);

    CommitAction createControllerCommitAction(String resourceName, String entityCode);

    Collection<CommitAction> createOverwriteReadmeCommitActions(String content);
}
