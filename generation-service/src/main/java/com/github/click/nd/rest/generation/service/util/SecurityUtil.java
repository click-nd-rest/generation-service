package com.github.click.nd.rest.generation.service.util;

import com.github.click.nd.rest.generation.service.security.SecurityPrincipal;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtil {
    public String getUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return ((SecurityPrincipal) auth.getPrincipal()).userId();
    }
}
