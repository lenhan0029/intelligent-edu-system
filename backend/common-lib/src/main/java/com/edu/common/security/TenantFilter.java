package com.edu.common.security;

import com.edu.common.util.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String tenantIdHeader = request.getHeader("X-Tenant-ID");
        if (tenantIdHeader != null && !tenantIdHeader.isEmpty()) {
            try {
                TenantContext.setCurrentTenant(Long.parseLong(tenantIdHeader));
            } catch (NumberFormatException e) {
                // Ignore invalid tenant ID
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
