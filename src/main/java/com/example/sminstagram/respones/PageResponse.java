package com.example.sminstagram.respones;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private List<T> data;
    private int page;
    private int pageSize;
    private long totalItems;
    private int totalPages;

    public static <T> PageResponse<T> of(Page<T> pageData) {
        return PageResponse.<T>builder()
                .data(pageData.getContent())
                .page(pageData.getNumber())
                .pageSize(pageData.getSize())
                .totalItems(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .build();
    }
}