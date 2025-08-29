package com.techie.microservices.order_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record OrderRequest(
        String orderNumber,
        @JsonProperty("skuCode") String skuCode,
        @JsonProperty("price") BigDecimal price,
        @JsonProperty("quantity") Integer quantity,
        @JsonProperty("userDetails") UserDetails userDetails
) {
    public record UserDetails(String email, String firstName, String lastName) {}
}