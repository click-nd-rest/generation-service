package com.github.click.nd.rest.generation.service.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserIdAuthenticationFilter implements Filter {

    private static final String USER_ID_HEADER_NAME = "userId";
    private static final String USER_ROLE = "user";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String userId = req.getHeader(USER_ID_HEADER_NAME);
        if (StringUtils.hasText(userId)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                SecurityPrincipal principal = new SecurityPrincipal(userId);
                GrantedAuthority authority = new SimpleGrantedAuthority(USER_ROLE);
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new AnonymousAuthenticationToken(userId, principal, List.of(authority)));
            }
        }

        chain.doFilter(req, resp);
    }
}
