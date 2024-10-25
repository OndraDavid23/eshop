package com.eshop.my_eshop.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshop.my_eshop.dto.OrderDTO;
import com.eshop.my_eshop.exception.ResourceNotFoundException;
import com.eshop.my_eshop.response.ApiResponse;
import com.eshop.my_eshop.service.order.IOrderService;

import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try {
            OrderDTO order = orderService.getOrder(orderId);
            return ResponseEntity.ok().body(new ApiResponse("Order found", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Order found", e.getMessage()));
        }
    }

    @PostMapping("/{userId}/order")
    public ResponseEntity<ApiResponse> createOrder(@PathVariable Long userId) {
        try {
            OrderDTO order = orderService.placeOrder(userId);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Order created", order));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Order not created", e.getMessage()));
        }
    }
    
    
}
