package com.github.click.nd.rest.generation.service.service.generation.generator;

import com.github.click.nd.rest.generation.service.domain.DataType;
import com.github.click.nd.rest.generation.service.service.generation.BaseCodeGeneratorTest;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class CodeGenerationLocalTest extends BaseCodeGeneratorTest {
    @Test
    @Disabled
    public void generateCodeLocally() {
        var sourceCode = codeGenerator.generateCode(apiDefinition(
                "",
                List.of(rawResource("flower", List.of(resourceField("color", DataType.STRING))))
        ));

        sourceCode.forEach(this::writeAsFile);
    }

    public void writeAsFile(ResourceSourceCode resourceSourceCode) {
        var resourceNameUpperCamel = resourceSourceCode.getResourceNameUpperCamel();
        writeFile(resourceNameUpperCamel + ".java", resourceSourceCode.entityCode());
        writeFile(resourceNameUpperCamel + "Repository.java", resourceSourceCode.repositoryCode());
        writeFile(resourceNameUpperCamel + "Controller.java", resourceSourceCode.controllerCode());
    }

    private void writeFile(String fileName, String stringToWrite) {
        try (var writer = new FileWriter(fileName)) {
            writer.write(stringToWrite);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
