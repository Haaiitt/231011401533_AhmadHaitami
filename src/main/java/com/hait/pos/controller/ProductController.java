package com.hait.pos.controller;

import com.hait.pos.model.Product;
import com.hait.pos.model.Category;
import com.hait.pos.repository.ProductRepository;
import com.hait.pos.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping
    public Map<String, List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .collect(Collectors.groupingBy(
                        product -> product.getCategory().getName()
                ));
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {

        String categoryName = product.getCategory().getName();

        Category category = categoryRepository
                .findByName(categoryName)
                .orElseThrow(() ->
                        new RuntimeException("Category tidak ditemukan"));

        product.setCategory(category);

        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @RequestBody Product productDetails) {

        Product product = productRepository
                .findById(id)
                .orElseThrow();

        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());

        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);

        return "Product deleted";
    }
}