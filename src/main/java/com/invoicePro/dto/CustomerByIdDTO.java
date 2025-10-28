package com.invoicePro.dto;

import com.invoicePro.enums.CustomerType;
import com.invoicePro.enums.State;
import com.invoicePro.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerByIdDTO {
    private Long id;
    private String name;
    private CustomerType customerType;
    private String emailId;
    private String phoneNumber;
    private Status status;
    private String address;
    private String city;
    private State state;
    private String pinCode;
}
