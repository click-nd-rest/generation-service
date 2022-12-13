package com.github.click.nd.rest.generation.service.service.generation;

import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.github.click.nd.rest.generation.service.domain.ApiDefinition;
import com.github.click.nd.rest.generation.service.domain.DataType;
import com.github.click.nd.rest.generation.service.domain.Resource;
import com.github.click.nd.rest.generation.service.domain.ResourceField;
import com.github.click.nd.rest.generation.service.service.generation.generator.CodeGenerator;
import com.github.click.nd.rest.generation.service.service.gitlab.GitLabService;
import com.github.click.nd.rest.generation.service.util.SecurityUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class ApiDefinitionServiceImplTest extends BaseCodeGeneratorTest {

    private final CodeGenerator codeGenerator = mock(CodeGenerator.class);
    private final GitLabService gitLabService = mock(GitLabService.class);
    private final ApiDefinitionService apiDefinitionService
            = new ApiDefinitionServiceImpl(codeGenerator, gitLabService);

    @Test
    public void successfullyGenerateCodeWhenDefinitionNotPushed() {
        try (MockedStatic<SecurityUtil> utilMock = mockStatic(SecurityUtil.class)) {
            Collection<ResourceField> fields = List.of(super.resourceField("FieldName", DataType.STRING));
            Collection<Resource> resources = List.of(super.rawResource("ResourceName", fields));
            ApiDefinition apiDefinition = super.apiDefinition("apiDefinitionId", resources);

            utilMock.when(SecurityUtil::getUserId).thenReturn("userId");
            when(gitLabService.isDefinitionPushed(anyString(), anyLong())).thenReturn(false);
            when(codeGenerator.generateCode(apiDefinition)).thenReturn(new ArrayList<>());

            apiDefinitionService.generateCodeIfDefinitionNotPushed(apiDefinition);

            verify(gitLabService).isDefinitionPushed(anyString(), anyLong());
            verify(codeGenerator).generateCode(apiDefinition);
            verify(gitLabService).pushGeneratedCode(anyString(), anyString(), anyLong(), anyCollection());
        }
    }

    @Test
    public void skipGeneratingCodeWhenDefinitionHasAlreadyBeenPushed() {
        try (MockedStatic<SecurityUtil> utilMock = mockStatic(SecurityUtil.class)) {
            Collection<ResourceField> fields = List.of(super.resourceField("FieldName", DataType.STRING));
            Collection<Resource> resources = List.of(super.rawResource("ResourceName", fields));
            ApiDefinition apiDefinition = super.apiDefinition("apiDefinitionId", resources);

            utilMock.when(SecurityUtil::getUserId).thenReturn("userId");

            when(gitLabService.isDefinitionPushed(anyString(), anyLong())).thenReturn(true);

            apiDefinitionService.generateCodeIfDefinitionNotPushed(apiDefinition);

            verifyNoInteractions(codeGenerator);
            verify(gitLabService, times(0)).pushGeneratedCode(
                    anyString(),
                    anyString(),
                    anyLong(),
                    anyCollection());
        }
    }
}
