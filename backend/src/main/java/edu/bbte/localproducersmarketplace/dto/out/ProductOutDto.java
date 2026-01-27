package edu.bbte.localproducersmarketplace.dto.out;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductOutDto {
    public Long id;
    public String name;
    public String description;
    public Double price;
    public Integer quantity;
    public CategoryOutDto category;
    public UserOutDto producer;
    public List<ImageOutDto> images;
}