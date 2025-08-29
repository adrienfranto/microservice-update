package com.techie.microservices.inventory_service.controller;

import com.techie.microservices.inventory_service.dto.InventoryRequest;
import com.techie.microservices.inventory_service.model.Inventory;
import com.techie.microservices.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Inventory create(@RequestBody InventoryRequest request) {
        return inventoryService.saveInventory(request);
    }

    @GetMapping("/check")
    public List<Inventory> getAll() {
        return inventoryService.getAllInventories();
    }

    @PutMapping("/{id}")
    public Inventory update(@PathVariable Long id, @RequestBody InventoryRequest request) {
        return inventoryService.updateInventory(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
    }
}
