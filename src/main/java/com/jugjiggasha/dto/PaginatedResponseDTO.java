package com.jugjiggasha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedResponseDTO<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int itemsPerPage;
    private boolean hasNext;
    private boolean hasPrevious;

    // Constructors
    public PaginatedResponseDTO() {
    }
    public PaginatedResponseDTO(List<T> content, int currentPage, int totalPages,
                                long totalItems, int itemsPerPage) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.itemsPerPage = itemsPerPage;
        this.hasNext = currentPage < totalPages;
        this.hasPrevious = currentPage > 1;
    }

}
