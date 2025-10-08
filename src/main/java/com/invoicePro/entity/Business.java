package com.invoicePro.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "businesses",
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_businesses_owner_name", columnNames = {"owner_id", "name"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // owning side of relationship
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_business_owner"))
    private BusinessOwner owner;

    @Column(name = "name", length = 255, nullable = false)
    @Size(max = 255)
    private String name;

    @Column(name = "is_registered_for_gst", nullable = false)
    private Boolean isRegisteredForGst = false;

    @Column(name = "gstin", length = 15, unique = false, nullable = true)
    @Size(max = 15)
    private String gstin;

    @Column(name = "pan", length = 10)
    @Size(max = 10)
    private String pan;

    @Column(name = "business_type", length = 50)
    private String businessType;

    @Column(name = "address", length = 255, columnDefinition = "TEXT")
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "pin_code", length = 20)
    private String pinCode;

    @Column(name = "contact_email", length = 200)
    private String contactEmail;

    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

    @Column(name = "status", length = 30)
    private String status = "ACTIVE";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;
}