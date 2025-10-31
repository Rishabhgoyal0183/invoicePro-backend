package com.invoicePro.service.impl;

import com.invoicePro.dto.ProductMetricsDTO;
import com.invoicePro.dto.ProductsDTO;
import com.invoicePro.entity.BusinessOwner;
import com.invoicePro.entity.Product;
import com.invoicePro.exception.ResourceNotFoundException;
import com.invoicePro.mapper.ProductMapper;
import com.invoicePro.repository.BusinessOwnerRepository;
import com.invoicePro.repository.BusinessRepository;
import com.invoicePro.repository.ProductRepository;
import com.invoicePro.request.PaginationRequest;
import com.invoicePro.request.SaveProductRequest;
import com.invoicePro.response.PageResponse;
import com.invoicePro.security.userDetails.BusinessOwnerDetails;
import com.invoicePro.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final BusinessRepository businessRepository;

    private final BusinessOwnerRepository businessOwnerRepository;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    public PageResponse<ProductsDTO> getAllProducts(long businessId, PaginationRequest paginationRequest) {
        businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found, please create a business first"));

        Pageable pageable = PageRequest.of(paginationRequest.getPageNumber() , paginationRequest.getPageSize(),
                Sort.by(paginationRequest.getSortDirection() , paginationRequest.getSortBy()));

        Page<Product> products = productRepository.findByBusinessId(businessId, pageable);

        Page<ProductsDTO> productsDTOS = products.map(productMapper::toDTO);

        return PageResponse.from(productsDTOS);
    }

    @Override
    @Transactional
    public String saveProduct(long businessId, SaveProductRequest saveProductRequest) {

        businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found, please create a business first"));

        productRepository.findByNameAndBusinessId(saveProductRequest.getName(), businessId)
                .ifPresent(existingProduct -> {
                    throw new RuntimeException("Product with the same name already exists in this business");
                });

        Product product = productMapper.toEntity(saveProductRequest);
        product.setBusinessId(businessId);
        product.setCreatedAt(LocalDateTime.now());
        product.setCreatedBy(getCurrentBusinessOwner().getId());

        productRepository.save(product);
        return "Product saved successfully";
    }

    @Override
    public ProductsDTO getProductById(long businessId, long productId) {
        businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found, please create a business first"));

        Product product = productRepository.findByIdAndBusinessId(productId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found, please add the product first"));

        return productMapper.toDTO(product);
    }

    @Override
    @Transactional
    public String updateProduct(long businessId, long productId, SaveProductRequest saveProductRequest) {
        businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found, please create a business first"));

        Product existingProduct = productRepository.findByIdAndBusinessId(productId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found, please add the product first"));

        validateCategoryAndUom(saveProductRequest, existingProduct);
        existingProduct.setName(saveProductRequest.getName());
        existingProduct.setDescription(saveProductRequest.getDescription());
        existingProduct.setSalePrice(saveProductRequest.getPrice());
        existingProduct.setStock(saveProductRequest.getQuantity());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        existingProduct.setUpdatedBy(getCurrentBusinessOwner().getId());

        productRepository.save(existingProduct);
        return "Product updated successfully";
    }

    @Override
    @Transactional
    public String deleteProduct(long businessId, long productId) {
        businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found, please create a business first"));

        Product product = productRepository.findByIdAndBusinessId(productId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found, please add the product first"));
        productRepository.delete(product);
        return "Product deleted successfully";
    }

    @Override
    public ProductMetricsDTO getProductMetrics(long businessId) {

        businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found, please create a business first"));

        long totalProducts = productRepository.countByBusinessId(businessId);
        long lowStockProducts = productRepository.countByBusinessIdAndStockLessThan(businessId, 10);
        Double totalInventoryValue = productRepository.calculateTotalInventoryValue(businessId);

        ProductMetricsDTO productMetricsDTO = new ProductMetricsDTO();
        productMetricsDTO.setTotalProductCount(totalProducts);
        productMetricsDTO.setLowStockProductCount(lowStockProducts);
        productMetricsDTO.setTotalInventoryValue(totalInventoryValue);

        return productMetricsDTO;
    }

    private void validateCategoryAndUom(SaveProductRequest saveProductRequest, Product product) {

        if (!product.getProductCategory().equals(saveProductRequest.getProductCategory())) {
            throw new RuntimeException("Cannot change product category during update, please create a new product");
        }
        if (!product.getUom().equals(saveProductRequest.getMeasuringUnit())) {
            throw new RuntimeException("Cannot change measuring unit during update, please create a new product");
        }
    }

    private BusinessOwner getCurrentBusinessOwner() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        BusinessOwnerDetails businessOwnerDetails = (BusinessOwnerDetails) auth.getPrincipal();
        return businessOwnerRepository.findById(businessOwnerDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
