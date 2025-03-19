package com.example.studapp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountMapper {
    @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }
}


