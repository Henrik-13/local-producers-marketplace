package edu.bbte.localproducersmarketplace.mapper;

import edu.bbte.localproducersmarketplace.dto.in.OrderCreateDTO;
import edu.bbte.localproducersmarketplace.dto.in.OrderItemCreateDTO;
import edu.bbte.localproducersmarketplace.dto.out.OrderItemResponseDTO;
import edu.bbte.localproducersmarketplace.dto.out.OrderResponseDTO;
import edu.bbte.localproducersmarketplace.model.Order;
import edu.bbte.localproducersmarketplace.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "customer.id", target = "customerId")
    OrderResponseDTO toResponse(Order order);

    @Mapping(source = "product.id", target = "productId")
    OrderItemResponseDTO toResponse(OrderItem item);

    List<OrderResponseDTO> toResponseList(List<Order> orders);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderItem toEntity(OrderItemCreateDTO dto);

    List<OrderItem> toEntityList(List<OrderItemCreateDTO> dtoList);
}
