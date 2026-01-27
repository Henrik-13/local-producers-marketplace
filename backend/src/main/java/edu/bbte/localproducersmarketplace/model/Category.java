package edu.bbte.localproducersmarketplace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "Categories")
@Getter
@Setter
@ToString(callSuper = true, exclude = {"products"})
public class Category extends BaseEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    List<Product> products;
}
