package com.github.click.nd.rest.generation.service.security;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

public class UserIdAuthenticationFilter implements Filter {

    private static final String USER_ID_HEADER_NAME = "userId";
    private static final String USER_ROLE = "user";

    @Override
    public void doFilter(ServletRequest rawRequest, ServletResponse rawResponse, FilterChain chain)
            throws IOException, ServletException {
        var req = (HttpServletRequest) rawRequest;
        var resp = (HttpServletResponse) rawResponse;

        String userId = req.getHeader(USER_ID_HEADER_NAME);
        if (StringUtils.hasText(userId)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                SecurityPrincipal principal = new SecurityPrincipal(userId);
                GrantedAuthority authority = new SimpleGrantedAuthority(USER_ROLE);
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                new AnonymousAuthenticationToken(userId, principal, List.of(authority)));
            }
        } else {
            throw new NoSuchElementException("User id wasn't specified in the appropriate header");
        }

        chain.doFilter(req, resp);
    }
}
