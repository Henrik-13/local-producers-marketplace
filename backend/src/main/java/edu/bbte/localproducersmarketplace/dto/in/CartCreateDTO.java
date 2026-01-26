package edu.bbte.localproducersmarketplace.dto.in;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CartCreateDTO {

    @NotEmpty(message = "Cart must contain at least one item.")
    @Valid
    private List<CartItemCreateDTO> items;
}

