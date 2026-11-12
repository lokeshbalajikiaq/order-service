package in.balu.feignClients;

import in.balu.dto.UserDTO;
import jakarta.ws.rs.Path;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
//@LoadBalancerClient
public interface UserFeignClient {

    @GetMapping("/api/users/{userId}")
    public UserDTO fetchUser(@PathVariable("userId") Integer userId);

}