package edu.bbte.localproducersmarketplace.dto.in;

import edu.bbte.localproducersmarketplace.model.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInDto {
    @NotEmpty
    private String email;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;

    @NotEmpty
    private String role;
}
