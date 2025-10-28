package com.invoicePro.mapper;

import com.invoicePro.dto.CustomerByIdDTO;
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

    public CustomerByIdDTO toByIdDTO(Customer customer) {

        CustomerByIdDTO customerByIdDTO = new CustomerByIdDTO();
        customerByIdDTO.setId(customer.getId());
        customerByIdDTO.setName(customer.getName());
        customerByIdDTO.setCustomerType(customer.getCustomerType());
        customerByIdDTO.setEmailId(customer.getEmail());
        customerByIdDTO.setPhoneNumber(customer.getPhone());
        customerByIdDTO.setStatus(customer.getStatus());
        customerByIdDTO.setAddress(customer.getAddress());
        customerByIdDTO.setCity(customer.getCity());
        customerByIdDTO.setState(customer.getState());
        customerByIdDTO.setPinCode(customer.getPinCode());
        return customerByIdDTO;
    }
}
