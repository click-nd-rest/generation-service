package com.github.click.nd.rest.generation.service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class GenerateApiResponse {

    int generatedInstanceId;

    public static GenerateApiResponse of(int generatedInstanceId) {
        return new GenerateApiResponse(generatedInstanceId);
    }
}
