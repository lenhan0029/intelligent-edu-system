package com.edu.common.security;

import com.edu.common.util.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InternalHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String userId = request.getHeader("X-User-ID");
        String tenantId = request.getHeader("X-Tenant-ID");
        String rolesStr = request.getHeader("X-User-Roles");

        if (tenantId != null) {
            try {
                TenantContext.setCurrentTenant(Long.parseLong(tenantId));
            } catch (NumberFormatException e) {
                // Ignore invalid tenant ID
            }
        }

        if (userId != null) {
            List<SimpleGrantedAuthority> authorities = Collections.emptyList();
            if (rolesStr != null && !rolesStr.isEmpty()) {
                authorities = Arrays.stream(rolesStr.split(","))
                        .map(r -> new SimpleGrantedAuthority(r.trim()))
                        .collect(Collectors.toList());
            }
            
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
