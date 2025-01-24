package com.ninjashop.ninjashop.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.Product;
import com.ninjashop.ninjashop.model.Size;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.request.CreateProductRequest;
import com.ninjashop.ninjashop.response.ApiResponse;
import com.ninjashop.ninjashop.service.ProductService;
import com.ninjashop.ninjashop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<Product> createProduct(
            @RequestParam("image") String image,
            @RequestParam("brand") String brand,
            @RequestParam("title") String title,
            @RequestParam("color") String color,
            @RequestParam("discountedPrice") String discountedPriceStr,
            @RequestParam("price") String priceStr,
            @RequestParam("discountedPercent") String discountedPercentStr,
            @RequestParam("quantity") String quantityStr,
            @RequestParam("topLevelCategory") String topLevelCategory,
            @RequestParam("secondLevelCategory") String secondLevelCategory,
            @RequestParam("thirdLevelCategory") String thirdLevelCategory,
            @RequestParam("description") String description,
            @RequestParam("sizes") String sizesJson, // Accept sizes as JSON string
            @RequestHeader("Authorization") String jwt
    ) throws UserException, IOException {
        // Authenticate user
        User user = userService.findUserProfileByJwt(jwt);

        // Parse numeric fields with default values
        int discountedPrice = parseInteger(discountedPriceStr, 0);
        int price = parseInteger(priceStr, 0);
        int discountedPercent = parseInteger(discountedPercentStr, 0);
        int quantity = parseInteger(quantityStr, 0);

        // Parse sizes JSON string to Set<Size>
        Set<Size> sizes = parseSizes(sizesJson);

        // Create the product request object
        CreateProductRequest req = new CreateProductRequest();
        req.setBrand(brand);
        req.setTitle(title);
        req.setColor(color);
        req.setDiscountedPrice(discountedPrice);
        req.setPrice(price);
        req.setDiscountedPercent(discountedPercent);
        req.setQuantity(quantity);
        req.setTopLevelCategory(topLevelCategory);
        req.setSecondLevelCategory(secondLevelCategory);
        req.setThirdLevelCategory(thirdLevelCategory);
        req.setDescription(description);
        req.setSizes(sizes); // Set the sizes directly
        req.setImageUrl(image); // Save the image path as URL

        // Save product to database
        Product product = productService.createProduct(req);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    private Set<Size> parseSizes(String sizesJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse JSON as List<Size> and convert to Set<Size> to remove duplicates
            List<Size> sizeList = objectMapper.readValue(sizesJson, new TypeReference<List<Size>>() {});
            return new HashSet<>(sizeList);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse sizes JSON", e);
        }
    }

    private int parseInteger(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    //    @Value("${upload.folder}")
//    private String uploadFolder;

//    /**
//     * Endpoint to create a product with an image.
//     */
//    @PostMapping("/")
//    public ResponseEntity<Product> createProduct(
//            @RequestParam("image") MultipartFile image,
//            @RequestParam("brand") String brand,
//            @RequestParam("title") String title,
//            @RequestParam("color") String color,
//            @RequestParam("discountedPrice") String discountedPriceStr, // Bind as String
//            @RequestParam("price") String priceStr, // Bind as String
//            @RequestParam("discountedPercent") String discountedPercentStr, // Bind as String
//            @RequestParam("quantity") String quantityStr, // Bind as String
//            @RequestParam("topLevelCategory") String topLevelCategory,
//            @RequestParam("secondLevelCategory") String secondLevelCategory,
//            @RequestParam("thirdLevelCategory") String thirdLevelCategory,
//            @RequestParam("description") String description,
//            @RequestParam("size") String size,
//            @RequestHeader("Authorization") String jwt
//    ) throws UserException, IOException {
//        // Authenticate user
//        User user = userService.findUserProfileByJwt(jwt);
//
//        // Parse numeric fields with default values
//        int discountedPrice = parseInteger(discountedPriceStr, 0);
//        int price = parseInteger(priceStr, 0);
//        int discountedPercent = parseInteger(discountedPercentStr, 0);
//        int quantity = parseInteger(quantityStr, 0);
//
//        // Save the uploaded image
//        String imagePath = saveImage(image);
//
//        // Create the product request object
//        CreateProductRequest req = new CreateProductRequest();
//        req.setBrand(brand);
//        req.setTitle(title);
//        req.setColor(color);
//        req.setDiscountedPrice(discountedPrice);
//        req.setPrice(price);
//        req.setDiscountedPercent(discountedPercent);
//        req.setQuantity(quantity);
//        req.setTopLevelCategory(topLevelCategory);
//        req.setSecondLevelCategory(secondLevelCategory);
//        req.setThirdLevelCategory(thirdLevelCategory);
//        req.setDescription(description);
//        req.setSize(size);
//        req.setImageUrl(imagePath); // Save the image path as URL
//
//        // Save product to database
//        Product product = productService.createProduct(req);
//        return new ResponseEntity<>(product, HttpStatus.CREATED);
//    }
//
//
//    /**
//     * Save the uploaded image to the "static/products" folder.
//     */
//    private String saveImage(MultipartFile image) throws IOException {
//        // Create the directory if it does not exist
//        Path productsPath = Paths.get(uploadFolder);
//        if (!Files.exists(productsPath)) {
//            Files.createDirectories(productsPath);
//        }
//
//        // Generate a unique file name
//        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
//        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
//
//        // Save the file
//        Path path = Paths.get(productsPath.toString(), uniqueFileName);
//        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//
//        // Return the relative file path
//        return "/products/" + uniqueFileName;
//    }

    /**
     * Endpoint to delete a product by its ID.
     */
    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) throws ProductException {
        productService.deleteProduct(productId);
        ApiResponse res = new ApiResponse();
        res.setMessage("Product deleted successfully.");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve all products.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> products = productService.findAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Endpoint to update a product by its ID.
     */
    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProduct(
            @RequestBody Product req,
            @PathVariable("productId") Long productId) throws ProductException {
        Product product = productService.updateProduct(productId, req);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * Endpoint to create multiple products at once.
     */
    @PostMapping("/creates")
    public ResponseEntity<ApiResponse> createMultipleProduct(@RequestBody CreateProductRequest[] req) {
        for (CreateProductRequest product : req) {
            productService.createProduct(product);
        }
        ApiResponse res = new ApiResponse();
        res.setMessage("Products created successfully.");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}