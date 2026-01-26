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

    @NotNull
    private Long categoryId;
}