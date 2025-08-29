package com.techie.microservices.api_gateway.routes;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Routes {

    @Bean
    public RouteLocator productServiceRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product_service", r -> r
                        .path("/api/product/**")
                        .filters(f -> f.circuitBreaker(config -> config
                                .setName("productServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallback/product")))
                        .uri("http://localhost:8080"))
                .build();
    }

    @Bean
    public RouteLocator productServiceSwaggerRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service-swagger", r -> r
                        .path("/aggregate/product-service/v3/api-docs")
                        .filters(f -> f.rewritePath(
                                "/aggregate/product-service/v3/api-docs",
                                "/v3/api-docs"))
                        .uri("http://localhost:8080"))
                .build();
    }

    @Bean
    public RouteLocator orderServiceRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("order_service", r -> r
                        .path("/api/order/**")
                        .filters(f -> f.circuitBreaker(config -> config
                                .setName("orderServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallback/order")))
                        .uri("http://localhost:8081"))
                .build();
    }

    @Bean
    public RouteLocator orderServiceSwaggerRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("order-service-swagger", r -> r
                        .path("/aggregate/order-service/v3/api-docs")
                        .filters(f -> f.rewritePath(
                                "/aggregate/order-service/v3/api-docs",
                                "/v3/api-docs"))
                        .uri("http://localhost:8081"))
                .build();
    }

    @Bean
    public RouteLocator inventoryServiceRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("inventory_service", r -> r
                        .path("/api/inventory/**")
                        .filters(f -> f.circuitBreaker(config -> config
                                .setName("inventoryServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallback/inventory")))
                        .uri("http://localhost:8082"))
                .build();
    }

    @Bean
    public RouteLocator inventoryServiceSwaggerRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("inventory-service-swagger", r -> r
                        .path("/aggregate/inventory-service/v3/api-docs")
                        .filters(f -> f.rewritePath(
                                "/aggregate/inventory-service/v3/api-docs",
                                "/v3/api-docs"))
                        .uri("http://localhost:8082"))
                .build();
    }
}
