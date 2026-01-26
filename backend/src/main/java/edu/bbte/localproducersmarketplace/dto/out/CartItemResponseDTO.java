package edu.bbte.localproducersmarketplace.dto.out;

import lombok.Data;

@Data
public class CartItemResponseDTO {

    private Long productId;

    private int quantity;

    private double unitPrice;
}

