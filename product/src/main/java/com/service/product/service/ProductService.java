package com.service.product.service;

import com.service.product.entity.Product;
import com.service.product.error_handling.exception.GeneralException;
import com.service.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProductService
{
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product save(Product product)
    {
        Product alreadyExists = productRepository.findByName(product.getName());

        if (alreadyExists != null && !Objects.equals(alreadyExists.getId(), product.getId()))
            throw new GeneralException("Product with name " + product.getName() + " already exists");

        return productRepository.save(product);
    }
}
