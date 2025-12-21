package com.vucko.apigateway.security;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable()) // Isključujemo CSRF za API Gateway
                .authorizeExchange(exchanges -> exchanges
                        // Dozvoli preflight OPTIONS za sve
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        // Dozvoli sve ostale zahteve (ili možeš dodati specifične rute)
                        .anyExchange().permitAll()
                );

        return http.build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
                "http://localhost:7155",
                "http://localhost:5173"
        ));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

    @Bean
    public GlobalFilter corsGlobalFilter() {
        return (exchange, chain) -> {
            exchange.getResponse().beforeCommit(() -> {
                exchange.getResponse().getHeaders().set("Access-Control-Allow-Origin", "http://localhost:7155");
                exchange.getResponse().getHeaders().set("Access-Control-Allow-Credentials", "true");
                return chain.filter(exchange).then();
            });
            return chain.filter(exchange);
        };
    }


}
