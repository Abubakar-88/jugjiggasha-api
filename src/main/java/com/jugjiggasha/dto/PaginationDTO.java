package com.jugjiggasha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationDTO {
    private int page;
    private int size;
    private String sortBy;
    private String sortDirection;
    // Default constructor
    public PaginationDTO() {
        this.page = 0;
        this.size = 10;
        this.sortBy = "createdAt";
        this.sortDirection = "desc";
    }

}
