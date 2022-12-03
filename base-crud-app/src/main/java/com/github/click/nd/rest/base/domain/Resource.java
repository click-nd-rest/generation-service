package com.github.click.nd.rest.base.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Resource {
    @Id
    private String name;
}
