package com.lahrach.blog.repository;

import org.springframework.data.repository.CrudRepository;

import com.lahrach.blog.model.Post;

public interface PostRepository extends CrudRepository<Post, Long> {

}
