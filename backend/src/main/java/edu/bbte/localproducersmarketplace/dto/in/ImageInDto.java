package edu.bbte.localproducersmarketplace.dto.in;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImageInDto {
    private String name;
    private MultipartFile file;
}