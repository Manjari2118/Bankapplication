package com.example.studapp.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

        private int offset;
        private int pageSize;
        private String field;
        private String sortDir;
        private String timezone;
    }



