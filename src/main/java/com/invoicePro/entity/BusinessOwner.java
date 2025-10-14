package com.invoicePro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table(name = "business_owners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BusinessOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,       // or {PERSIST, MERGE} depending on need
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Business> businesses = new ArrayList<>();

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "gender", length = 45)
    @Size(max = 45)
    private String gender;

    @Email
    @Column(name = "email_id", unique = true)
    private String emailId;

    @Column(name = "phone_number",length = 20, unique = true)
    private String phoneNumber;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "status", length = 45)
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