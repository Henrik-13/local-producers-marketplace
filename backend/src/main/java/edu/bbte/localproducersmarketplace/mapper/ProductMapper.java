package edu.bbte.localproducersmarketplace.mapper;

import edu.bbte.localproducersmarketplace.dto.in.ProductInDto;
import edu.bbte.localproducersmarketplace.dto.out.ProductOutDto;
import edu.bbte.localproducersmarketplace.model.Image;
import edu.bbte.localproducersmarketplace.model.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface ProductMapper {
    ProductOutDto toDto(Product product);

    List<ProductOutDto> toDtoList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "producer", ignore = true)
    Product toEntity(ProductInDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    void updateEntityFromDto(ProductInDto dto, @MappingTarget Product product);

    default List<String> mapImagesToUrls(List<Image> images) {
        if (images == null) return List.of();
        return images.stream()
                .map(Image::getName)
                .toList();
    }
}
