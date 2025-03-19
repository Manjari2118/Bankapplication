package com.example.studapp.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Authenticateresponse {

    private String jwt;
    private String error;


}
