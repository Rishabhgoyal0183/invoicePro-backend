package com.invoicePro.service.impl;

import com.invoicePro.dto.CustomersDTO;
import com.invoicePro.entity.BusinessOwner;
import com.invoicePro.entity.Customer;
import com.invoicePro.enums.CustomerType;
import com.invoicePro.enums.Status;
import com.invoicePro.exception.ResourceNotFoundException;
import com.invoicePro.mapper.CustomerMapper;
import com.invoicePro.repository.BusinessOwnerRepository;
import com.invoicePro.repository.BusinessRepository;
import com.invoicePro.repository.CustomerRepository;
import com.invoicePro.request.PaginationRequest;
import com.invoicePro.request.SaveCustomerRequest;
import com.invoicePro.response.PageResponse;
import com.invoicePro.security.userDetails.BusinessOwnerDetails;
import com.invoicePro.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final BusinessRepository businessRepository;

    private final BusinessOwnerRepository businessOwnerRepository;

    private final CustomerMapper customerMapper;


    @Override
    public PageResponse<CustomersDTO> getAllCustomers(long businessId, PaginationRequest paginationRequest) {
        businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found, please create a business first"));

        Pageable pageable = PageRequest.of(paginationRequest.getPageNumber() , paginationRequest.getPageSize(),
                Sort.by(paginationRequest.getSortDirection() , paginationRequest.getSortBy()));

        Page<Customer> customers = customerRepository.findByBusinessIdAndStatusNot(businessId, Status.DELETED, pageable);

        Page<CustomersDTO> customersDTOS = customers.map(customerMapper::toDTO);

        return PageResponse.from(customersDTOS);
    }

    @Override
    public String saveCustomers(long businessId, SaveCustomerRequest saveCustomerRequest) {

        businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found, please create a business first"));

        customerRepository.findByNameAndBusinessId(saveCustomerRequest.getName(), businessId)
                .ifPresent(c -> {
                    throw new RuntimeException("Customer with name " + saveCustomerRequest.getName() + " already exists");
                });

        Customer customer = new Customer();
        customer.setBusinessId(businessId);
        customer.setName(saveCustomerRequest.getName());
        customer.setCustomerType(CustomerType.valueOf(saveCustomerRequest.getCustomerType()));
        customer.setEmail(saveCustomerRequest.getEmail());
        customer.setPhone(saveCustomerRequest.getPhoneNumber());
        customer.setStatus(Status.ACTIVE);
        customer.setAddress(saveCustomerRequest.getAddress());
        customer.setCity(saveCustomerRequest.getCity());
        customer.setState(saveCustomerRequest.getState());
        customer.setPinCode(saveCustomerRequest.getPinCode());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy(getCurrentBusinessOwner().getId());
        customer.setUpdatedAt(LocalDateTime.now());

        customerRepository.save(customer);

        return "Customer details saved successfully";
    }

    private BusinessOwner getCurrentBusinessOwner() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        BusinessOwnerDetails businessOwnerDetails = (BusinessOwnerDetails) auth.getPrincipal();
        return businessOwnerRepository.findById(businessOwnerDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
