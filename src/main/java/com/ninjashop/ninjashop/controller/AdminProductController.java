package com.ninjashop.ninjashop.controller;

import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.Product;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.request.CreateProductRequest;
import com.ninjashop.ninjashop.response.ApiResponse;
import com.ninjashop.ninjashop.service.ProductService;
import com.ninjashop.ninjashop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;
    @PostMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest req,
                                                 @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Product product = productService.createProduct(req);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) throws ProductException {
        productService.deleteProduct(productId);
        ApiResponse res = new ApiResponse();
        res.setMessage("Product Delete Successfully");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProduct(){
        List<Product> products = productService.findAllProducts();
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProduct(@RequestBody Product req,
                                                 @PathVariable("productId")Long productId) throws  ProductException{
        Product product = productService.updateProduct(productId,req);
        return new ResponseEntity<>(product,HttpStatus.CREATED);
    }

    @PostMapping("/creates")
    public ResponseEntity<ApiResponse> createMultipleProduct(@RequestBody CreateProductRequest[] req){
        for (CreateProductRequest product:req){
            productService.createProduct(product);
        }
        ApiResponse res = new ApiResponse();
        res.setMessage("Product Created Successfully");
        res.setStatus(true);

        return new ResponseEntity<>(res,HttpStatus.CREATED);
    }
}
