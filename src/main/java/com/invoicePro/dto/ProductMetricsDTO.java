package com.invoicePro.dto;

import lombok.Data;

@Data
public class ProductMetricsDTO {

    private Long totalProductCount;
    private Long lowStockProductCount;
    private Double totalInventoryValue;
}
