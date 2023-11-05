package com.intuit.craft.model;

import com.intuit.craft.enums.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long id;

    @Column(name = "product", nullable = false)
    private String title;

    @Column(name = "base_price", nullable = false)
    private Double basePrice;

    @Column(name = "product_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "product_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
