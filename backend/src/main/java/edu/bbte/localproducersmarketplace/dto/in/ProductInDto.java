package edu.bbte.localproducersmarketplace.dto.in;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class ProductInDto {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.0", inclusive = false)
    private Double price;

    @NotNull(message = "Quantity/Stock is mandatory")
    @Min(value = 0, message = "Quantity must be 0 or greater")
    private Integer quantity;

    @NotNull
    private Long categoryId;
}