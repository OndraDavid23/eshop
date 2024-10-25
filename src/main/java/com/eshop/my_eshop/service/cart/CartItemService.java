package com.eshop.my_eshop.service.cart;

import org.springframework.stereotype.Service;

import com.eshop.my_eshop.exception.ResourceNotFoundException;
import com.eshop.my_eshop.model.Cart;
import com.eshop.my_eshop.model.CartItem;
import com.eshop.my_eshop.model.Product;
import com.eshop.my_eshop.repository.CartRepository;
import com.eshop.my_eshop.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;

    @Override
    public void addCartItem(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item->item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(new CartItem());
        if(cartItem.getId() == null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnit_price(product.getPrice());
        }
        else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartRepository.save(cart);

    }

    @Override
    public void removeCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = getCartItem(cart, productId);
        cart.removeCartItem(cartItem);
        cart.updateTotalAmount();
        cartRepository.save(cart);
    }

    @Override
    public void updateCartItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = getCartItem(cart, productId);

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice();
        cart.updateTotalAmount();
        cartRepository.save(cart);

        cartRepository.save(cart);
    }

    public CartItem getCartItem(Cart cart, Long productId) {
        return cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));
    }
}
