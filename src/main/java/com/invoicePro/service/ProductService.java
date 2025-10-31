package com.invoicePro.service;

import com.invoicePro.dto.ProductMetricsDTO;
import com.invoicePro.dto.ProductsDTO;
import com.invoicePro.request.PaginationRequest;
import com.invoicePro.request.SaveProductRequest;
import com.invoicePro.response.PageResponse;

public interface ProductService {
    PageResponse<ProductsDTO> getAllProducts(long businessId, PaginationRequest paginationRequest);

    String saveProduct(long businessId, SaveProductRequest saveProductRequest);

    ProductsDTO getProductById(long businessId, long productId);

    String updateProduct(long businessId, long productId, SaveProductRequest saveProductRequest);

    String deleteProduct(long businessId, long productId);

    ProductMetricsDTO getProductMetrics(long businessId);
}
