package com.github.click.nd.rest.generation.service.service.gitlab;

import com.github.click.nd.rest.generation.service.service.generation.generator.ResourceSourceCode;
import java.util.Collection;


/**
 * Service for pushing generated source code to the user Gitlab repository on the path
 * {organization}/{user-id}-{api-definition-id}
 */
public interface GitLabService {

    boolean isDefinitionPushed(String apiDefinitionId, long hash);

    /**
     * Push generated user defined source code on the path
     * {organization}/{user-id}-{api-definition-id}. If no repository exists on the such path it
     * should be forked from the base repository.
     *
     * @param resourceSourceCodes generated code
     */
    void pushGeneratedCode(
            String apiDefinitionId,
            String apiDefinitionVerbose,
            long hash,
            Collection<ResourceSourceCode> resourceSourceCodes
    );
}
