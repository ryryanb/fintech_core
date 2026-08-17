package com.ryanbondoc.fintech.customer.entity;


import java.time.Instant;
import java.util.UUID;

import com.ryanbondoc.fintech.customer.enums.KycStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Size(min = 5, max = 20, message = "Customer number must be 5-20 characters")
    @Column(unique = true)
    private String customerNumber;
    
    private String firstName;
    
    private String lastName;
    
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;
    
    private String phoneNumber;    

    private String status;

    @Enumerated(EnumType.STRING)
@Column(name = "kyc_status")
private KycStatus kycStatus;

    private Instant createdAt;

private Instant updatedAt;

@PrePersist
private void onCreate() {
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
}

@PreUpdate
private void onUpdate() {
    this.updatedAt = Instant.now();
}
}
