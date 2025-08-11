package com.fs.api_gateway.filters;

import com.fs.api_gateway.interfaces.AuthFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;

@Component
public class JwtAuthGlobalFilter implements GlobalFilter {

    @Autowired
    private AuthFeignClient authFeignClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange);
    }
    public Mono<Void> filter2(ServerWebExchange exchange, GatewayFilterChain chain) {


        String path = exchange.getRequest().getURI().getPath();

        // Skip JWT validation for login and signup
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/signup")) {
            return chain.filter(exchange);
        }
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            return Mono.fromCallable(() -> authFeignClient.validateToken(Map.of("token", token)))
                    .subscribeOn(Schedulers.boundedElastic()) // runs Feign in blocking thread pool
                    .flatMap(userDetails -> {
                        // Inject user details into request headers for downstream services
                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(r -> r.headers(h -> {
                                    h.set("X-User-Name", (String) userDetails.get("username"));
                                    List<String> roles = (List<String>) userDetails.get("roles");
                                    h.set("X-User-Roles", String.join(",", roles));
                                }))
                                .build();

                        return chain.filter(mutatedExchange);

                    })
                    .onErrorResume(e -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        }

        // Reject if no token
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
