package com.techie.microservices.inventory_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequest {
    private Long id;
    private String skuCode;
    private Integer quantity;
}
