package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import com.example.demo.entity.Intro;


public interface IntroRepository extends JpaRepository<Intro, Integer> {

	User findByEmail(String email);
    // JpaRepository already provides CRUD methods like save, findById, findAll, and deleteById
	
}
