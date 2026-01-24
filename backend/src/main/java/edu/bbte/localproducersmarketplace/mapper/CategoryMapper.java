package edu.bbte.localproducersmarketplace.mapper;

import edu.bbte.localproducersmarketplace.dto.in.CategoryInDto;
import edu.bbte.localproducersmarketplace.dto.out.CategoryOutDto;
import edu.bbte.localproducersmarketplace.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryInDto dto);

    CategoryOutDto toDto(Category category);

    List<CategoryOutDto> toDtoList(List<Category> categories);
}