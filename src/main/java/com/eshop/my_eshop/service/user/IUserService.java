package com.eshop.my_eshop.service.user;

import com.eshop.my_eshop.dto.UserDTO;
import com.eshop.my_eshop.request.CreateUserRequest;
import com.eshop.my_eshop.request.UpdateUserRequest;

public interface IUserService {
    UserDTO getUserById(Long userId);
    UserDTO createUser(CreateUserRequest request);
    UserDTO updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);

}
