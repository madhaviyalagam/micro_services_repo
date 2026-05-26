package com.ecommerce.prodcut.service;

import com.ecommerce.prodcut.model.Product;
import com.ecommerce.prodcut.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public Product createProduct(Product product) {
        Product save = productRepository.save(product);
        return save;
    }


    public Optional<Product> updateProduct(Long id, Product product) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setCategory(product.getCategory());
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setImageUrl(product.getImageUrl());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStockQuantity(product.getStockQuantity());
            return productRepository.save(existingProduct);
        });
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllByActiveTrue();
    }

    public boolean deleteProduct(Long id) {
        productRepository.deleteById(id);
       return productRepository.existsById(id);
    }


    public List<Product> serachProduct(String keyword) {
        return productRepository.searchProduct(keyword);
    }
}
