package edu.bbte.localproducersmarketplace.dto.out;

import edu.bbte.localproducersmarketplace.model.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponseDTO {

    private Long id;

    private Long customerId;

    private double totalPrice;

    private OrderStatus status;

    private List<OrderItemResponseDTO> items;
}
