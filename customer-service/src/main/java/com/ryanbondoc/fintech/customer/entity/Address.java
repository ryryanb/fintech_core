package com.ryanbondoc.fintech.customer.entity;



import java.time.Instant;
import java.util.UUID;

import com.ryanbondoc.fintech.customer.enums.AddressType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "addresses")
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "street")
private String street;

@Column(name = "city")
private String city;

@Column(name = "province")
private String province;

@Column(name = "country")
private String country;

@Column(name = "postal_code")
private String postalCode;

@Enumerated(EnumType.STRING)
@Column(name = "address_type")
private AddressType addressType;

@Column(name = "created_at")
private Instant createdAt;

@Column(name = "updated_at")
private Instant updatedAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
