package edu.bbte.localproducersmarketplace.dto.in;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemCreateDTO {

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    @NotNull(message = "Quantity cannot be null")
    private int quantity;

    @Min(value = 0, message = "Unit price must be positive")
    @NotNull(message = "Unit price cannot be null")
    private Double unitPrice;
}

