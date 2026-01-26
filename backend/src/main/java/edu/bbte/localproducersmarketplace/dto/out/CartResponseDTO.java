package edu.bbte.localproducersmarketplace.dto.out;

import lombok.Data;

import java.util.List;

@Data
public class CartResponseDTO {

    private Long id;

    private Long userId;

    private List<CartItemResponseDTO> items;
}

