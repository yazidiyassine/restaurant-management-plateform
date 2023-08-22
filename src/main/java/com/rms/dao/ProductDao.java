package com.rms.dao;


import com.rms.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductDao extends JpaRepository<Product, Integer> {
}
