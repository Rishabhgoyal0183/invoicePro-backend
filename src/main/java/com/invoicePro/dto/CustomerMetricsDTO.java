package com.invoicePro.dto;

import lombok.Data;

@Data
public class CustomerMetricsDTO {
    private Long totalCustomers;
    private Long activeCustomers;
    private Long inactiveCustomers;
    private Long retailCustomers;
    private Long wholesaleCustomers;
}
