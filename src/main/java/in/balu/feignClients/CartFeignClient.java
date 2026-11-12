package in.balu.feignClients;
import in.balu.dto.CartItemResponseDTO;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@FeignClient(name = "cart-service", path = "/cart")
//@LoadBalancerClient
public interface CartFeignClient {

    @GetMapping("/{userId}")
    public List<CartItemResponseDTO> getCartByUserId(@PathVariable Long userId);

    @DeleteMapping("/clear/{userId}")
    public void clearUserCart(@PathVariable Long userId);

}
