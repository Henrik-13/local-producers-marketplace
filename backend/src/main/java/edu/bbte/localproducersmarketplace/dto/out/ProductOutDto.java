package edu.bbte.localproducersmarketplace.dto.out;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductOutDto {
    public Long id;
    public String name;
    public String description;
    public Double price;
    public Integer quantity;
    public Long categoryId;
    public String categoryName;
    public List<String> imageUrls;
}