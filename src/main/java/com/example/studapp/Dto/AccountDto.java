package com.example.studapp.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDto {

    private Long id;
    private String accountHolderName;
    private double balance;
    private String username;
    private String password;
    private String role;
    private String CreatedBy;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;

    public void setCreatedAt(LocalDateTime createdAt, ZoneId userZone)
    {
        this.CreatedAt = (createdAt != null)
                ? createdAt.atZone(ZoneId.of("UTC")).withZoneSameInstant(userZone).toLocalDateTime()
                : null;
    }


    public void setUpdatedAt(LocalDateTime updatedAt, ZoneId userZone)
    {
        this.UpdatedAt = (updatedAt != null)
                ? updatedAt.atZone(ZoneId.of("UTC")).withZoneSameInstant(userZone).toLocalDateTime()
                : null;
    }


}
