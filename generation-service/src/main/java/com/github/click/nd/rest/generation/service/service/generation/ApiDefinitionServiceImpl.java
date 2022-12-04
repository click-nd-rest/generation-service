package com.github.click.nd.rest.generation.service.service.generation;

import com.github.click.nd.rest.generation.service.domain.ApiDefinition;
import com.github.click.nd.rest.generation.service.domain.GenerateApiResponse;
import com.github.click.nd.rest.generation.service.service.generation.generator.CodeGenerator;
import com.github.click.nd.rest.generation.service.service.gitlab.GitLabService;
import com.github.click.nd.rest.generation.service.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ApiDefinitionServiceImpl implements ApiDefinitionService {

    private final CodeGenerator codeGenerator;
    private final GitLabService gitLabService;

    @Override
    public GenerateApiResponse generateCodeIfDefinitionNotPushed(ApiDefinition apiDefinition) {
        log.info(apiDefinition.toString());

        var hash = calculateHash(apiDefinition);

        var apiDefinitionId = apiDefinition.getId();
        //If API with such hash already pushed we don't need to recreate it
        if (gitLabService.isDefinitionPushed(apiDefinitionId, hash)) {
            return GenerateApiResponse.of(hash);
        }

        //If definition is not pushed code is being generated
        var resourceSourceCodes = codeGenerator.generateCode(apiDefinition);
        gitLabService.pushGeneratedCode(
            apiDefinitionId,
            apiDefinition.toString(),
            hash,
            resourceSourceCodes
        );

        return GenerateApiResponse.of(hash);
    }

    private int calculateHash(ApiDefinition apiDefinition) {
        return new HashWrapper(SecurityUtil.getUserId(), apiDefinition).hashCode();
    }

    @Value
    private static class HashWrapper {
        String userId;
        ApiDefinition apiDefinition;
    }
}
