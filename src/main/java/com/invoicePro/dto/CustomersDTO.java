package com.invoicePro.dto;

import com.invoicePro.enums.Status;
import lombok.Data;

@Data
public class CustomersDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Status status;
    private Long invoices;
    private Long totalAmount;
}
