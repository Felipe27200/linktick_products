package com.service.product.service;

import com.service.product.entity.Product;
import com.service.product.error_handling.exception.NotFoundException;
import com.service.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest
{
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    public void ProductService_Create_Product_Test()
    {
        Product product = new Product();

        product.setName("product 1");
        product.setPrice(new BigDecimal(3800));

        when(productRepository.save(Mockito.any(Product.class)))
            .thenReturn(product);

        Product newProduct = productService.save(product);

        Assertions.assertNotNull(newProduct);
    }

    @Test
    public void ProductService_GetAll_Page_Test()
    {
        Page<Product> products = Mockito.mock(Page.class);
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findAll(Mockito.any(Pageable.class))).thenReturn(products);

        Page<Product> saveProduct = productService.productPagination(pageable);

        Assertions.assertNotNull(saveProduct);
    }

    @Test
    public void ProductService_FindById_Test()
    {
        Product newProduct = new Product();

        newProduct.setId(1L);
        newProduct.setName("newProduct 1");
        newProduct.setPrice(new BigDecimal(3800));

        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(newProduct));

        Product product = productService.findById(newProduct.getId());

        Assertions.assertNotNull(product);
    }

    @Test
    public void ProductService_Update_Test()
    {
        Product newProduct = new Product();

        newProduct.setId(1L);
        newProduct.setName("newProduct 1");
        newProduct.setPrice(new BigDecimal(3800));

        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(newProduct));
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(newProduct);

        Product product = productService.update(newProduct, 1L);

        Assertions.assertNotNull(product);
    }

    @Test
    public void ProductService_Delete_Test()
    {
        Product newProduct = new Product();

        newProduct.setId(1L);
        newProduct.setName("newProduct 1");
        newProduct.setPrice(new BigDecimal(3800));

        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(newProduct));

        // Checks that an exception is not threw
        Assertions.assertAll(() -> productService.deleteById(1L));
    }

    @Test
    public void ProductService_FindById_NotFound_Test() {
        // Arrange
        Long nonExistentId = 99L;
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(NotFoundException.class, () -> {
            productService.findById(nonExistentId);
        });
    }
}
