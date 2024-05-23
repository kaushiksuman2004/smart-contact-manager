package com.smart.scm2.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cId;

    @NotBlank(message = "Field is required !! *")
    @Size(min = 3, max = 50, message = "Name must be between 3 - 50 characters !!")
    private String name;
    
    @Size(min = 3, max = 50, message = "Name must be between 3 - 50 characters !!")
    private String secondName;

    @Column(unique = true)
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email format !!")
    @NotBlank(message = "Field is required !!")
    private String email;

    @NotBlank(message = "Field is required !! *")
    private String phone;

    private String image;

    @NotBlank(message = "Field is required !! *")
    private String work;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    private User user;

    public Contact() {
    }

    public Contact(int cId, String name, String secondName, String email, String phone, String image, String work,
            String description) {
        this.cId = cId;
        this.name = name;
        this.secondName = secondName;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.work = work;
        this.description = description;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
