package edu.bbte.localproducersmarketplace.dto.in;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}

