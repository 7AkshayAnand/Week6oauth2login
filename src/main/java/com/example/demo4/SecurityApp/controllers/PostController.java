package com.example.demo4.SecurityApp.controllers;

import com.example.demo4.SecurityApp.dto.PostDTO;
import com.example.demo4.SecurityApp.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
   @Secured({"ROLE_USER","ROLE_ADMIN"})
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{postId}")
//    @PreAuthorize("hasAnyRole('USER','ADMIN') OR hasAuthority('POST_VIEW')")
//    above we do not need to add ROLE_ prefix to user as hasRole method does that automatically
    @PreAuthorize("@postSecurity.isOwnerOfPost(#postId)")
//    # is used to passing parameter to method
    public PostDTO getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    @PostMapping
    public PostDTO createNewPost(@RequestBody PostDTO inputPost) {
        return postService.createNewPost(inputPost);
    }

}
