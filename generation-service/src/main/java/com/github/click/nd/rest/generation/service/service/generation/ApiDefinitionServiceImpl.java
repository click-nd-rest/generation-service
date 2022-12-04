package com.github.click.nd.rest.generation.service.service.generation;

import com.github.click.nd.rest.generation.service.domain.ApiDefinition;
import com.github.click.nd.rest.generation.service.domain.GenerateApiResponse;
import com.github.click.nd.rest.generation.service.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiDefinitionServiceImpl implements ApiDefinitionService {
    private final CodeGenerator codeGenerator;

    @Override
    public GenerateApiResponse generateCodeIfNeeded(ApiDefinition apiDefinition) {
        var hash = calculateHash(apiDefinition);
        //TODO: check if hash is present
        var resourceSourceCodes = codeGenerator.generateCode(apiDefinition);
        //TODO: put it in the user Gitlab repo
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
