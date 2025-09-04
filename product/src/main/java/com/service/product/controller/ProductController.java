package com.service.product.controller;

import com.service.product.dto.CreateProductDTO;
import com.service.product.entity.Product;
import com.service.product.error_handling.exception.GeneralException;
import com.service.product.service.ProductService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/${apiPrefix}/products")
public class ProductController
{
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/ok")
    public String ok()
    {
        return "OK";
    }

    @PostMapping("/")
    public ResponseEntity<Product> save(@Valid @RequestBody CreateProductDTO productDTO)
    {
        Product product = this.modelMapper.map(productDTO, Product.class);

        if (product == null)
            throw new GeneralException("product is null");
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new GeneralException("price can not be negative");
        if (product.getName() == null || product.getName().trim().isEmpty())
            throw new GeneralException("The product name can not be empty");

        Product newProduct = this.productService.save(product);

        return new ResponseEntity<>(newProduct, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id)
    {
        if (id == null || id <= 0)
            throw new GeneralException("id product can not be null");

        Product product = this.productService.findById(id);

        return  new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Product>> findByAll()
    {
        return  new ResponseEntity<>(this.productService.findAll(), HttpStatus.OK);
    }
}
