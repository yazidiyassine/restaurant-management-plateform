package com.rms.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "User.findByEmailId", query = "SELECT u FROM User u WHERE u.email = :email")

@NamedQuery(name = "User.getAllUsers", query = "SELECT new com.rms.wrapper.UserWrapper(u.id, u.name, u.email ,u.contactNumber, u.status) FROM User u where u.role = 'USER'")

@NamedQuery(name = "User.updateStatus", query = "update User u set u.status = :status where u.id = :id")

@NamedQuery(name = "User.getAllAdmins", query = "SELECT u.email FROM User u where u.role = 'ADMIN'")

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "user")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;

}
