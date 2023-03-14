package com.springsecurityaccess.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springsecurityaccess.demo.model.Post;

public interface Postrepository extends JpaRepository<Post, Integer>{

}
