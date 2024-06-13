package com.lahrach.blog.controller;

import org.springframework.web.bind.annotation.RestController;

import com.lahrach.blog.model.Post;
import com.lahrach.blog.service.PostService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    @Secured("ROLE_READER")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.findAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    @Secured("ROLE_READER")
    public void addPost(@RequestBody Post post) {
        postService.addPost(post);
    }
}
