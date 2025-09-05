package com.service.product.service;

import com.service.product.entity.Product;
import com.service.product.error_handling.exception.GeneralException;
import com.service.product.error_handling.exception.NotFoundException;
import com.service.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductService
{
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product save(Product product)
    {
        log.info("Attempting to save product: {}", product.getName());

        Product alreadyExists = productRepository.findByName(product.getName());

        if (alreadyExists != null && !Objects.equals(alreadyExists.getId(), product.getId()))
        {
            log.warn("Product with name {} already exists", product.getName());
            throw new GeneralException("Product with name " + product.getName() + " already exists");
        }

        Product saved = productRepository.save(product);
        log.info("Product saved successfully with id {}", saved.getId());

        return saved;
    }

    public Product findById(Long id)
    {
        log.debug("Searching product with id {}", id);

        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", id);
                    return new NotFoundException("Product with id " + id + " not found");
                });
    }

    public List<Product> findAll()
    {
        log.debug("Fetching all products");
        return productRepository.findAll();
    }

    public Page<Product> findByNameStartsWith(String name, int page, int size)
    {
        log.debug("Searching products by name starting with: {}", name);

        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findByNameStartsWith(name, pageable);
    }

    @Transactional
    public Product update(Product product, Long id)
    {
        log.info("Updating product id {} with new data", id);

        Product oldProduct = this.findById(id);
        Product fetchProduct = this.productRepository.findByName(product.getName());

        if (fetchProduct != null && !fetchProduct.getId().equals(oldProduct.getId()))
        {
            log.warn("There is another product with the name {}", product.getName());
            throw new GeneralException("There is another product with the name " + product.getName());
        }

        oldProduct.setName(product.getName());
        oldProduct.setPrice(product.getPrice());

        Product updated = productRepository.save(oldProduct);
        log.info("Product id {} updated successfully", updated.getId());
        return updated;
    }

    @Transactional
    public Product deleteById(Long id)
    {
        log.info("Deleting product with id {}", id);

        Product product = this.findById(id);
        productRepository.delete(product);

        log.info("Product with id {} deleted successfully", id);
        return product;
    }
}

