package in.balu.dto;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long orderId;

    private Long userId;

    private BigDecimal totalPrice;

    private String status;

    private List<OrderItemResponseDTO> items;

    private UserDTO userDTO;

}
