package com.invoicePro.service;

import com.invoicePro.dto.CustomersDTO;
import com.invoicePro.request.PaginationRequest;
import com.invoicePro.request.SaveCustomerRequest;
import com.invoicePro.response.PageResponse;


public interface CustomerService {
    PageResponse<CustomersDTO> getAllCustomers(long businessId, PaginationRequest paginationRequest);

    String saveCustomers(long businessId, SaveCustomerRequest saveCustomerRequest);
}
