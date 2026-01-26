package edu.bbte.localproducersmarketplace.mapper;

import edu.bbte.localproducersmarketplace.dto.in.CartItemCreateDTO;
import edu.bbte.localproducersmarketplace.dto.out.CartItemResponseDTO;
import edu.bbte.localproducersmarketplace.dto.out.CartResponseDTO;
import edu.bbte.localproducersmarketplace.model.Cart;
import edu.bbte.localproducersmarketplace.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "user.id", target = "userId")
    CartResponseDTO toResponse(Cart cart);

    @Mapping(source = "product.id", target = "productId")
    CartItemResponseDTO toResponse(CartItem item);

    List<CartItemResponseDTO> toResponseList(List<CartItem> items);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "cart", ignore = true)
    CartItem toEntity(CartItemCreateDTO dto);

    List<CartItem> toEntityList(List<CartItemCreateDTO> dtoList);
}

