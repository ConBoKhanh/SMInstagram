package com.example.sminstagram.requests;

import com.example.sminstagram.entities.FilterRequest;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryRequest {
    private int page = 0;
    private int pageSize = 10;
    private List<FilterRequest> filters;
}
