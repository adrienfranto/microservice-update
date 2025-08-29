package com.techie.microservices.order_service.service;

import com.techie.microservices.order_service.client.InventoryClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryClient inventoryClient;

    @CircuitBreaker(name = "inventory", fallbackMethod = "fallback")
    @Retry(name = "inventory")
    @TimeLimiter(name = "inventory")
    public CompletableFuture<Boolean> isInStock(String skuCode, Integer quantity) {
        return CompletableFuture.supplyAsync(() -> inventoryClient.isInStock(skuCode, quantity));
    }

    public CompletableFuture<Boolean> fallback(String skuCode, Integer quantity, Throwable throwable) {
        log.warn("Fallback inventory for SKU {}: {}", skuCode, throwable.getMessage());
        return CompletableFuture.completedFuture(false);
    }
}
