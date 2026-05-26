package com.ecommerce.prodcut.repository;


import com.ecommerce.prodcut.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getAllByActiveTrue();
    @Query("select p from products p where p.active = true and p.stockQuantity > 0 and LOWER(p.name) LIKE (CONCAT('%',:keyword, '%') )")
    List<Product> searchProduct(String keyword);
}
