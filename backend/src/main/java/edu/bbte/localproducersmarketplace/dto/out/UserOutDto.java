package edu.bbte.localproducersmarketplace.dto.out;

import edu.bbte.localproducersmarketplace.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOutDto {
    private Long id;

    private String email;

    private String name;

    private Role role;
}
