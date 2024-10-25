package com.eshop.my_eshop.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private CartDTO cart;
    private List<OrderDTO> orders;
}