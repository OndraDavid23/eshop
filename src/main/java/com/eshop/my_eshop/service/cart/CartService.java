package com.eshop.my_eshop.service.cart;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.eshop.my_eshop.dto.CartDTO;
import com.eshop.my_eshop.dto.CartItemDTO;
import com.eshop.my_eshop.dto.ProductDTO;
import com.eshop.my_eshop.exception.ResourceNotFoundException;
import com.eshop.my_eshop.model.Cart;
import com.eshop.my_eshop.model.CartItem;
import com.eshop.my_eshop.model.Product;
import com.eshop.my_eshop.model.User;
import com.eshop.my_eshop.repository.CartItemRepository;
import com.eshop.my_eshop.repository.CartRepository;
import com.eshop.my_eshop.repository.UserRepository;
import com.eshop.my_eshop.service.product.ProductService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    public CartDTO getCartDTO(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Cart not found"));
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            throw new ResourceNotFoundException("Cart not found");
        }
        CartDTO cartDTO = createCartDTO(cart);
        return cartDTO;
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        // User user = cart.getUser();
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);
        // user.setCart(null);

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart createCart(Long userId){
        Cart cart = new Cart();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    public CartItemDTO createCartItemDTO(CartItem cartItem){
        CartItemDTO cartItemDTO = new CartItemDTO();
        Product product = cartItem.getProduct();
        ProductDTO productDTO = productService.createProductDTO(product);
        cartItemDTO.setId(cartItem.getId());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setUnitPrice(cartItem.getUnit_price());
        cartItemDTO.setTotalPrice(cartItem.getTotal_price());
        cartItemDTO.setProduct(productDTO);
        return cartItemDTO;
    }

    public CartDTO createCartDTO(Cart cart){
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setTotalAmount(cart.getTotalAmount());
        Set<CartItem> cartItems = cart.getCartItems();
        Set<CartItemDTO> cartItemDTOs = cartItems.stream().map(this::createCartItemDTO).collect(Collectors.toSet());
        cartDTO.setCartItems(cartItemDTOs);
        return cartDTO;
                        
    }
}
