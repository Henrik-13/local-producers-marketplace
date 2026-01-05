package edu.bbte.localproducersmarketplace.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
public class OrderItem extends BaseEntity {
    private int quantity;
    private double unitPrice;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;
}
