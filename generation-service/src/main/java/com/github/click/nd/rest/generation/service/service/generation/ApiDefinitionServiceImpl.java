package com.github.click.nd.rest.generation.service.service.generation;

import com.github.click.nd.rest.generation.service.domain.ApiDefinition;
import com.github.click.nd.rest.generation.service.domain.GenerateApiResponse;
import com.github.click.nd.rest.generation.service.service.generation.generator.CodeGenerator;
import com.github.click.nd.rest.generation.service.service.gitlab.GitLabService;
import com.github.click.nd.rest.generation.service.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiDefinitionServiceImpl implements ApiDefinitionService {
    private final CodeGenerator codeGenerator;
    private final GitLabService gitlabService;

    @Override
    public GenerateApiResponse generateCodeIfNeeded(ApiDefinition apiDefinition) {
        var hash = calculateHash(apiDefinition);

        var apiDefinitionId = apiDefinition.getId();
        //If API with such hash already pushed we don't need to recreate it
        if (gitlabService.isDefinitionPushed(apiDefinitionId, hash)) {
            return GenerateApiResponse.of(hash);
        }

        //If definition is not pushed code is being generated
        var resourceSourceCodes = codeGenerator.generateCode(apiDefinition);
        gitlabService.pushGeneratedCode(apiDefinitionId, hash, resourceSourceCodes);

        return GenerateApiResponse.of(hash);
    }

    private int calculateHash(ApiDefinition apiDefinition) {
        return new HashWrapper(SecurityUtils.getUserId(), apiDefinition).hashCode();
    }

    @Value
    private static class HashWrapper {
        String userId;
        ApiDefinition apiDefinition;
    }
}
