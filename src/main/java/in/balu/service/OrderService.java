package in.balu.service;

import in.balu.dto.OrderResponseDTO;
import in.balu.dto.PlaceOrderRequestDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO placeOrder(PlaceOrderRequestDTO request);

    void updateOrderStatus(Long orderId, String status);

    List<OrderResponseDTO> getOrdersByUser(Long userId);

}
