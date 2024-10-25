package com.eshop.my_eshop.dto;

import java.time.LocalDate;
import java.util.Set;

import com.eshop.my_eshop.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private LocalDate orderDate;
    private OrderStatus orderStatus;
    private Set<OrderItemDTO> orderItems;
    
}
