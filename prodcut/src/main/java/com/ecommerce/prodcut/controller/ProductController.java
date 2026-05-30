package com.ecommerce.prodcut.controller;

import com.ecommerce.prodcut.model.Product;
import com.ecommerce.prodcut.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    private final ProductService productService;
    ProductController(ProductService productService){
        this.productService = productService;
    }
    @PostMapping("/create/products")
    ResponseEntity<String> createProduct(@RequestBody Product product){
        productService.createProduct(product);
        return new ResponseEntity<>("Product added Successfully", HttpStatus.CREATED);
    }
    @PutMapping("/create/products/{id}")
    ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product){
       return productService.updateProduct(id, product).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    @GetMapping("/products")
    ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @GetMapping("/products/{id}")
    ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);

        if (deleted) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.notFound().build(); // 404
    }
    @GetMapping("/search")
    ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        return ResponseEntity.ok(productService.serachProduct(keyword));
    }
}
