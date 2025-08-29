package com.techie.microservices.order_service.service;

import com.techie.microservices.order_service.dto.OrderRequest;
import com.techie.microservices.order_service.event.OrderPlacedEvent;
import com.techie.microservices.order_service.model.Order;
import com.techie.microservices.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {
        try {
            boolean inStock = inventoryService
                    .isInStock(orderRequest.skuCode(), orderRequest.quantity())
                    .get(); // synchronous

            if (inStock) {
                Order order = new Order();
                order.setOrderNumber(UUID.randomUUID().toString());
                order.setPrice(orderRequest.price());
                order.setSkucode(orderRequest.skuCode());
                order.setQuantity(orderRequest.quantity());

                orderRepository.save(order);
                OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
                orderPlacedEvent.setOrderNumber(order.getOrderNumber());
                orderPlacedEvent.setEmail(orderRequest.userDetails().email());
                orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
                orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());

                log.info("Start - Sending OrderPlacedEvent {} to kafka topic order-placed",orderPlacedEvent);
                kafkaTemplate.send("order-placed",orderPlacedEvent);
                log.info("End - Sending OrderPlacedEvent {} to kafka topic order-placed",orderPlacedEvent);
            } else {
                throw new RuntimeException("Product with SKU " + orderRequest.skuCode() + " is not in stock.");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error while placing order", ex);
        }
    }
}
