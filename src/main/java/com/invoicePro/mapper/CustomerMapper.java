package com.invoicePro.mapper;

import com.invoicePro.dto.CustomersDTO;
import com.invoicePro.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomersDTO toDTO(Customer customer) {
        CustomersDTO dto = new CustomersDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhoneNumber(customer.getPhone());
        dto.setStatus(customer.getStatus());
        dto.setInvoices((long) 1);
        dto.setTotalAmount((long) 1000);

        // For Future Use
        //customer.getInvoices().stream()
        //                .mapToLong(invoice -> invoice.getTotalAmount().longValue())
        //                .sum()
        return dto;
    }
}
