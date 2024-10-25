package com.eshop.my_eshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eshop.my_eshop.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryName(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByName(String name);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    Product findByBrandAndName(String brand, String name);

    Long countByBrandAndName(String brand, String name);

    boolean existsByBrandAndName(String brand, String name);
}
