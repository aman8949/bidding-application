package com.intuit.craft.model;

import com.intuit.craft.enums.Category;
import jakarta.persistence.*;
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

    @Column(name = "product")
    private String title;

    @Column(name = "base_price")
    private Double basePrice;

    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "product_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
