package com.example.demo.entity;  // Adjust the package to match your project structure

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Intro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment ID field in DB
    private int ID;
    private String name;
    private String email;
    private String password;
    private String username;

    // Getters and Setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Intro [ID=" + ID + ", name=" + name + ", email=" + email + ", password=" + password + ", username=" + username + "]";
    }
}
