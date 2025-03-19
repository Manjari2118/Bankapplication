package com.example.studapp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name="accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "account_holder_name")
    private String accountHolderName;
    private double balance;
    private String username;
    private String password;
    private String createdBy;
    private String role;


    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime CreatedAt;

    @LastModifiedDate
    @Column(nullable = true)
    private LocalDateTime UpdatedAt;

    @PrePersist
    public void prePersist() {
        CreatedAt = LocalDateTime.now(ZoneOffset.UTC);
        UpdatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    @PreUpdate
    public void preUpdate() {
        UpdatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }


}
