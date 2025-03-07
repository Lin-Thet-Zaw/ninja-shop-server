package com.ninjashop.ninjashop.repository;

import com.ninjashop.ninjashop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p " +
            "WHERE (:category = '' OR p.category.name = :category) " +
            "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) " +
            "AND (:minDiscounted IS NULL OR p.discountedPercent >= :minDiscounted) " +
            "ORDER BY " +
            "CASE WHEN :sort = 'price_low' THEN p.discountedPrice END ASC, " +
            "CASE WHEN :sort = 'price_high' THEN p.discountedPrice END DESC")
    public List<Product> filterProducts(@Param("category") String category,
                                        @Param("minPrice") Integer minPrice,
                                        @Param("maxPrice") Integer maxPrice,
                                        @Param("minDiscounted") Integer minDiscounted,
                                        @Param("sort") String sort);
    // Fixed query to filter products by category only
    @Query("SELECT p FROM Product p WHERE (:category = '' OR p.category.name = :category)")
    public List<Product> findByCategory_Name(@Param("category") String category);
}
