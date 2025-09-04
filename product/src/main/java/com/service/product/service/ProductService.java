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
import java.util.Optional;

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

    public Product findById(Long id)
    {
        Optional<Product> product = this.productRepository.findById(id);

        if (product.isEmpty())
            throw new NotFoundException("Product with id " + id + " not found");

        return product.get();
    }

    public List<Product> findAll()
    {
        List<Product> products = this.productRepository.findAll();

        return products;
    }

    public Page<Product> productPagination(Pageable pageable)
    {
        return this.productRepository.findAll(pageable);
    }

    public Page findByNameStartsWith(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return this.productRepository.findByNameStartsWith(name, pageable);
    }

    @Transactional
    public Product update(Product product, Long id)
    {
        Product oldProduct = this.findById(id);
        Product fetchProduct = this.productRepository.findByName(product.getName());

        if (oldProduct == null)
            throw new NotFoundException("Product with id " + id + " not found");

        if (fetchProduct != null && !fetchProduct.getId().equals(oldProduct.getId()))
            throw new GeneralException("There is another product with the name " + product.getName());

        oldProduct.setName(product.getName());
        oldProduct.setPrice(product.getPrice());

        return productRepository.save(oldProduct);
    }

    @Transactional
    public Product deleteById(Long id)
    {
        Product product = this.findById(id);

        this.productRepository.delete(product);

        return product;
    }
}
