package com.eshop.my_eshop.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.eshop.my_eshop.dto.OrderDTO;
import com.eshop.my_eshop.dto.OrderItemDTO;
import com.eshop.my_eshop.dto.ProductDTO;
import com.eshop.my_eshop.enums.OrderStatus;
import com.eshop.my_eshop.exception.ResourceNotFoundException;
import com.eshop.my_eshop.model.Cart;
import com.eshop.my_eshop.model.Order;
import com.eshop.my_eshop.model.OrderItem;
import com.eshop.my_eshop.model.Product;
import com.eshop.my_eshop.repository.CartRepository;
import com.eshop.my_eshop.repository.OrderRepository;
import com.eshop.my_eshop.repository.ProductRepository;
import com.eshop.my_eshop.service.cart.CartService;
import com.eshop.my_eshop.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final ProductService productService;

    @Override
    public OrderDTO placeOrder(Long userId) {
    Cart cart = cartRepository.findByUserId(userId);
    if(cart == null){
        throw new ResourceNotFoundException("Cart not found for user id: " + userId);
    }
    Order order = createOrder(cart);
    List<OrderItem> orderItems = createOrderItem(order, cart);
    order.setItems(new HashSet<>(orderItems));
    order.setOrderTotalAmount(calculateTotalPrice(orderItems));
    Order savedOrder = orderRepository.save(order);
    OrderDTO orderDTO = createOrderDTO(savedOrder);
    cartService.clearCart(cart.getId());

    return orderDTO;
    }

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    
    @Override
    public OrderDTO getOrder(Long id) {
        return orderRepository.findById(id).map(this::createOrderDTO).orElseThrow(()->new ResourceNotFoundException("Order not found"));
    }
    
    @Override
    public BigDecimal getOrderTotalPrice(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order not found."));
        return order.getOrderTotalAmount();
    }
    
    @Override
    public void clearOrder() {}
    
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    private List<OrderItem> createOrderItem(Order order, Cart cart){
        return cart.getCartItems().stream().map(item -> {
            Product product = item.getProduct();
            product.setInventory(product.getInventory() - item.getQuantity());
            productRepository.save(product);
            return new OrderItem(product,
                    order,
                    item.getQuantity(),
                    item.getUnit_price());
        }).toList();

    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItems){
            return orderItems.stream().map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        }

    public OrderDTO createOrderDTO(Order order){
        OrderDTO orderDTO = new OrderDTO();
        Set<OrderItem> orderItems = order.getItems();
        orderDTO.setId(order.getId());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setOrderStatus(order.getOrderStatus());
        Set<OrderItemDTO> orderItemDTOs = orderItems.stream().map(this::createOrderItemDTO).collect(Collectors.toSet());
        orderDTO.setOrderItems(orderItemDTOs);
        return orderDTO;
    }

    public OrderItemDTO createOrderItemDTO(OrderItem orderItem){
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setPrice(orderItem.getPrice());
        ProductDTO productDTO = productService.createProductDTO(orderItem.getProduct());
        orderItemDTO.setProduct(productDTO);
        return orderItemDTO;
    }

}
