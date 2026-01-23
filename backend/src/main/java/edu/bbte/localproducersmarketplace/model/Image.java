package edu.bbte.localproducersmarketplace.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Images")
@Getter
@Setter
public class Image extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String name;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;
}
