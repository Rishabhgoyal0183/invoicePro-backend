package com.invoicePro.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_products_business_sku", columnNames = {"business_id", "sku"})
        },
        indexes = {
                @Index(name = "idx_products_business_name", columnList = "business_id, name"),
                @Index(name = "idx_products_business_category", columnList = "business_id, product_category")
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "name", nullable = false, length = 255, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "sku", length = 255, insertable = false, updatable = false)
    private String sku;

    @Column(name = "product_category", length = 255)
    private String productCategory;

    @Column(length = 50)
    private String uom = "pcs";

    @Column(name = "sale_price", columnDefinition = "DECIMAL(15,2) DEFAULT 0.00", nullable = false)
    private Double salePrice = 0.00;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

}
