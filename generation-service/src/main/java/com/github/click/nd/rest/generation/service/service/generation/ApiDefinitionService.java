package com.github.click.nd.rest.generation.service.service.generation;

import com.github.click.nd.rest.generation.service.domain.ApiDefinition;
import com.github.click.nd.rest.generation.service.domain.GenerateApiResponse;

/**
 * Service for generating and deploy API. API is being generated only if some changes in API is
 * made: it's being guaranteed by calculating hashcode from the API definition. Hashcode is getting
 * from: - API definition: definition itself and API id - user id
 */
public interface ApiDefinitionService {

    /**
     * @param apiDefinition user-defined API definition
     * @return hashCode of API definition and user id to avoid collisions between users.
     */
    GenerateApiResponse generateCodeIfDefinitionNotPushed(ApiDefinition apiDefinition);
}
