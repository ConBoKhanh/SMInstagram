package com.example.sminstagram.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterRequest {
    private String field;        // "fullName", "email", "role"...
    private String operator;     // "LIKE", "EQUAL", "NOT_EQUAL", "GT", "LT"
    private String value;        // "Duy", "admin", "true"...
    private String sort;         // "asc" | "desc" | null
}