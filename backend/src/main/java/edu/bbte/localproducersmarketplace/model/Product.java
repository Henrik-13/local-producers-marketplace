package edu.bbte.localproducersmarketplace.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Products")
@Getter
@Setter
@ToString(callSuper = true)
public class Product extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 500)
    private String description;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private int quantity;
    @JoinColumn(nullable = false)
    @ManyToOne
    private Category category;
    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();
}
