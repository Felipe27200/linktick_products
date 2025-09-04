package com.service.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateProductDTO
{
    @NotBlank(message = "The name field is required.")
    private String name;
    @Min(value = 1, message = "The price must be greater than zero")
    @NotNull(message = "The price is required")
    private BigDecimal price;

    public CreateProductDTO() {
    }

    public CreateProductDTO(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
