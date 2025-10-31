package com.invoicePro.mapper;

import com.invoicePro.dto.ProductsDTO;
import com.invoicePro.entity.Product;
import com.invoicePro.request.SaveProductRequest;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductsDTO toDTO(Product product) {
        ProductsDTO dto = new ProductsDTO();
        dto.setId(product.getId());
        dto.setProductName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setProductCategory(product.getProductCategory());
        dto.setPrice(product.getSalePrice());
        dto.setStock(product.getStock());
        dto.setSku(product.getSku());
        dto.setMeasuringUnit(product.getUom());

        return dto;
    }

    public Product toEntity(SaveProductRequest saveProductRequest) {

        Product product = new Product();
        product.setName(saveProductRequest.getName());
        product.setDescription(saveProductRequest.getDescription());
        product.setProductCategory(saveProductRequest.getProductCategory());
        product.setSalePrice(saveProductRequest.getPrice());
        product.setStock(saveProductRequest.getQuantity());
        product.setUom(saveProductRequest.getMeasuringUnit());
        return product;
    }
}
