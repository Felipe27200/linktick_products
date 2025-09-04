package com.service.product.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${apiPrefix}/products")
public class ProductController
{
    @GetMapping("/ok")
    public String ok()
    {
        return "OK";
    }
}
