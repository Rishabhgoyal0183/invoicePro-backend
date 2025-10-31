package com.invoicePro.controller;

import com.invoicePro.dto.ProductsDTO;
import com.invoicePro.request.PaginationRequest;
import com.invoicePro.request.SaveProductRequest;
import com.invoicePro.response.PageResponse;
import com.invoicePro.response.Response;
import com.invoicePro.service.ProductService;
import com.invoicePro.utils.ExceptionUtils;
import com.invoicePro.utils.ResponseUtils;
import com.invoicePro.validator.RequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/businesses/{businessId}/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Response> getAllProducts(@PathVariable long businessId,
                                                    @RequestBody(required = false) PaginationRequest paginationRequest) {

        try {
            PageResponse<ProductsDTO> productsDTO = productService.getAllProducts(businessId, paginationRequest);
            if (productsDTO == null || productsDTO.getContent().isEmpty()) {
                throw new RuntimeException("No Products found, please add products first.");
            }
            return ResponseUtils.data(productsDTO);
        }catch (Exception exception){
            return ExceptionUtils.handleException(exception);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Response> saveProduct(@PathVariable long businessId,
                                                @RequestBody @Valid SaveProductRequest saveProductRequest,
                                                BindingResult bindingResult) {

        RequestValidator.validateRequest(bindingResult);
        try {
            String message = productService.saveProduct(businessId, saveProductRequest);
            return ResponseUtils.data(message);
        }catch (Exception exception){
            return ExceptionUtils.handleException(exception);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Response> getProductById(@PathVariable long businessId,
                                                   @PathVariable long productId) {

        try {
            ProductsDTO productsDTO = productService.getProductById(businessId, productId);
            return ResponseUtils.data(productsDTO);
        }catch (Exception exception){
            return ExceptionUtils.handleException(exception);
        }
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<Response> updateProduct(@PathVariable long businessId,
                                                @PathVariable long productId,
                                                @RequestBody @Valid SaveProductRequest saveProductRequest,
                                                BindingResult bindingResult) {

        RequestValidator.validateRequest(bindingResult);
        try {
            String message = productService.updateProduct(businessId, productId, saveProductRequest);
            return ResponseUtils.data(message);
        }catch (Exception exception){
            return ExceptionUtils.handleException(exception);
        }
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<Response> deleteProduct(@PathVariable long businessId,
                                                  @PathVariable long productId) {

        try {
            String message = productService.deleteProduct(businessId, productId);
            return ResponseUtils.data(message);
        }catch (Exception exception){
            return ExceptionUtils.handleException(exception);
        }
    }
}
