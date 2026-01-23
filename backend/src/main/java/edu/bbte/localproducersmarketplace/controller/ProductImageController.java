package edu.bbte.localproducersmarketplace.controller;

import edu.bbte.localproducersmarketplace.dto.in.ImageInDto;
import edu.bbte.localproducersmarketplace.dto.out.ImageOutDto;
import edu.bbte.localproducersmarketplace.mapper.ImageMapper;
import edu.bbte.localproducersmarketplace.model.Image;
import edu.bbte.localproducersmarketplace.model.Product;
import edu.bbte.localproducersmarketplace.service.ImageService;
import edu.bbte.localproducersmarketplace.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductImageController {

    private final ImageService imageService;
    private final ProductService productService;
    private final ImageMapper imageMapper;

    @Autowired
    public ProductImageController(ImageService imageService, ProductService productService, ImageMapper imageMapper) {
        this.imageService = imageService;
        this.productService = productService;
        this.imageMapper = imageMapper;
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<ImageOutDto> createImage(@PathVariable Long id, @ModelAttribute ImageInDto inDto) {
        try {
            Product product = productService.findById(id);
            Image image = new Image();
            image.setProduct(product);
            image = imageService.save(image, inDto.getFile(), inDto.getName());
            return ResponseEntity.created(new URI("/images/" + image.getId())).body(imageMapper.toDto(image));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
