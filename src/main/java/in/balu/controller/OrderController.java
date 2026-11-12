package in.balu.controller;

import in.balu.dto.OrderResponseDTO;
import in.balu.dto.PlaceOrderRequestDTO;
import in.balu.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public OrderResponseDTO placeOrder(@RequestBody PlaceOrderRequestDTO request) {

        log.info("OrderController placeOrder {}",request.getUserId());

        return orderService.placeOrder(request);
    }
}
