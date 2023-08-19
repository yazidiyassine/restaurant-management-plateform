package com.rms.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@NamedQuery(name = "Category.getAllCategories", query= "SELECT c FROM Category c")

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "category")
@Data
public class Category implements java.io.Serializable{

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;
}
