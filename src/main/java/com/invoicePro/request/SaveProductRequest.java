package com.invoicePro.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SaveProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(message = "Product name must be between 1 and 255 characters", min = 1, max = 255)
    private String name;

    @NotBlank(message = "Product description is required")
    @Size(message = "Product description must be between 1 and 1000 characters", min = 1, max = 1000)
    private String description;

    @NotNull(message = "Product Price is required")
    @PositiveOrZero(message = "Product Price must be zero or positive")
    private Double price;

    @NotNull(message = "Product quantity is required")
    @PositiveOrZero(message = "Product quantity must be zero or positive")
    private Integer quantity;

    @NotBlank(message = "Product category is required")
    @Size(message = "Product category must be between 1 and 100 characters", min = 1, max = 100)
    private String productCategory;

    @NotBlank(message = "Measuring unit is required")
    @Size(message = "Measuring unit must be between 1 and 50 characters", min = 1, max = 50)
    private String measuringUnit;

}
