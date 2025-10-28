package com.invoicePro.service;

import com.invoicePro.dto.CustomerByIdDTO;
import com.invoicePro.dto.CustomersDTO;
import com.invoicePro.request.PaginationRequest;
import com.invoicePro.request.SaveCustomerRequest;
import com.invoicePro.response.PageResponse;


public interface CustomerService {
    PageResponse<CustomersDTO> getAllCustomers(long businessId, PaginationRequest paginationRequest);

    String saveCustomers(long businessId, SaveCustomerRequest saveCustomerRequest);

    String updateCustomer(long businessId, long customerId, SaveCustomerRequest saveCustomerRequest);

    CustomerByIdDTO getCustomerById(long businessId, long customerId);

    String changeCustomerStatus(long businessId, long customerId);

    String softDeleteCustomer(long businessId, long customerId);
}
