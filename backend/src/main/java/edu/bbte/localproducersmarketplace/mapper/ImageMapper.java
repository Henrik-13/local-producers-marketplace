package edu.bbte.localproducersmarketplace.mapper;

import edu.bbte.localproducersmarketplace.dto.out.ImageOutDto;
import edu.bbte.localproducersmarketplace.model.Image;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageMapper {
    public ImageOutDto toDto(Image image) {
        return new ImageOutDto(image.getName());
    }

    public List<ImageOutDto> toDtoList(List<Image> images) {
        return images.stream().map(this::toDto).toList();
    }
}
