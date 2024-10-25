package com.eshop.my_eshop.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eshop.my_eshop.exception.ResourceNotFoundException;
import com.eshop.my_eshop.model.Cart;
import com.eshop.my_eshop.response.ApiResponse;
import com.eshop.my_eshop.service.cart.ICartItemService;
import com.eshop.my_eshop.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
@CrossOrigin("http://localhost:3000")
public class CartItemController {

    private final ICartService cartService;
    private final ICartItemService cartItemService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required=false) Long cartId,
                                                   @RequestParam Long productId,
                                                   @RequestParam Integer quantity,
                                                   @RequestParam Long userId) {
        try {
            if (cartId == null) {
                Cart cart = cartService.createCart(userId);
                cartId = cart.getId();
            }
            cartItemService.addCartItem(cartId, productId, quantity);
            return ResponseEntity.ok().body(new ApiResponse("CartItem added to the cart", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId){
        try {
            cartItemService.removeCartItem(cartId, itemId);
            return ResponseEntity.ok().body(new ApiResponse("CartItem deleted from the cart", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/updateQuantity")
    public ResponseEntity<ApiResponse> updateQuantity(@PathVariable Long cartId, @PathVariable Long itemId, @RequestParam Integer quantity){
        try {
            cartItemService.updateCartItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok().body(new ApiResponse("Cart item quantity updated", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
