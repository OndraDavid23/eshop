package com.eshop.my_eshop.controller;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshop.my_eshop.dto.CartDTO;
import com.eshop.my_eshop.exception.ResourceNotFoundException;
import com.eshop.my_eshop.response.ApiResponse;
import com.eshop.my_eshop.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cart")
@CrossOrigin("http://localhost:3000")
public class CartController {

    private final ICartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long userId){
        try {
            CartDTO cart = cartService.getCartDTO(userId);
            return ResponseEntity.ok().body(new ApiResponse("Cart found", cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Cart not found", null));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long id){
        try {
            cartService.clearCart(id);
            return ResponseEntity.ok().body(new ApiResponse("Cart deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Cart not found", null));
        }
    }
    @GetMapping("/{id}/total-price")
    public ResponseEntity<ApiResponse> getTotalPrice(@PathVariable Long id){
        try {
            BigDecimal totalPrice = cartService.getTotalPrice(id);
            return ResponseEntity.ok().body(new ApiResponse("Cart price ", totalPrice));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Cart not found", null));
        }
    }
}
