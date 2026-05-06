package com.edu.gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${app.jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        
        // Skip auth for login/register/swagger
        if (path.contains("/api/auth/") || path.contains("/swagger-ui") || path.contains("/v3/api-docs") || path.contains("/oauth2/")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            Object orgIdObj = claims.get("organizationId");
            String orgId = orgIdObj != null ? orgIdObj.toString() : "";
            
            Object rolesObj = claims.get("roles");
            String roles = "";
            if (rolesObj instanceof java.util.Collection) {
                roles = ((java.util.Collection<?>) rolesObj).stream().map(Object::toString).collect(java.util.stream.Collectors.joining(","));
            } else if (rolesObj != null) {
                roles = rolesObj.toString().replace("[", "").replace("]", "");
            }

            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("X-User-ID", userId)
                    .header("X-Tenant-ID", orgId)
                    .header("X-User-Roles", roles)
                    .build();

            return chain.filter(exchange.mutate().request(request).build());

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
