package com.invoicePro.dto;

import lombok.Data;

@Data
public class ProductsDTO {
    private Long id;
    private String productName;
    private String description;
    private String productCategory;
    private Double price;
    private Integer stock;
    private String sku;
    private String measuringUnit;
}
