package edu.bbte.localproducersmarketplace.dto.in;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDTO {

    @NotEmpty(message = "Order must contain at least one item.")
    @Valid
    private List<OrderItemCreateDTO> items;
}
