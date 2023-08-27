package com.rms.wrapper;

import lombok.Data;

@Data
public class ProductWrapper {

    Integer id;

    String name;

    String description;

    String status;

    double price;

    Integer categoryId;

    String categoryName;

    public ProductWrapper(){

    }

    public ProductWrapper(Integer id, String name, String description, double price, String status, Integer categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.price = price;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
