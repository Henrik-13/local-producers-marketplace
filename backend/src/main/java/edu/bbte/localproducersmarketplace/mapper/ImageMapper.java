package edu.bbte.localproducersmarketplace.mapper;

import edu.bbte.localproducersmarketplace.dto.out.ImageOutDto;
import edu.bbte.localproducersmarketplace.model.Image;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageOutDto toDto(Image image);

    List<ImageOutDto> toDtoList(List<Image> images);
}
