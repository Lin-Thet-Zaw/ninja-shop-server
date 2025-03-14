package com.ninjashop.ninjashop.controller;

import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.model.Product;
import com.ninjashop.ninjashop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(@RequestParam String category,
                                                                      @RequestParam List<String> color,
                                                                      @RequestParam List<String> size,
                                                                      @RequestParam Integer minPrice,
                                                                      @RequestParam Integer maxPrice,
                                                                      @RequestParam Integer minDiscounted,
                                                                      @RequestParam String sort,
                                                                      @RequestParam String stock,
                                                                      @RequestParam Integer pageNumber,
                                                                      @RequestParam Integer pageSize
    ) {
        Page<Product> res = productService.getAllFilterProduct(category, color, size, minPrice, maxPrice, minDiscounted, sort, stock, pageNumber, pageSize);
        System.out.println("Product complete");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProuctByIdHandler(@PathVariable Long productId) throws ProductException {
        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.findAllProducts();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // New endpoint to filter by category only
    @GetMapping("/products/category")
    public ResponseEntity<List<Product>> findProductsByCategory(
            @RequestParam String category
    ) {
        try {
            List<Product> products = productService.findProductsByCategory(category);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}