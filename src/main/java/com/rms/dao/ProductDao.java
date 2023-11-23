package com.rms.dao;


import com.rms.model.Product;
import com.rms.wrapper.ProductWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductDao extends JpaRepository<Product, Integer> {
    List<ProductWrapper> getAllProducts();

    @Modifying
    @Transactional
    Integer updateProductStatus(@Param("status") String status,@Param("id") int id);
}
