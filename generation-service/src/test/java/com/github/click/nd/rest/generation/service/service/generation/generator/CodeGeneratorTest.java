package com.github.click.nd.rest.generation.service.service.generation.generator;

import static com.github.click.nd.rest.generation.service.domain.DataType.STRING;

import com.github.click.nd.rest.generation.service.service.generation.BaseCodeGeneratorTest;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CodeGeneratorTest extends BaseCodeGeneratorTest {
    @Test
    public void entityCodeGeneration() {
        var entityCode = codeGenerator.generateEntityCode(
            resource("pIE", List.of(resourceField("COOL_NAME", STRING)))
        );

        Assertions.assertThat(entityCode.trim()).isEqualTo(
            """
                package com.github.click.nd.rest.base.domain;

                import lombok.Data;
                import org.springframework.data.annotation.Id;

                @Data
                public class Pie {
                    @Id
                    private String id;
                    private String coolName;
                }"""
        );
    }

    @Test
    public void repositoryCodeGeneration() {
        var repositoryCode = codeGenerator.generateRepositoryCode(
            resource("pIE", List.of(resourceField("name", STRING)))
        );

        Assertions.assertThat(repositoryCode).isEqualTo(
            """
                package com.github.click.nd.rest.base.repository;
                                
                import com.github.click.nd.rest.base.domain.Pie;
                import org.springframework.data.mongodb.repository.MongoRepository;
                                
                import java.util.List;
                                
                public interface PieRepository extends MongoRepository<Pie, String> {
                    List<Pie> findPiesById(String id);
                    void deleteById(String id);
                    List<Pie> findPiesByName(String name);
                    void deleteByName(String name);
                }"""
        );
    }

    @Test
    public void controllerCodeGeneration() {
        var controllerCode = codeGenerator.generateControllerCode(
            resource("piE", List.of(resourceField("name", STRING)))
        );

        Assertions.assertThat(controllerCode).isEqualTo(
            """
                package com.github.click.nd.rest.base.controller;
                                
                import java.util.List;
                                
                import com.github.click.nd.rest.base.domain.Pie;
                import com.github.click.nd.rest.base.repository.PieRepository;
                import lombok.RequiredArgsConstructor;
                import org.springframework.http.HttpStatus;
                import org.springframework.validation.annotation.Validated;
                import org.springframework.web.bind.annotation.*;
                                
                @RestController
                @Validated
                @RequestMapping("/pie")
                @RequiredArgsConstructor
                public class PieController {
                    private final PieRepository repository;
                                
                    @PutMapping
                    @ResponseStatus(HttpStatus.CREATED)
                    public Pie upsert(@RequestBody Pie pie) {
                        return repository.save(pie);
                    }
                          
                                
                    @GetMapping(path = "/id/{id}")
                    @ResponseStatus(HttpStatus.OK)
                    public List<Pie> findById(@PathVariable String id) {
                        return repository.findPiesById(id);
                    }
                                
                    @DeleteMapping(path = "/id/{id}")
                    @ResponseStatus(HttpStatus.OK)
                    public void deleteById(@PathVariable String id) {
                        repository.deleteById(id);
                    }
                    
                    @GetMapping(path = "/name/{name}")
                    @ResponseStatus(HttpStatus.OK)
                    public List<Pie> findByName(@PathVariable String name) {
                        return repository.findPiesByName(name);
                    }
                                
                    @DeleteMapping(path = "/name/{name}")
                    @ResponseStatus(HttpStatus.OK)
                    public void deleteByName(@PathVariable String name) {
                        repository.deleteByName(name);
                    }
                                
                }
                """
        );
    }
}
