package in.balu.service;
import in.balu.dto.*;
import in.balu.exceptions.ResourceNotFoundException;
import in.balu.feignClients.CartFeignClient;
import in.balu.feignClients.ProductFeignClient;
import in.balu.feignClients.UserFeignClient;
import in.balu.models.Order;
import in.balu.models.OrderItem;
import in.balu.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements  OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    private UserFeignClient userFeignClient;

    private ProductFeignClient productFeignClient;

    private CartFeignClient cartFeignClient;


    @Override
    public OrderResponseDTO placeOrder(PlaceOrderRequestDTO request) {

        UserDTO userDTO = validateUser(request.getUserId());

        if(userDTO == null) {
            throw new ResourceNotFoundException("User not found in DB");
        }

        List<CartItemResponseDTO> cartItems = fetchCartItems(request.getUserId());

        if(cartItems == null || cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        BigDecimal totalPrice = calculateTotalPrice(cartItems);

        List<OrderItem> orderItems = buildOrderItems(cartItems);

        Order order = createOrder(request, totalPrice, orderItems);

        Order dbOrder = orderRepository.save(order);

        cartFeignClient.clearUserCart(request.getUserId());

        return mapToOrder(dbOrder, userDTO);
    }

    private OrderResponseDTO mapToOrder(Order dbOrder, UserDTO userDTO) {

        OrderResponseDTO dto = new OrderResponseDTO();

        BeanUtils.copyProperties(dbOrder, dto, "items");

        dto.setOrderId(dbOrder.getId());
        dto.setUserDTO(userDTO);

        List<OrderItemResponseDTO> orderItemResponse = dbOrder.getItems().stream().map(item -> {
            OrderItemResponseDTO itemDto = new OrderItemResponseDTO();
            BeanUtils.copyProperties(item, itemDto);
            return itemDto;
        }).toList();

        dto.setItems(orderItemResponse);
        return dto;
    }

    private UserDTO validateUser(Long userId) {
        return userFeignClient.fetchUser(userId.intValue());
    }

    private List<CartItemResponseDTO> fetchCartItems(Long userId) {
        return cartFeignClient.getCartByUserId(userId);
    }

    private BigDecimal calculateTotalPrice(List<CartItemResponseDTO> cartItems) {
        BigDecimal total = BigDecimal.ZERO;

        for(CartItemResponseDTO item : cartItems) {
            ProductResponseDTO product = productFeignClient.getProduct(item.getProductId());
            Integer quantity = item.getQuantity();
            BigDecimal individualPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            total = total.add(individualPrice);
        }
        return total;
    }

    private List<OrderItem> buildOrderItems(List<CartItemResponseDTO> cartItems) {

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItemResponseDTO item : cartItems) {

            ProductResponseDTO product = productFeignClient.getProduct(item.getProductId());

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private Order createOrder(PlaceOrderRequestDTO request, BigDecimal totalPrice, List<OrderItem> orderItems) {

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setTotalPrice(totalPrice);
        order.setStatus("PLACED");

        for(OrderItem item : orderItems) {
            item.setOrder(order);
        }

        order.setItems(orderItems);

        return order;

    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {

    }

    @Override
    public List<OrderResponseDTO> getOrdersByUser(Long userId) {
        return List.of();
    }
}
