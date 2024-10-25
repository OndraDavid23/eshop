package com.eshop.my_eshop.service.order;

import java.math.BigDecimal;

import com.eshop.my_eshop.dto.OrderDTO;
import com.eshop.my_eshop.model.Cart;
import com.eshop.my_eshop.model.Order;

public interface IOrderService {
    OrderDTO placeOrder(Long userId);
    Order createOrder(Cart cart);
    OrderDTO getOrder(Long id);
    BigDecimal getOrderTotalPrice(Long id);
    void clearOrder();

}
