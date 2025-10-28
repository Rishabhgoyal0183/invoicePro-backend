package com.invoicePro.controller;

import com.invoicePro.dto.CustomerByIdDTO;
import com.invoicePro.dto.CustomersDTO;
import com.invoicePro.request.PaginationRequest;
import com.invoicePro.request.SaveCustomerRequest;
import com.invoicePro.response.PageResponse;
import com.invoicePro.response.Response;
import com.invoicePro.service.CustomerService;
import com.invoicePro.utils.ExceptionUtils;
import com.invoicePro.utils.ResponseUtils;
import com.invoicePro.validator.RequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/businesses/{businessId}")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/customers")
    public ResponseEntity<Response> getAllCustomers(@PathVariable long businessId,
                                                    @RequestBody(required = false) PaginationRequest paginationRequest) {

        try {
            PageResponse<CustomersDTO> customersDTOS = customerService.getAllCustomers(businessId, paginationRequest);
            if (customersDTOS == null || customersDTOS.getContent().isEmpty()) {
                throw new RuntimeException("No customers found, please add customers");
            }
            return ResponseUtils.data(customersDTOS);
        }catch (Exception exception){
            return ExceptionUtils.handleException(exception);
        }
    }

    @PostMapping("/customers/save")
    public ResponseEntity<Response> saveCustomers(@PathVariable long businessId,
                                                  @RequestBody @Valid SaveCustomerRequest saveCustomerRequest,
                                                  BindingResult bindingResult) {

        RequestValidator.validateRequest(bindingResult);
        try {
            String message = customerService.saveCustomers(businessId, saveCustomerRequest);
            return ResponseUtils.data(message);
        }catch (Exception exception){
            return ExceptionUtils.handleException(exception);
        }
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<Response> getCustomerById(@PathVariable long businessId,
                                                   @PathVariable long customerId) {

        try {
            CustomerByIdDTO customerByIdDTO = customerService.getCustomerById(businessId, customerId);
            return ResponseUtils.data(customerByIdDTO);
        } catch (Exception exception) {
            return ExceptionUtils.handleException(exception);
        }
    }

    @PutMapping("/customers/{customerId}")
    public ResponseEntity<Response> updateCustomer(@PathVariable long businessId,
                                                   @PathVariable long customerId,
                                                   @RequestBody @Valid SaveCustomerRequest saveCustomerRequest,
                                                   BindingResult bindingResult) {
        RequestValidator.validateRequest(bindingResult);

        try {
            String message = customerService.updateCustomer(businessId, customerId, saveCustomerRequest);
            return ResponseUtils.data(message);
        } catch (Exception exception) {
            return ExceptionUtils.handleException(exception);
        }
    }

    @PutMapping("/customers/{customerId}/change-status")
    public ResponseEntity<Response> changeCustomerStatus(@PathVariable long businessId,
                                                    @PathVariable long customerId) {

        try {
            String message = customerService.changeCustomerStatus(businessId, customerId);
            return ResponseUtils.data(message);
        } catch (Exception exception) {
            return ExceptionUtils.handleException(exception);
        }
    }

    @DeleteMapping("/customers/{customerId}/delete")
    public ResponseEntity<Response> softDeleteCustomer(@PathVariable long businessId,
                                                         @PathVariable long customerId) {

        try {
            String message = customerService.softDeleteCustomer(businessId, customerId);
            return ResponseUtils.data(message);
        } catch (Exception exception) {
            return ExceptionUtils.handleException(exception);
        }
    }
}
