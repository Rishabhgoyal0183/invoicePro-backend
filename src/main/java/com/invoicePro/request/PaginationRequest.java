package com.invoicePro.request;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class PaginationRequest {

    private int pageNumber = 0;

    private int pageSize = 10;

    private String sortBy = "id";

    private Sort.Direction sortDirection = Sort.Direction.DESC;
}
