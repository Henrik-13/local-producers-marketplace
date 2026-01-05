package edu.bbte.localproducersmarketplace.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Categories")
@Getter
@Setter
@ToString(callSuper = true)
public class Category extends BaseEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String name;
}
