package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.model.Category;
import com.ninjashop.ninjashop.model.Product;
import com.ninjashop.ninjashop.repository.CategpryRepository;
import com.ninjashop.ninjashop.repository.ProductRepository;
import com.ninjashop.ninjashop.request.CreateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CategpryRepository categpryRepository;

    public ProductServiceImplementation(ProductRepository productRepository, UserService userService, CategpryRepository categpryRepository) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categpryRepository = categpryRepository;
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(CreateProductRequest req) {

        Category topLevel = categpryRepository.findByName(req.getTopLevelCategory());
        if (topLevel == null) {
           Category topLevelCategory = new Category();
            topLevelCategory.setName(req.getTopLevelCategory());
            topLevelCategory.setLevel(1);
            topLevel = categpryRepository.save(topLevelCategory);
        }

        Category secondLevel = categpryRepository.findByNameAndParent(req.getSecondLevelCategory(), topLevel.getName());
        if (secondLevel == null) {
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(req.getSecondLevelCategory());
            secondLevelCategory.setParent(topLevel);
            secondLevelCategory.setLevel(2);
            secondLevel = categpryRepository.save(secondLevelCategory);
        }

        Category thirdLevel = categpryRepository.findByNameAndParent(req.getThirdLevelCategory(), secondLevel.getName());
        if (thirdLevel == null) {
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(req.getThirdLevelCategory());
            thirdLevelCategory.setParent(secondLevel);
            thirdLevelCategory.setLevel(3);
            thirdLevel = categpryRepository.save(thirdLevelCategory);
        }

        Product product = new Product();
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscountedPrice(req.getDiscountedPrice());
        product.setDiscountedPercent(req.getDiscountedPercent());
        product.setImageUrl(req.getImageUrl());
        product.setBrand(req.getBrand());
        product.setPrice(req.getPrice());
        product.setSizes(req.getSizes());
        product.setQuantity(req.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        return savedProduct;


    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        product.getSizes().clear();
        productRepository.delete(product);
        return "Product deleted Successfully";

    }

    @Override
    public Product updateProduct(Long productId, Product req) throws ProductException {
        Product product = findProductById(productId);
        if (req.getQuantity() != 0) {
            product.setQuantity(req.getQuantity());
        }
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        Optional<Product> opt = productRepository.findById(productId);

        if (opt.isPresent()) {
            return opt.get();
        }
        throw new ProductException("Product not found with id " + productId);
    }

    @Override
    public List<Product> findProductByCategory(String category) {

        return List.of();
    }

    @Override
    public Page<Product> getAllFilterProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscounted, String sort, String stock, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Fetch initial products list based on filter criteria
        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscounted, sort);

        // Filter by colors
        if (!colors.isEmpty()) {
            products = products.stream()
                    .filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
                    .collect(Collectors.toList());
        }

        // Filter by stock status
        if (stock != null) {
            if (stock.equals("in_stock")) {
                products = products.stream()
                        .filter(p -> p.getQuantity() > 0)
                        .collect(Collectors.toList());
            } else if (stock.equals("out_of_stock")) {
                products = products.stream()
                        .filter(p -> p.getQuantity() < 1)
                        .collect(Collectors.toList());
            }
        }

        // Handle pagination
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());
        List<Product> pageContent = products.subList(startIndex, endIndex);

        // Create a paginated result
        return new PageImpl<>(pageContent, pageable, products.size());
    }
}