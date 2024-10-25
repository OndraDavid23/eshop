package com.eshop.my_eshop.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
}
