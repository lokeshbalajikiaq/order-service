package in.balu.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponseDTO {

    private Long id;

    private Long userId;

    private Long productId;

    private Integer quantity;

}
