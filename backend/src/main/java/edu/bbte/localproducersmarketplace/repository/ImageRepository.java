package edu.bbte.localproducersmarketplace.repository;

import edu.bbte.localproducersmarketplace.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
