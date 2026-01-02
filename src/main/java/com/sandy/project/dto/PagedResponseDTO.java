// src/main/java/com/sandy/project/dto/PagedResponseDTO.java
package com.sandy.project.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PagedResponseDTO<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private List<T> content;
    private PageMetadata metadata;
    
    // Constructor dari Spring Page
    public PagedResponseDTO(Page<T> page) {
        this.content = page.getContent();
        this.metadata = new PageMetadata(
                page.getNumber(),           // Current page (0-indexed)
                page.getSize(),             // Size per page
                page.getTotalElements(),    // Total items
                page.getTotalPages(),       // Total pages
                page.isFirst(),             // Is first page?
                page.isLast(),              // Is last page?
                page.hasNext(),             // Has next page?
                page.hasPrevious()          // Has previous page?
        );
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PageMetadata implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private int pageNumber;         // Current page (0-indexed)
        private int pageSize;           // Items per page
        private long totalElements;     // Total items in DB
        private int totalPages;         // Total pages
        private boolean first;          // Is first page?
        private boolean last;           // Is last page?
        private boolean hasNext;        // Has next page?
        private boolean hasPrevious;    // Has previous page?
    }
}