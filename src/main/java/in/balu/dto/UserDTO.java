package in.balu.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Integer userId;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String contact;

}
