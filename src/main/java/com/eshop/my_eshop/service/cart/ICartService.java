package com.eshop.my_eshop.service.cart;

import java.math.BigDecimal;

import com.eshop.my_eshop.dto.CartDTO;
import com.eshop.my_eshop.model.Cart;

public interface ICartService {
    Cart getCart(Long id);
    CartDTO getCartDTO(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Cart createCart(Long userId);
}
