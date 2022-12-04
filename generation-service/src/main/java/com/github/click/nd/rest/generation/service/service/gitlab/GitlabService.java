package com.github.click.nd.rest.generation.service.service.gitlab;

import java.util.Collection;

import com.github.click.nd.rest.generation.service.service.generation.generator.ResourceSourceCode;


/**
 * Service for pushing generated source code to the user Gitlab repository on the path
 *  {organization}/{user-id}-{api-definition-id}
 */
public interface GitlabService {
    boolean isDefinitionPushed(String apiDefinitionId, int hash);

    /**
     * Push generated user defined source code on the path {organization}/{user-id}-{api-definition-id}.
     * If no repository exists on the such path it should be forked from the base repository.
     * @param resourceSourceCodes generated code
     */
    void pushGeneratedCode(String apiDefinitionId, int hash, Collection<ResourceSourceCode> resourceSourceCodes);
}
