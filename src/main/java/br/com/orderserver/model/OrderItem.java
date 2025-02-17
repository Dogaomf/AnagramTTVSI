package br.com.orderserver.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    CustomerOrder customerOrder;
    @ManyToOne
    private Product product;

    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("price")
    private BigDecimal price;
}