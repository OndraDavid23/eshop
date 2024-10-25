package com.eshop.my_eshop.service.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eshop.my_eshop.dto.CartDTO;
import com.eshop.my_eshop.dto.OrderDTO;
import com.eshop.my_eshop.dto.UserDTO;
import com.eshop.my_eshop.exception.AlreadyExistsException;
import com.eshop.my_eshop.exception.ResourceNotFoundException;
import com.eshop.my_eshop.model.User;
import com.eshop.my_eshop.repository.UserRepository;
import com.eshop.my_eshop.request.CreateUserRequest;
import com.eshop.my_eshop.request.UpdateUserRequest;
import com.eshop.my_eshop.service.cart.CartService;
import com.eshop.my_eshop.service.order.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderService orderService;

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        UserDTO userDTO = createUserDTO(user);
        return userDTO;
    }

    @Override
    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException(request.getEmail() + " already exists");
        }
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        User savedUser = userRepository.save(user);
        UserDTO userDTO = createUserDTO(savedUser);
        return userDTO;
    }

    @Override
    public UserDTO updateUser(UpdateUserRequest request, Long userId) {
        User updateUser =  userRepository.findById(userId)
                .map(user->{
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(()->new ResourceNotFoundException("User not found"));
        UserDTO userDTO = createUserDTO(updateUser);
        return userDTO;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, ()->{
            throw new ResourceNotFoundException("User not found");
        });

    }

    public UserDTO createUserDTO(User user){
        CartDTO cartDTO = null;
        List<OrderDTO> orderDTOs = new ArrayList<>();
        if(user.getCart() != null){
            cartDTO = cartService.createCartDTO(user.getCart());
        } 
        if(user.getOrder() != null){
            orderDTOs = user.getOrder().stream().map(orderService::createOrderDTO).toList();
        }
        UserDTO userDTO = new UserDTO(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            cartDTO,
            orderDTOs
        );
        return userDTO;
        
    }
}
