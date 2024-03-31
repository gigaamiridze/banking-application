package com.workshop.banking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse<T> {

    private List<T> content;

    private int pageNumber;

    private int pageSize;

    private int totalPages;

    private long totalElements;

    private boolean isFirst;

    private boolean isLast;

    private boolean isEmpty;
}
